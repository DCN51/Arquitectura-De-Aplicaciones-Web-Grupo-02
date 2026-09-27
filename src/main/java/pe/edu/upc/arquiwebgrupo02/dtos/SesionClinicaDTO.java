package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SesionClinicaDTO {

    private Long sesionClinicaId;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;

    // Fechas y estado NO se validan: los pone el servicio, el cliente no los envia
    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    // Estos los llena la IA al finalizar la sesion
    private String emocionDetectada;

    private String nivelUrgencia;

    private String resumenIa;

    private Boolean requiereDerivacion;

    private String estado;

    public Long getSesionClinicaId() {
        return sesionClinicaId;
    }

    public void setSesionClinicaId(Long sesionClinicaId) {
        this.sesionClinicaId = sesionClinicaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public String getEmocionDetectada() {
        return emocionDetectada;
    }

    public void setEmocionDetectada(String emocionDetectada) {
        this.emocionDetectada = emocionDetectada;
    }

    public String getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(String nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    public String getResumenIa() {
        return resumenIa;
    }

    public void setResumenIa(String resumenIa) {
        this.resumenIa = resumenIa;
    }

    public Boolean getRequiereDerivacion() {
        return requiereDerivacion;
    }

    public void setRequiereDerivacion(Boolean requiereDerivacion) {
        this.requiereDerivacion = requiereDerivacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}