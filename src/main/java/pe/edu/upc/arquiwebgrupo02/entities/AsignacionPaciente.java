package pe.edu.upc.arquiwebgrupo02.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "asignaciones_pacientes")
public class AsignacionPaciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asignacionPacienteId")
    private Long asignacionPacienteId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pacienteId", nullable = false)
    private Users paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "psicologoId", nullable = false)
    private Users psicologo;

    @Column(name = "fechaAsignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @Column(name = "activa", nullable = false)
    private boolean activa;

    public AsignacionPaciente() {
    }

    public AsignacionPaciente(Long asignacionPacienteId, Users paciente, Users psicologo, LocalDate fechaAsignacion, boolean activa) {
        this.asignacionPacienteId = asignacionPacienteId;
        this.paciente = paciente;
        this.psicologo = psicologo;
        this.fechaAsignacion = fechaAsignacion;
        this.activa = activa;
    }

    public Long getAsignacionPacienteId() {
        return asignacionPacienteId;
    }

    public void setAsignacionPacienteId(Long asignacionPacienteId) {
        this.asignacionPacienteId = asignacionPacienteId;
    }

    public Users getPaciente() {
        return paciente;
    }

    public void setPaciente(Users paciente) {
        this.paciente = paciente;
    }

    public Users getPsicologo() {
        return psicologo;
    }

    public void setPsicologo(Users psicologo) {
        this.psicologo = psicologo;
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
