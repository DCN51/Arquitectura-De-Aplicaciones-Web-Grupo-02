package pe.edu.upc.arquiwebgrupo02.entities;

import jakarta.persistence.*;
import org.apache.catalina.User;

import java.time.LocalDate;

@Entity
@Table(name = "actividadRecomendada")
public class ActividadRecomendada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NatividadId")
    private int actividadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId", nullable = false)
    private Users user;

    @Column(name = "diagnosticoId", nullable = false)
    private int diagnosticoId;

    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;

    @Column(name = "tipo", length = 50, nullable = false)
    private String tipo;

    @Column(name = "fechaAsignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @Column(name = "fechaCompletada")
    private LocalDate fechaCompletada;

    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    @Column(name = "feedbackUser", length = 255)
    private String feedbackUsuario;

    public ActividadRecomendada() {
    }

    public ActividadRecomendada(int actividadId, Users user, int diagnosticoId, String titulo, String descripcion,
                                String tipo, LocalDate fechaAsignacion, LocalDate fechaCompletada, String estado,
                                String feedbackUsuario) {
        this.actividadId = actividadId;
        this.user = user;
        this.diagnosticoId = diagnosticoId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaCompletada = fechaCompletada;
        this.estado = estado;
        this.feedbackUsuario = feedbackUsuario;
    }

    public int getActividadId() {
        return actividadId;
    }

    public void setActividadId(int actividadId) {
        this.actividadId = actividadId;
    }

    public Users getUsuario() {
        return user;
    }

    public void setUsuario(Users user) {
        this.user = user;
    }

    public int getDiagnosticoId() {
        return diagnosticoId;
    }

    public void setDiagnosticoId(int diagnosticoId) {
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
