package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    public CatalogoPlanController(ICatalogoPlanService cPS, ICatalogoPlanService cpS, ModelMapper modelMapper) {
        this.cpS = cpS;
        this.modelMapper = modelMapper;
    }

    // US04 - Listar planes disponibles (solo los activos)
    @GetMapping
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
    public ResponseEntity<CatalogoPlanDTO> registrar(@Valid @RequestBody CatalogoPlanDTO dto) {
        CatalogoPlan c = modelMapper.map(dto, CatalogoPlan.class);
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

    @GetMapping("/{id}")
    public ResponseEntity<CatalogoPlanDTO> buscarPorId(@PathVariable Long id) {
        CatalogoPlan c = cpS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un plan con el id: " + id
                        )
                );
        return ResponseEntity.ok(modelMapper.map(c, CatalogoPlanDTO.class));
    }

    // US02 - Actualizar plan  /  US03 - Desactivar plan (enviando activoCatalogoPlan = false)
    @PutMapping
    public ResponseEntity<CatalogoPlanDTO> actualizar(@Valid @RequestBody CatalogoPlanDTO dto) {
        CatalogoPlan existente = cpS.listId(dto.getCatalogoPlanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un plan con el id: " + dto.getCatalogoPlanId()
                        )
                );
        CatalogoPlan c = modelMapper.map(dto, CatalogoPlan.class);
        c.setCatalogoPlanId(existente.getCatalogoPlanId());
        cpS.update(c);

        return ResponseEntity.ok(modelMapper.map(c, CatalogoPlanDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        CatalogoPlan c = cpS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un plan con el id: " + id
                        )
                );
        cpS.delete(c.getCatalogoPlanId());
        return ResponseEntity.noContent().build();
    }

}
