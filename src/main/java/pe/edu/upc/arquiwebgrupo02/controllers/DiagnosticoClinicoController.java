package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.arquiwebgrupo02.dtos.DiagnosticoClinicoDTO;
import pe.edu.upc.arquiwebgrupo02.entities.DiagnosticoClinico;
import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IDiagnosticoClinicoService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos")
public class DiagnosticoClinicoController {
    private final IDiagnosticoClinicoService dcS;
    private final IUserService uS;

    public DiagnosticoClinicoController(IDiagnosticoClinicoService dcS, IUserService uS) {
        this.dcS = dcS;
        this.uS = uS;
    }

    @PostMapping
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<DiagnosticoClinicoDTO> registrar(@Valid @RequestBody DiagnosticoClinicoDTO dto,
                                                           Authentication authentication) {
        SesionClinica sesion = new SesionClinica();
        sesion.setSesionClinicaId(dto.getSesionClinicaId());

        DiagnosticoClinico d = new DiagnosticoClinico();
        d.setSesionClinica(sesion);
        d.setResultadoPrincipal(dto.getResultadoPrincipal().trim());
        d.setDescripcionDetallada(dto.getDescripcionDetallada());
        d.setNivelRiesgo(dto.getNivelRiesgo().trim());
        d.setRecomendacionGeneral(dto.getRecomendacionGeneral());

        DiagnosticoClinico creado = dcS.registrar(d, uS.usuarioActual(authentication));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getDiagnosticoClinicoId())
                .toUri();

        return ResponseEntity.created(location).body(toDTO(creado));
    }

    @PutMapping("/{id}/validar")
    @PreAuthorize("hasRole('PSICOLOGO')")
    public ResponseEntity<DiagnosticoClinicoDTO> validar(@PathVariable Long id, Authentication authentication) {
        Users psicologo = uS.usuarioActual(authentication);
        return ResponseEntity.ok(toDTO(dcS.validar(id, psicologo)));
    }

    @GetMapping("/mis-diagnosticos")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<DiagnosticoClinicoDTO>> misDiagnosticos(Authentication authentication) {
        Users paciente = uS.usuarioActual(authentication);
        List<DiagnosticoClinicoDTO> lista = dcS.historialPorUsuario(paciente.getId())
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE','PSICOLOGO')")
    public ResponseEntity<DiagnosticoClinicoDTO> listarPorId(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(toDTO(dcS.buscarParaUsuario(id, uS.usuarioActual(authentication))));
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('PSICOLOGO','ADMINISTRADOR')")
    public ResponseEntity<List<DiagnosticoClinicoDTO>> historial(@PathVariable Long usuarioId,
                                                                 Authentication authentication) {
        List<DiagnosticoClinicoDTO> lista = dcS.historialParaUsuario(usuarioId, uS.usuarioActual(authentication))
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('PSICOLOGO','ADMINISTRADOR')")
    public ResponseEntity<List<DiagnosticoClinicoDTO>> pendientes(Authentication authentication) {
        List<DiagnosticoClinicoDTO> lista = dcS.listarPendientesParaUsuario(uS.usuarioActual(authentication))
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/psicologo/{psicologoId}")
    @PreAuthorize("hasAnyRole('PSICOLOGO','ADMINISTRADOR')")
    public ResponseEntity<List<DiagnosticoClinicoDTO>> validadosPorPsicologo(@PathVariable Long psicologoId,
                                                                             Authentication authentication) {
        List<DiagnosticoClinicoDTO> lista = dcS.listarValidadosPorPsicologo(psicologoId, uS.usuarioActual(authentication))
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        dcS.delete(id);
        return ResponseEntity.noContent().build();
    }

    private DiagnosticoClinicoDTO toDTO(DiagnosticoClinico d) {
        DiagnosticoClinicoDTO dto = new DiagnosticoClinicoDTO();
        dto.setDiagnosticoClinicoId(d.getDiagnosticoClinicoId());
        dto.setUsuarioId(d.getUsuario().getId());
        dto.setNombrePaciente(uS.nombreCompleto(d.getUsuario()));
        dto.setNombrePsicologo(uS.nombreCompleto(d.getValidacionPsicologo()));
        dto.setSesionClinicaId(d.getSesionClinica().getSesionClinicaId());
        dto.setFechaDiagnostico(d.getFechaDiagnostico());
        dto.setResultadoPrincipal(d.getResultadoPrincipal());
        dto.setDescripcionDetallada(d.getDescripcionDetallada());
        dto.setNivelRiesgo(d.getNivelRiesgo());
        dto.setRecomendacionGeneral(d.getRecomendacionGeneral());
        dto.setValidacionPsicologoId(d.getValidacionPsicologo() == null ? null : d.getValidacionPsicologo().getId());
        dto.setFechaValidacion(d.getFechaValidacion());
        return dto;
    }
}
