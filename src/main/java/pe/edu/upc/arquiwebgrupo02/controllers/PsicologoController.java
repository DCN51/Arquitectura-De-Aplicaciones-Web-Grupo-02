package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.arquiwebgrupo02.dtos.CapacidadPsicologoDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.PsicologoDTO;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IAsignacionPacienteService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IPsicologoService;

import java.util.List;

@RestController
@RequestMapping("/api/psicologos")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class PsicologoController {
    private final IPsicologoService pS;
    private final IAsignacionPacienteService apS;

    public PsicologoController(IPsicologoService pS, IAsignacionPacienteService apS) {
        this.pS = pS;
        this.apS = apS;
    }

    @GetMapping
    public ResponseEntity<List<PsicologoDTO>> listar() {
        return ResponseEntity.ok(pS.listar().stream().map(this::toDTO).toList());
    }

    @PatchMapping("/{id}/verificar")
    public ResponseEntity<PsicologoDTO> verificar(@PathVariable Long id) {
        return ResponseEntity.ok(toDTO(pS.verificar(id)));
    }

    @PutMapping("/{id}/capacidad")
    public ResponseEntity<PsicologoDTO> cambiarCapacidad(@PathVariable Long id,
                                                         @Valid @RequestBody CapacidadPsicologoDTO dto) {
        return ResponseEntity.ok(toDTO(pS.cambiarCapacidad(id, dto.getMaxPacientes())));
    }

    private PsicologoDTO toDTO(Users u) {
        PsicologoDTO dto = new PsicologoDTO();
        dto.setPsicologoId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setNombres(u.getNombres());
        dto.setApellidos(u.getApellidos());
        dto.setCorreoElectronico(u.getCorreoElectronico());
        dto.setNumeroColegiatura(u.getNumeroColegiatura());
        dto.setEspecializacion(u.getEspecializacion());
        dto.setAnosExperiencia(u.getAnosExperiencia());
        dto.setEstadoVerificacion(u.getEstadoVerificacion());
        dto.setEstadoCuenta(u.getEstadoCuenta());
        dto.setMaxPacientes(u.getMaxPacientes());
        dto.setPacientesAsignados(apS.contarAsignados(u.getId()));
        return dto;
    }
}
