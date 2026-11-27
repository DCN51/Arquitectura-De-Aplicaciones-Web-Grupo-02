package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class AsignacionPacienteDTO {
    private Long asignacionPacienteId;

    @NotNull(message = "El id del paciente es obligatorio")
    @Positive(message = "El id del paciente debe ser mayor que 0")
    private Long pacienteId;

    @NotNull(message = "El id del psicólogo es obligatorio")
    @Positive(message = "El id del psicólogo debe ser mayor que 0")
    private Long psicologoId;

    private String nombrePaciente;

    private String nombrePsicologo;

    private LocalDate fechaAsignacion;

    private boolean activa;

    public Long getAsignacionPacienteId() {
        return asignacionPacienteId;
    }

    public void setAsignacionPacienteId(Long asignacionPacienteId) {
        this.asignacionPacienteId = asignacionPacienteId;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getPsicologoId() {
        return psicologoId;
    }

    public void setPsicologoId(Long psicologoId) {
        this.psicologoId = psicologoId;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNombrePsicologo() {
        return nombrePsicologo;
    }

    public void setNombrePsicologo(String nombrePsicologo) {
        this.nombrePsicologo = nombrePsicologo;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
