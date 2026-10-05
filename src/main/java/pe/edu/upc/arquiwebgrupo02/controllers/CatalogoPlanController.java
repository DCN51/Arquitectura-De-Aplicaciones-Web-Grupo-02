package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.arquiwebgrupo02.dtos.CatalogoPlanDTO;
import pe.edu.upc.arquiwebgrupo02.entities.CatalogoPlan;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ICatalogoPlanService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/CatalogoPlan")
public class CatalogoPlanController {

    private final ICatalogoPlanService cpS;
    private final ModelMapper modelMapper;

    public CatalogoPlanController(ICatalogoPlanService cpS, ModelMapper modelMapper) {
        this.cpS = cpS;
        this.modelMapper = modelMapper;
    }

    // US04 - Listar planes disponibles (solo los activos)
    @GetMapping
    @PreAuthorize("hasAnyRole('PACIENTE','PSICOLOGO','ADMINISTRADOR')")
    public ResponseEntity<List<CatalogoPlanDTO>> listar() {
        List<CatalogoPlanDTO> lista = cpS.list()
                .stream()
                .filter(CatalogoPlan::isActivoCatalogoPlan)
                .map(c -> modelMapper.map(c, CatalogoPlanDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    // US01 - Registrar plan
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CatalogoPlanDTO> registrar(@Valid @RequestBody CatalogoPlanDTO dto) {
        CatalogoPlan c = modelMapper.map(dto, CatalogoPlan.class);
        c.setCatalogoPlanId(null);         // siempre es un plan nuevo
        c.setActivoCatalogoPlan(true);     // todo plan nuevo nace activo
        cpS.insert(c);

        CatalogoPlanDTO responseDTO = modelMapper.map(c, CatalogoPlanDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(c.getCatalogoPlanId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    // US02 - Actualizar plan (no cambia su estado activo/inactivo)
    @PutMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CatalogoPlanDTO> actualizar(@Valid @RequestBody CatalogoPlanDTO dto) {
        CatalogoPlan existente = buscarPlan(dto.getCatalogoPlanId());

        // El plan free debe seguir llamandose "free" para que se asigne a los pacientes nuevos
        if (esPlanFree(existente) && !"free".equalsIgnoreCase(dto.getNombreCatalogoPlan())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El nombre del plan free no se puede cambiar");
        }

        CatalogoPlan c = modelMapper.map(dto, CatalogoPlan.class);
        c.setCatalogoPlanId(existente.getCatalogoPlanId());
        c.setActivoCatalogoPlan(existente.isActivoCatalogoPlan());   // el estado solo cambia con /desactivar
        cpS.update(c);

        return ResponseEntity.ok(modelMapper.map(c, CatalogoPlanDTO.class));
    }

    // US03 - Desactivar plan (solo se envia el id)
    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CatalogoPlanDTO> desactivar(@PathVariable Long id) {
        CatalogoPlan c = buscarPlan(id);

        if (esPlanFree(c)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El plan free no se puede desactivar");
        }
        if (!c.isActivoCatalogoPlan()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El plan ya está desactivado");
        }

        c.setActivoCatalogoPlan(false);
        cpS.update(c);
        return ResponseEntity.ok(modelMapper.map(c, CatalogoPlanDTO.class));
    }

    // ---------- Metodos de apoyo ----------

    // Busca el plan o responde 404 si no existe
    private CatalogoPlan buscarPlan(Long id) {
        return cpS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un plan con el id: " + id
                        )
                );
    }

    // El plan free es el que se asigna automaticamente a los pacientes nuevos
    private boolean esPlanFree(CatalogoPlan c) {
        return "free".equalsIgnoreCase(c.getNombreCatalogoPlan());
    }
}