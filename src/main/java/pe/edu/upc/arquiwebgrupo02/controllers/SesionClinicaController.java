package pe.edu.upc.arquiwebgrupo02.controllers;

import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public SesionClinicaController(ISesionClinicaService scS, IUserService uS, ModelMapper modelMapper) {
        this.scS = scS;
        this.uS = uS;
        this.modelMapper = modelMapper;
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
                .body(modelMapper.map(creada, SesionClinicaDTO.class));
    }

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<SesionClinicaDTO> finalizar(@PathVariable Long id, @RequestBody SesionClinicaDTO dto,
                                                      Authentication authentication) {
        SesionClinica datos = new SesionClinica();
        datos.setEmocionDetectada(dto.getEmocionDetectada());
        datos.setNivelUrgencia(dto.getNivelUrgencia());
        datos.setResumenIa(dto.getResumenIa());
        datos.setRequiereDerivacion(dto.getRequiereDerivacion());

        SesionClinica finalizada = scS.finalizar(id, datos, uS.usuarioActual(authentication));
        return ResponseEntity.ok(modelMapper.map(finalizada, SesionClinicaDTO.class));
    }

    @GetMapping("/mis-sesiones")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<SesionClinicaDTO>> misSesiones(Authentication authentication) {
        Users paciente = uS.usuarioActual(authentication);
        List<SesionClinicaDTO> lista = scS.historialPorUsuario(paciente.getId())
                .stream()
                .map(s -> modelMapper.map(s, SesionClinicaDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<SesionClinicaDTO> listarPorId(@PathVariable Long id, Authentication authentication) {
        SesionClinica s = scS.buscarDelPaciente(id, uS.usuarioActual(authentication));
        return ResponseEntity.ok(modelMapper.map(s, SesionClinicaDTO.class));
    }

    @GetMapping("/derivaciones")
    @PreAuthorize("hasRole('PSICOLOGO')")
    public ResponseEntity<List<SesionClinicaDTO>> derivaciones() {
        List<SesionClinicaDTO> lista = scS.listarQueRequierenDerivacion()
                .stream()
                .map(s -> modelMapper.map(s, SesionClinicaDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication authentication) {
        scS.delete(id, uS.usuarioActual(authentication));
        return ResponseEntity.noContent().build();
    }
}
