package pe.edu.upc.arquiwebgrupo02.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones_clinicas")
public class SesionClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sesionClinicaId")
    private Long sesionClinicaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId", nullable = false)
    private Users usuario;

    @Column(name = "fechaHoraInicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fechaHoraFin")
    private LocalDateTime fechaHoraFin;

    @Column(name = "emocionDetectada", length = 50)
    private String emocionDetectada;

    @Column(name = "nivelUrgencia", length = 20, nullable = false)
    private String nivelUrgencia = "bajo";

    @Column(name = "resumenIa", columnDefinition = "TEXT")
    private String resumenIa;

    @Column(name = "requiereDerivacion", nullable = false)
    private Boolean requiereDerivacion = false;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado = "en_curso";

    public SesionClinica() {
    }

    // Si no mandan fecha de inicio, se pone la actual (CURRENT_TIMESTAMP del diagrama)
    @PrePersist
    private void alCrear() {
        if (fechaHoraInicio == null) {
            fechaHoraInicio = LocalDateTime.now();
        }
    }

    public Long getSesionClinicaId() {
        return sesionClinicaId;
    }

    public void setSesionClinicaId(Long sesionClinicaId) {
        this.sesionClinicaId = sesionClinicaId;
    }

    public Users getUsuario() {
        return usuario;
    }

    public void setUsuario(Users usuario) {
        this.usuario = usuario;
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