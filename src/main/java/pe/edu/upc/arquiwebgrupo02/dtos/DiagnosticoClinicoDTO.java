package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class DiagnosticoClinicoDTO {

    private Long diagnosticoClinicoId;

    private Long usuarioId;

    @NotNull(message = "El id de la sesión es obligatorio")
    private Long sesionClinicaId;

    private LocalDateTime fechaDiagnostico;

    @NotBlank(message = "El resultado principal es obligatorio")
    @Size(max = 100, message = "El resultado principal no puede tener más de 100 caracteres")
    private String resultadoPrincipal;

    private String descripcionDetallada;

    @NotBlank(message = "El nivel de riesgo es obligatorio")
    private String nivelRiesgo;

    private String recomendacionGeneral;

    private Long validacionPsicologoId;

    private LocalDateTime fechaValidacion;

    public Long getDiagnosticoClinicoId() {
        return diagnosticoClinicoId;
    }

    public void setDiagnosticoClinicoId(Long diagnosticoClinicoId) {
        this.diagnosticoClinicoId = diagnosticoClinicoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getSesionClinicaId() {
        return sesionClinicaId;
    }

    public void setSesionClinicaId(Long sesionClinicaId) {
        this.sesionClinicaId = sesionClinicaId;
    }

    public LocalDateTime getFechaDiagnostico() {
        return fechaDiagnostico;
    }

    public void setFechaDiagnostico(LocalDateTime fechaDiagnostico) {
        this.fechaDiagnostico = fechaDiagnostico;
    }

    public String getResultadoPrincipal() {
        return resultadoPrincipal;
    }

    public void setResultadoPrincipal(String resultadoPrincipal) {
        this.resultadoPrincipal = resultadoPrincipal;
    }

    public String getDescripcionDetallada() {
        return descripcionDetallada;
    }

    public void setDescripcionDetallada(String descripcionDetallada) {
        this.descripcionDetallada = descripcionDetallada;
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(String nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public String getRecomendacionGeneral() {
        return recomendacionGeneral;
    }

    public void setRecomendacionGeneral(String recomendacionGeneral) {
        this.recomendacionGeneral = recomendacionGeneral;
    }

    public Long getValidacionPsicologoId() {
        return validacionPsicologoId;
    }

    public void setValidacionPsicologoId(Long validacionPsicologoId) {
        this.validacionPsicologoId = validacionPsicologoId;
    }

    public LocalDateTime getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(LocalDateTime fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }
}