package pe.edu.upc.arquiwebgrupo02.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "casoEmergencias")
public class CasoEmergencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "casoEmergenciaId")
    private Long casoEmergenciaId;

    // Paciente en riesgo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId", nullable = false)
    private Users usuario;

    // Psicologo asignado (puede ser null mientras el caso esta pendiente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psicologoId")
    private Users psicologo;

    // Sesion donde la IA detecto la emergencia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesionClinicaId", nullable = false)
    private SesionClinica sesionClinica;

    @Column(name = "fechaEmergencia", nullable = false)
    private LocalDateTime fechaEmergencia;

    @Column(name = "nivelUrgencia", length = 20, nullable = false)
    private String nivelUrgencia;

    // pendiente, en_atencion, cerrado
    @Column(name = "estado", length = 20, nullable = false)
    private String estado = "pendiente";

    @Column(name = "resumenContextual", columnDefinition = "TEXT")
    private String resumenContextual;

    @Column(name = "notasPsicologo", columnDefinition = "TEXT")
    private String notasPsicologo;

    @Column(name = "fechaCierre")
    private LocalDateTime fechaCierre;

    public CasoEmergencia() {
    }

    public CasoEmergencia(Long casoEmergenciaId, Users usuario, Users psicologo, SesionClinica sesionClinica, LocalDateTime fechaEmergencia, String nivelUrgencia, String estado, String resumenContextual, String notasPsicologo, LocalDateTime fechaCierre) {
        this.casoEmergenciaId = casoEmergenciaId;
        this.usuario = usuario;
        this.psicologo = psicologo;
        this.sesionClinica = sesionClinica;
        this.fechaEmergencia = fechaEmergencia;
        this.nivelUrgencia = nivelUrgencia;
        this.estado = estado;
        this.resumenContextual = resumenContextual;
        this.notasPsicologo = notasPsicologo;
        this.fechaCierre = fechaCierre;
    }

    public Long getCasoEmergenciaId() {
        return casoEmergenciaId;
    }

    public void setCasoEmergenciaId(Long casoEmergenciaId) {
        this.casoEmergenciaId = casoEmergenciaId;
    }

    public Users getUsuario() {
        return usuario;
    }

    public void setUsuario(Users usuario) {
        this.usuario = usuario;
    }

    public Users getPsicologo() {
        return psicologo;
    }

    public void setPsicologo(Users psicologo) {
        this.psicologo = psicologo;
    }

    public SesionClinica getSesionClinica() {
        return sesionClinica;
    }

    public void setSesionClinica(SesionClinica sesionClinica) {
        this.sesionClinica = sesionClinica;
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
