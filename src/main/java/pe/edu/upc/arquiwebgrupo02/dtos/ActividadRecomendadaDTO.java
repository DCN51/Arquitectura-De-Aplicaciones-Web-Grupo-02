package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ActividadRecomendadaDTO {
    private Integer actividadId;

    @NotNull(message = "El usuario es obligatorio")
    @Positive(message = "El identificador del usuario debe ser positivo")
    private Long usuarioId;

    @NotNull(message = "El diagnóstico es obligatorio")
    @Positive(message = "El identificador del diagnóstico debe ser positivo")
    private Integer diagnosticoId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 30, message = "El tipo no puede superar los 30 caracteres")
    private String tipo;

    private LocalDate fechaAsignacion;
    private LocalDate fechaCompletada;

    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    private String estado;

    private String feedbackUsuario;

    public Integer getActividadId() {
        return actividadId;
    }

    public void setActividadId(Integer actividadId) {
        this.actividadId = actividadId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getDiagnosticoId() {
        return diagnosticoId;
    }

    public void setDiagnosticoId(Integer diagnosticoId) {
        this.diagnosticoId = diagnosticoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDate getFechaCompletada() {
        return fechaCompletada;
    }

    public void setFechaCompletada(LocalDate fechaCompletada) {
        this.fechaCompletada = fechaCompletada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFeedbackUsuario() {
        return feedbackUsuario;
    }

    public void setFeedbackUsuario(String feedbackUsuario) {
        this.feedbackUsuario = feedbackUsuario;
    }
}
