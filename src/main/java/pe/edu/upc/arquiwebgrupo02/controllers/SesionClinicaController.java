package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.arquiwebgrupo02.dtos.SesionClinicaDTO;
import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
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

    // HU05 - Iniciar una sesion con la IA (solo se envia el usuarioId)
    @PostMapping
    public ResponseEntity<SesionClinicaDTO> iniciar(@Valid @RequestBody SesionClinicaDTO dto) {
        Users u = uS.listId(dto.getUsuarioId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el usuario con el id: " + dto.getUsuarioId()
                        )
                );

        SesionClinica s = new SesionClinica();
        s.setUsuario(u);
        SesionClinica creada = scS.iniciar(s);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getSesionClinicaId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(modelMapper.map(creada, SesionClinicaDTO.class));
    }

    // HU05 y HU14 - Finalizar la sesion con el resultado de la IA
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<SesionClinicaDTO> finalizar(@PathVariable Long id, @RequestBody SesionClinicaDTO dto) {
        SesionClinica datos = new SesionClinica();
        datos.setEmocionDetectada(dto.getEmocionDetectada());
        datos.setNivelUrgencia(dto.getNivelUrgencia());
        datos.setResumenIa(dto.getResumenIa());
        datos.setRequiereDerivacion(dto.getRequiereDerivacion());

        SesionClinica finalizada = scS.finalizar(id, datos);
        return ResponseEntity.ok(modelMapper.map(finalizada, SesionClinicaDTO.class));
    }

    // Ver una sesion
    @GetMapping("/{id}")
    public ResponseEntity<SesionClinicaDTO> listarPorId(@PathVariable Long id) {
        SesionClinica s = scS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe la sesión con el id: " + id
                        )
                );
        return ResponseEntity.ok(modelMapper.map(s, SesionClinicaDTO.class));
    }

    // Historial de sesiones del usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SesionClinicaDTO>> historial(@PathVariable Long usuarioId) {
        uS.listId(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el usuario con el id: " + usuarioId
                        )
                );
        List<SesionClinicaDTO> lista = scS.historialPorUsuario(usuarioId)
                .stream()
                .map(s -> modelMapper.map(s, SesionClinicaDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    // HU11 - Sesiones que requieren derivacion (solo psicologos y admin)
    @GetMapping("/derivaciones")
    @PreAuthorize("hasAnyAuthority('PSICOLOGO', 'ROLE_PSICOLOGO', 'ADMINISTRADOR', 'ROLE_ADMINISTRADOR')")
    public ResponseEntity<List<SesionClinicaDTO>> derivaciones() {
        List<SesionClinicaDTO> lista = scS.listarQueRequierenDerivacion()
                .stream()
                .map(s -> modelMapper.map(s, SesionClinicaDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    // HU38 - Eliminar una sesion del historial
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        scS.delete(id);
        return ResponseEntity.noContent().build();
    }
}