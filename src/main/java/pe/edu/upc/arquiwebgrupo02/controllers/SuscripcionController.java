package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.arquiwebgrupo02.dtos.SuscripcionDTO;
import pe.edu.upc.arquiwebgrupo02.entities.CatalogoPlan;
import pe.edu.upc.arquiwebgrupo02.entities.Suscripcion;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ICatalogoPlanService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ISuscripcionService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/suscripciones")
public class SuscripcionController {
    private final ISuscripcionService sS;
    private final ModelMapper modelMapper;
    private final IUserService uS;
    private final ICatalogoPlanService cpS;

    public SuscripcionController(ISuscripcionService sS, ModelMapper modelMapper,
                                 IUserService uS, ICatalogoPlanService cpS) {
        this.sS = sS;
        this.modelMapper = modelMapper;
        this.uS = uS;
        this.cpS = cpS;
    }

    @GetMapping
    public ResponseEntity<List<SuscripcionDTO>> listar() {
        List<SuscripcionDTO> lista = sS.list()
                .stream()
                .map(s -> modelMapper.map(s, SuscripcionDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    // US05 - Contratar plan
    @PostMapping
    public ResponseEntity<SuscripcionDTO> registrar(@Valid @RequestBody SuscripcionDTO dto) {
        Suscripcion s = armarSuscripcion(dto);
        sS.insert(s);

        SuscripcionDTO responseDTO = modelMapper.map(s, SuscripcionDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(s.getSuscripcionId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    // US06 - Cambiar plan
    @PutMapping("/cambiar-plan")
    public ResponseEntity<SuscripcionDTO> cambiarPlan(@Valid @RequestBody SuscripcionDTO dto) {
        Suscripcion s = armarSuscripcion(dto);
        sS.cambiarPlan(s);

        SuscripcionDTO responseDTO = modelMapper.map(s, SuscripcionDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    // US07 - Cancelar suscripcion
    @PutMapping("/usuario/{usuarioId}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long usuarioId) {
        uS.listId(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el usuario con el id: " + usuarioId
                        )
                );
        sS.cancelar(usuarioId);
        return ResponseEntity.ok().build();
    }

    // US08 - Consultar historial
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SuscripcionDTO>> historial(@PathVariable Long usuarioId) {
        List<SuscripcionDTO> lista = sS.historialPorUsuario(usuarioId)
                .stream()
                .map(s -> modelMapper.map(s, SuscripcionDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    // Busca el usuario y el plan (como hace la profesora con el invernadero)
    // y arma la suscripcion. Las fechas y el estado los pone el servicio.
    private Suscripcion armarSuscripcion(SuscripcionDTO dto) {
        Users u = uS.listId(dto.getUsuarioId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el usuario con el id: " + dto.getUsuarioId()
                        )
                );
        CatalogoPlan p = cpS.listId(dto.getCatalogoPlanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el plan con el id: " + dto.getCatalogoPlanId()
                        )
                );

        Suscripcion s = modelMapper.map(dto, Suscripcion.class);
        s.setSuscripcionId(null);      // siempre es una suscripcion nueva
        s.setUsuario(u);
        s.setCatalogoPlan(p);
        return s;
    }
}