package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.arquiwebgrupo02.dtos.SesionClinicaDTO;
import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ISesionClinicaService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
public class SesionClinicaController {
    private final ISesionClinicaService scS;
    private final IUserService uS;

    public SesionClinicaController(ISesionClinicaService scS, IUserService uS) {
        this.scS = scS;
        this.uS = uS;
    }

    @PostMapping
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<SesionClinicaDTO> iniciar(Authentication authentication) {
        Users paciente = uS.usuarioActual(authentication);
        SesionClinica creada = scS.iniciar(paciente);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getSesionClinicaId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(toDTO(creada));
    }

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<SesionClinicaDTO> finalizar(@PathVariable Long id, @Valid @RequestBody SesionClinicaDTO dto,
                                                      Authentication authentication) {
        SesionClinica datos = new SesionClinica();
        datos.setEmocionDetectada(dto.getEmocionDetectada());
        datos.setNivelUrgencia(dto.getNivelUrgencia());
        datos.setResumenIa(dto.getResumenIa());
        datos.setRequiereDerivacion(dto.getRequiereDerivacion());

        SesionClinica finalizada = scS.finalizar(id, datos, uS.usuarioActual(authentication));
        return ResponseEntity.ok(toDTO(finalizada));
    }

    @GetMapping("/mis-sesiones")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<SesionClinicaDTO>> misSesiones(Authentication authentication) {
        Users paciente = uS.usuarioActual(authentication);
        List<SesionClinicaDTO> lista = scS.historialPorUsuario(paciente.getId())
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<SesionClinicaDTO> listarPorId(@PathVariable Long id, Authentication authentication) {
        SesionClinica s = scS.buscarDelPaciente(id, uS.usuarioActual(authentication));
        return ResponseEntity.ok(toDTO(s));
    }

    @GetMapping("/derivaciones")
    @PreAuthorize("hasRole('PSICOLOGO')")
    public ResponseEntity<List<SesionClinicaDTO>> derivaciones(Authentication authentication) {
        List<SesionClinicaDTO> lista = scS.listarDerivacionesDePsicologo(uS.usuarioActual(authentication))
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication authentication) {
        scS.delete(id, uS.usuarioActual(authentication));
        return ResponseEntity.noContent().build();
    }

    private SesionClinicaDTO toDTO(SesionClinica s) {
        SesionClinicaDTO dto = new SesionClinicaDTO();
        dto.setSesionClinicaId(s.getSesionClinicaId());
        dto.setUsuarioId(s.getUsuario().getId());
        dto.setNombrePaciente(uS.nombreCompleto(s.getUsuario()));
        dto.setFechaHoraInicio(s.getFechaHoraInicio());
        dto.setFechaHoraFin(s.getFechaHoraFin());
        dto.setEmocionDetectada(s.getEmocionDetectada());
        dto.setNivelUrgencia(s.getNivelUrgencia());
        dto.setResumenIa(s.getResumenIa());
        dto.setRequiereDerivacion(s.getRequiereDerivacion());
        dto.setEstado(s.getEstado());
        return dto;
    }
}
