package pe.edu.upc.arquiwebgrupo02.servicesimplements;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upc.arquiwebgrupo02.entities.SesionClinica;
import pe.edu.upc.arquiwebgrupo02.repositories.ISesionClinicaRepository;
import pe.edu.upc.arquiwebgrupo02.servicesinterfaces.ISesionClinicaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SesionClinicaServiceImplement implements ISesionClinicaService {
    private final ISesionClinicaRepository scR;

    public SesionClinicaServiceImplement(ISesionClinicaRepository scR) {
        this.scR = scR;
    }

    // HU05 - Iniciar sesion con la IA
    @Override
    public SesionClinica iniciar(SesionClinica s) {
        // Un usuario no puede tener dos sesiones abiertas a la vez
        if (scR.buscarEnCursoPorUsuario(s.getUsuario().getId()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya tienes una sesión en curso");
        }

        // Lo decide el sistema, no el cliente
        s.setSesionClinicaId(null);
        s.setFechaHoraInicio(LocalDateTime.now());
        s.setFechaHoraFin(null);
        s.setEstado("en_curso");
        s.setNivelUrgencia("bajo");
        s.setRequiereDerivacion(false);
        s.setEmocionDetectada(null);
        s.setResumenIa(null);

        return scR.save(s);
    }

    // HU05 y HU14 - Finalizar sesion y guardar el resumen emocional de la IA
    @Override
    public SesionClinica finalizar(Long sesionClinicaId, SesionClinica datos) {
        SesionClinica actual = scR.findById(sesionClinicaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sesión no encontrada"));

        if (!"en_curso".equals(actual.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sesión ya fue finalizada");
        }

        // Validar el nivel de urgencia si lo mandan
        String urgencia = datos.getNivelUrgencia() == null ? actual.getNivelUrgencia() : datos.getNivelUrgencia().toLowerCase();
        if (!List.of("bajo", "medio", "alto", "critico").contains(urgencia)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nivel de urgencia inválido (bajo, medio, alto, critico)");
        }

        actual.setFechaHoraFin(LocalDateTime.now());
        actual.setEmocionDetectada(datos.getEmocionDetectada());
        actual.setResumenIa(datos.getResumenIa());
        actual.setNivelUrgencia(urgencia);

        // Si la urgencia es alta o critica, siempre se deriva a un psicologo (HU11)
        boolean derivar = Boolean.TRUE.equals(datos.getRequiereDerivacion())
                || urgencia.equals("alto") || urgencia.equals("critico");
        actual.setRequiereDerivacion(derivar);

        actual.setEstado("finalizada");
        return scR.save(actual);
    }

    @Override
    public Optional<SesionClinica> listId(Long sesionClinicaId) {
        return scR.findById(sesionClinicaId);
    }

    @Override
    public List<SesionClinica> historialPorUsuario(Long usuarioId) {
        return scR.buscarHistorialPorUsuario(usuarioId);
    }

    @Override
    public List<SesionClinica> listarQueRequierenDerivacion() {
        return scR.buscarQueRequierenDerivacion();
    }

    // HU38 - Eliminar una sesion del historial
    @Override
    public void delete(Long sesionClinicaId) {
        SesionClinica s = scR.findById(sesionClinicaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sesión no encontrada"));
        if ("en_curso".equals(s.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes eliminar una sesión en curso");
        }
        scR.deleteById(sesionClinicaId);
    }
}