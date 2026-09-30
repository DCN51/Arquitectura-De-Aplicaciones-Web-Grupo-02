package pe.edu.upc.arquiwebgrupo02.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.arquiwebgrupo02.dtos.CasoEmergenciaDTO;
import pe.edu.upc.arquiwebgrupo02.dtos.CasosPorPsicologoDTO;
import pe.edu.upc.arquiwebgrupo02.entities.CasoEmergencia;
import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;
import pe.edu.upc.arquiwebgrupo02.entities.Users;
import pe.edu.upc.arquiwebgrupo02.exceptions.ResourceNotFoundException;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ICasoEmergenciaService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ISesionClinicaService;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.IUserService;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/casos-emergencia")
public class CasoEmergenciaController {
    private final ICasoEmergenciaService ceS;
    private final IUserService uS;
    private final ISesionClinicaService scS;

    public CasoEmergenciaController(ICasoEmergenciaService ceS, IUserService uS, ISesionClinicaService scS) {
        this.ceS = ceS;
        this.uS = uS;
        this.scS = scS;
    }

    @GetMapping
    public ResponseEntity<List<CasoEmergenciaDTO>> listar() {
        List<CasoEmergenciaDTO> lista = ceS.list()
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<CasoEmergenciaDTO> registrar(@Valid @RequestBody CasoEmergenciaDTO dto) {
        CasoEmergencia c = new CasoEmergencia();
        llenarDesdeDTO(c, dto);
        ceS.insert(c);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(c.getCasoEmergenciaId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(toDTO(c));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CasoEmergenciaDTO> buscarPorId(@PathVariable Long id) {
        CasoEmergencia c = ceS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un caso de emergencia con el id: " + id
                        )
                );
        return ResponseEntity.ok(toDTO(c));
    }

    @PutMapping
    public ResponseEntity<CasoEmergenciaDTO> actualizar(@Valid @RequestBody CasoEmergenciaDTO dto) {
        CasoEmergencia existente = ceS.listId(dto.getCasoEmergenciaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un caso de emergencia con el id: " + dto.getCasoEmergenciaId()
                        )
                );
        llenarDesdeDTO(existente, dto);
        ceS.update(existente);
        return ResponseEntity.ok(toDTO(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        CasoEmergencia c = ceS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un caso de emergencia con el id: " + id
                        )
                );
        ceS.delete(c.getCasoEmergenciaId());
        return ResponseEntity.noContent().build();
    }

    // Query 1 - Casos pendientes ordenados por urgencia
    @GetMapping("/pendientes")
    public ResponseEntity<List<CasoEmergenciaDTO>> pendientesPorUrgencia() {
        List<CasoEmergenciaDTO> lista = ceS.listarPendientesPorUrgencia()
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // Query 2 - Cantidad de casos por psicologo
    @GetMapping("/por-psicologo")
    public ResponseEntity<List<CasosPorPsicologoDTO>> casosPorPsicologo() {
        List<CasosPorPsicologoDTO> lista = ceS.contarCasosPorPsicologo()
                .stream()
                .map(item -> {
                    CasosPorPsicologoDTO dto = new CasosPorPsicologoDTO();
                    dto.setNombrePsicologo((String) item[0]);
                    dto.setCantidadCasos(((Number) item[1]).longValue());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(lista);
    }

    // ---------- Conversiones ----------

    // DTO -> Entidad: busca paciente, sesion y psicologo (si viene) y copia los demas campos
    private void llenarDesdeDTO(CasoEmergencia c, CasoEmergenciaDTO dto) {
        Users paciente = uS.listId(dto.getUsuarioId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el paciente con el id: " + dto.getUsuarioId()
                        )
                );
        SesionClinica sesion = scS.listId(dto.getSesionClinicaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe la sesion clinica con el id: " + dto.getSesionClinicaId()
                        )
                );

        Users psicologo = null;
        if (dto.getPsicologoId() != null) {
            psicologo = uS.listId(dto.getPsicologoId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "No existe el psicologo con el id: " + dto.getPsicologoId()
                            )
                    );
            // El usuario asignado debe tener rol de psicologo
            String rol = psicologo.getRole().getRol().toUpperCase(Locale.ROOT);
            if (!rol.contains("PSICOLOGO")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El usuario asignado no es psicologo");
            }
        }

        c.setUsuario(paciente);
        c.setSesionClinica(sesion);
        c.setPsicologo(psicologo);
        c.setNivelUrgencia(dto.getNivelUrgencia());
        c.setResumenContextual(dto.getResumenContextual());
        c.setNotasPsicologo(dto.getNotasPsicologo());
        if (dto.getFechaEmergencia() != null) {
            c.setFechaEmergencia(dto.getFechaEmergencia());
        }
        if (dto.getEstado() != null) {
            c.setEstado(dto.getEstado());
        }
        if (dto.getFechaCierre() != null) {
            c.setFechaCierre(dto.getFechaCierre());
        }
    }

    // Entidad -> DTO: los objetos relacionados se convierten en sus ids
    private CasoEmergenciaDTO toDTO(CasoEmergencia c) {
        CasoEmergenciaDTO dto = new CasoEmergenciaDTO();
        dto.setCasoEmergenciaId(c.getCasoEmergenciaId());
        dto.setUsuarioId(c.getUsuario().getId());
        dto.setPsicologoId(c.getPsicologo() != null ? c.getPsicologo().getId() : null);
        dto.setSesionClinicaId(c.getSesionClinica().getSesionClinicaId());
        dto.setFechaEmergencia(c.getFechaEmergencia());
        dto.setNivelUrgencia(c.getNivelUrgencia());
        dto.setEstado(c.getEstado());
        dto.setResumenContextual(c.getResumenContextual());
        dto.setNotasPsicologo(c.getNotasPsicologo());
        dto.setFechaCierre(c.getFechaCierre());
        return dto;
    }
}