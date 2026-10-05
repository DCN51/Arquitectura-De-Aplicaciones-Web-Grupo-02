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
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.repositories.IUsuarioRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IDiagnosticoClinicoService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos")
public class DiagnosticoClinicoController {
    private final IDiagnosticoClinicoService dcS;
    private final IUserService uS;
    private final IUsuarioRepository uR;

    public DiagnosticoClinicoController(IDiagnosticoClinicoService dcS, IUserService uS, IUsuarioRepository uR) {
        this.dcS = dcS;
        this.uS = uS;
        this.uR = uR;
    }

    @PostMapping
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<DiagnosticoClinicoDTO> registrar(@Valid @RequestBody DiagnosticoClinicoDTO dto) {
        SesionClinica sesion = new SesionClinica();
        sesion.setSesionClinicaId(dto.getSesionClinicaId());

        DiagnosticoClinico d = new DiagnosticoClinico();
        d.setSesionClinica(sesion);
        d.setResultadoPrincipal(dto.getResultadoPrincipal().trim());
        d.setDescripcionDetallada(dto.getDescripcionDetallada());
        d.setNivelRiesgo(dto.getNivelRiesgo().trim());
        d.setRecomendacionGeneral(dto.getRecomendacionGeneral());

        DiagnosticoClinico creado = dcS.registrar(d);

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
        Users psicologo = uR.findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el usuario: " + authentication.getName()
                        )
                );
        return ResponseEntity.ok(toDTO(dcS.validar(id, psicologo)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE','PSICOLOGO')")
    public ResponseEntity<DiagnosticoClinicoDTO> listarPorId(@PathVariable Long id) {
        DiagnosticoClinico d = dcS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el diagnóstico con el id: " + id
                        )
                );
        return ResponseEntity.ok(toDTO(d));
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('PACIENTE','PSICOLOGO')")
    public ResponseEntity<List<DiagnosticoClinicoDTO>> historial(@PathVariable Long usuarioId) {
        uS.listId(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el usuario con el id: " + usuarioId
                        )
                );
        List<DiagnosticoClinicoDTO> lista = dcS.historialPorUsuario(usuarioId)
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('PSICOLOGO','ADMINISTRADOR')")
    public ResponseEntity<List<DiagnosticoClinicoDTO>> pendientes() {
        List<DiagnosticoClinicoDTO> lista = dcS.listarPendientesDeValidar()
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/psicologo/{psicologoId}")
    @PreAuthorize("hasAnyRole('PSICOLOGO','ADMINISTRADOR')")
    public ResponseEntity<List<DiagnosticoClinicoDTO>> validadosPorPsicologo(@PathVariable Long psicologoId) {
        List<DiagnosticoClinicoDTO> lista = dcS.listarValidadosPorPsicologo(psicologoId)
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