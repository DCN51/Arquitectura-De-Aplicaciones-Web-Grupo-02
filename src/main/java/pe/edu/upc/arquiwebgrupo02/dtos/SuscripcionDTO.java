package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SuscripcionDTO {

    private Long suscripcionId;

    // Fechas y estado NO se validan: los pone el servicio, el cliente no los envia
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String estadoSuscripcion;

    // Pueden ir vacios si el plan es gratis
    private String metodoPagoSuscripcion;

    private String referenciaPagoSuscripcion;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El id del plan es obligatorio")
    private Long catalogoPlanId;

    public Long getSuscripcionId() {
        return suscripcionId;
    }

    public void setSuscripcionId(Long suscripcionId) {
        this.suscripcionId = suscripcionId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstadoSuscripcion() {
        return estadoSuscripcion;
    }

    public void setEstadoSuscripcion(String estadoSuscripcion) {
        this.estadoSuscripcion = estadoSuscripcion;
    }

    public String getMetodoPagoSuscripcion() {
        return metodoPagoSuscripcion;
    }

    public void setMetodoPagoSuscripcion(String metodoPagoSuscripcion) {
        this.metodoPagoSuscripcion = metodoPagoSuscripcion;
    }

    public String getReferenciaPagoSuscripcion() {
        return referenciaPagoSuscripcion;
    }

    public void setReferenciaPagoSuscripcion(String referenciaPagoSuscripcion) {
        this.referenciaPagoSuscripcion = referenciaPagoSuscripcion;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getCatalogoPlanId() {
        return catalogoPlanId;
    }

    public void setCatalogoPlanId(Long catalogoPlanId) {
        this.catalogoPlanId = catalogoPlanId;
    }


}
