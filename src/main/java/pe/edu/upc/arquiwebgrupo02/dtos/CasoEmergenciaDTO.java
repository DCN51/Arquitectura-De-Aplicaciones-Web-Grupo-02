package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CasoEmergenciaDTO {

    private Long casoEmergenciaId;

    @NotNull(message = "El id del paciente es obligatorio")
    private Long usuarioId;

    // Opcional: el caso puede crearse sin psicologo y asignarse despues
    private Long psicologoId;

    @NotNull(message = "El id de la sesion clinica es obligatorio")
    private Long sesionClinicaId;

    // Si no se envia, se pone la fecha actual
    private LocalDateTime fechaEmergencia;

    @NotBlank(message = "El nivel de urgencia es obligatorio")
    private String nivelUrgencia;

    // Si no se envia, queda como "pendiente"
    private String estado;

    private String resumenContextual;

    private String notasPsicologo;

    private LocalDateTime fechaCierre;

    public Long getCasoEmergenciaId() {
        return casoEmergenciaId;
    }

    public void setCasoEmergenciaId(Long casoEmergenciaId) {
        this.casoEmergenciaId = casoEmergenciaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getPsicologoId() {
        return psicologoId;
    }

    public void setPsicologoId(Long psicologoId) {
        this.psicologoId = psicologoId;
    }

    public Long getSesionClinicaId() {
        return sesionClinicaId;
    }

    public void setSesionClinicaId(Long sesionClinicaId) {
        this.sesionClinicaId = sesionClinicaId;
    }

    public LocalDateTime getFechaEmergencia() {
        return fechaEmergencia;
    }

    public void setFechaEmergencia(LocalDateTime fechaEmergencia) {
        this.fechaEmergencia = fechaEmergencia;
    }

    public String getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(String nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getResumenContextual() {
        return resumenContextual;
    }

    public void setResumenContextual(String resumenContextual) {
        this.resumenContextual = resumenContextual;
    }

    public String getNotasPsicologo() {
        return notasPsicologo;
    }

    public void setNotasPsicologo(String notasPsicologo) {
        this.notasPsicologo = notasPsicologo;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
}