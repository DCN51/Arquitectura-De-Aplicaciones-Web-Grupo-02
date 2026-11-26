package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.arquiwebgrupo02.dtos.SuscripcionDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.SuscriptoresPorPlanDTO;
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
    private final IUserService uS;
    private final ICatalogoPlanService cpS;

    public SuscripcionController(ISuscripcionService sS, IUserService uS, ICatalogoPlanService cpS) {
        this.sS = sS;
        this.uS = uS;
        this.cpS = cpS;
    }

    // US05 - Contratar plan
    @PostMapping
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<SuscripcionDTO> registrar(@Valid @RequestBody SuscripcionDTO dto,
                                                    Authentication authentication) {
        Suscripcion s = armarSuscripcion(dto, uS.usuarioActual(authentication));
        sS.insert(s);

        SuscripcionDTO responseDTO = toDTO(s);

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
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<SuscripcionDTO> cambiarPlan(@Valid @RequestBody SuscripcionDTO dto,
                                                      Authentication authentication) {
        Suscripcion s = armarSuscripcion(dto, uS.usuarioActual(authentication));
        sS.cambiarPlan(s);

        SuscripcionDTO responseDTO = toDTO(s);
        return ResponseEntity.ok(responseDTO);
    }

    // US07 - Cancelar suscripcion
    @PutMapping("/cancelar")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<Void> cancelar(Authentication authentication) {
        sS.cancelar(uS.usuarioActual(authentication).getId());
        return ResponseEntity.ok().build();
    }

    // US08 - Consultar historial
    @GetMapping("/mis-suscripciones")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<SuscripcionDTO>> misSuscripciones(Authentication authentication) {
        return ResponseEntity.ok(historialDe(uS.usuarioActual(authentication).getId()));
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<SuscripcionDTO>> historial(@PathVariable Long usuarioId) {
        uS.buscarPorId(usuarioId);
        return ResponseEntity.ok(historialDe(usuarioId));
    }

    // US09 - Consultar suscripciones por vencer (@Query)
    @GetMapping("/por-vencer")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<SuscripcionDTO>> porVencer(@RequestParam(defaultValue = "7") int dias) {
        List<SuscripcionDTO> lista = sS.porVencer(dias)
                .stream()
                .map(s -> toDTO(s))
                .toList();
        return ResponseEntity.ok(lista);
    }

    // US10 - Contar suscriptores activos por plan (@Query)
    @GetMapping("/suscriptores-por-plan")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<SuscriptoresPorPlanDTO>> suscriptoresPorPlan() {
        List<SuscriptoresPorPlanDTO> lista = sS.contarSuscriptoresActivosPorPlan()
                .stream()
                .map(item -> {
                    SuscriptoresPorPlanDTO dto = new SuscriptoresPorPlanDTO();
                    dto.setNombrePlan((String) item[0]);
                    dto.setCantidadSuscriptores(((Number) item[1]).longValue());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(lista);
    }

    private List<SuscripcionDTO> historialDe(Long usuarioId) {
        return sS.historialPorUsuario(usuarioId)
                .stream()
                .map(s -> toDTO(s))
                .toList();
    }

    private SuscripcionDTO toDTO(Suscripcion s) {
        SuscripcionDTO dto = new SuscripcionDTO();
        dto.setSuscripcionId(s.getSuscripcionId());
        dto.setFechaInicio(s.getFechaInicio());
        dto.setFechaFin(s.getFechaFin());
        dto.setEstadoSuscripcion(s.getEstadoSuscripcion());
        dto.setMetodoPagoSuscripcion(s.getMetodoPagoSuscripcion());
        dto.setReferenciaPagoSuscripcion(s.getReferenciaPagoSuscripcion());
        dto.setUsuarioId(s.getUsuario().getId());
        dto.setNombrePaciente(uS.nombreCompleto(s.getUsuario()));
        dto.setCatalogoPlanId(s.getCatalogoPlan().getCatalogoPlanId());
        dto.setNombrePlan(s.getCatalogoPlan().getNombreCatalogoPlan());
        return dto;
    }

    private Suscripcion armarSuscripcion(SuscripcionDTO dto, Users usuario) {
        CatalogoPlan p = cpS.listId(dto.getCatalogoPlanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el plan con el id: " + dto.getCatalogoPlanId()
                        )
                );

        Suscripcion s = new Suscripcion();
        s.setUsuario(usuario);
        s.setCatalogoPlan(p);
        s.setMetodoPagoSuscripcion(dto.getMetodoPagoSuscripcion());
        s.setReferenciaPagoSuscripcion(dto.getReferenciaPagoSuscripcion());
        return s;
    }
}
