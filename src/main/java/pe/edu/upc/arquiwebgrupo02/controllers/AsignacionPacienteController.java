package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.arquiwebgrupo02.dtos.AsignacionPacienteDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.PacientesPorPsicologoDTO;
import pe.edu.upc.arquiwebgrupo02.entities.AsignacionPaciente;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IAsignacionPacienteService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AsignacionPacienteController {
    private final IAsignacionPacienteService apS;

    public AsignacionPacienteController(IAsignacionPacienteService apS) {
        this.apS = apS;
    }

    @GetMapping
    public ResponseEntity<List<AsignacionPacienteDTO>> listarActivas() {
        return ResponseEntity.ok(apS.listarActivas().stream().map(this::toDTO).toList());
    }

    @PostMapping
    public ResponseEntity<AsignacionPacienteDTO> asignar(@Valid @RequestBody AsignacionPacienteDTO dto) {
        AsignacionPaciente creada = apS.asignar(dto.getPacienteId(), dto.getPsicologoId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getAsignacionPacienteId())
                .toUri();

        return ResponseEntity.created(location).body(toDTO(creada));
    }

    @GetMapping("/pacientes-por-psicologo")
    public ResponseEntity<List<PacientesPorPsicologoDTO>> pacientesPorPsicologo() {
        List<PacientesPorPsicologoDTO> lista = apS.contarPacientesPorPsicologo()
                .stream()
                .map(item -> {
                    PacientesPorPsicologoDTO dto = new PacientesPorPsicologoDTO();
                    dto.setNombrePsicologo((String) item[0]);
                    dto.setCantidadPacientes(((Number) item[1]).longValue());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(lista);
    }

    private AsignacionPacienteDTO toDTO(AsignacionPaciente a) {
        AsignacionPacienteDTO dto = new AsignacionPacienteDTO();
        dto.setAsignacionPacienteId(a.getAsignacionPacienteId());
        dto.setPacienteId(a.getPaciente().getId());
        dto.setNombrePaciente(a.getPaciente().getNombres() + " " + a.getPaciente().getApellidos());
        dto.setPsicologoId(a.getPsicologo().getId());
        dto.setNombrePsicologo(a.getPsicologo().getNombres() + " " + a.getPsicologo().getApellidos());
        dto.setFechaAsignacion(a.getFechaAsignacion());
        dto.setActiva(a.isActiva());
        return dto;
    }
}
