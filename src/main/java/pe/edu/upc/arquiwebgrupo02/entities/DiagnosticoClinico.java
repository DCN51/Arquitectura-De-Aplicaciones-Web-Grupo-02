package pe.edu.upc.arquiwebgrupo02.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "diagnosticos_clinicos")
public class DiagnosticoClinico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosticoClinicoId")
    private Long diagnosticoClinicoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId", nullable = false)
    private Users usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesionClinicaId", nullable = false)
    private SesionClinica sesionClinica;

    @Column(name = "fechaDiagnostico", nullable = false)
    private LocalDateTime fechaDiagnostico;

    @Column(name = "resultadoPrincipal", length = 100, nullable = false)
    private String resultadoPrincipal;

    @Column(name = "descripcionDetallada", columnDefinition = "TEXT")
    private String descripcionDetallada;

    @Column(name = "nivelRiesgo", length = 20, nullable = false)
    private String nivelRiesgo;

    @Column(name = "recomendacionGeneral", columnDefinition = "TEXT")
    private String recomendacionGeneral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validacionPsicologoId")
    private Users validacionPsicologo;

    @Column(name = "fechaValidacion")
    private LocalDateTime fechaValidacion;

    public DiagnosticoClinico() {
    }

    @PrePersist
    private void alCrear() {
        if (fechaDiagnostico == null) {
            fechaDiagnostico = LocalDateTime.now();
        }
    }

    public Long getDiagnosticoClinicoId() {
        return diagnosticoClinicoId;
    }

    public void setDiagnosticoClinicoId(Long diagnosticoClinicoId) {
        this.diagnosticoClinicoId = diagnosticoClinicoId;
    }

    public Users getUsuario() {
        return usuario;
    }

    public void setUsuario(Users usuario) {
        this.usuario = usuario;
    }

    public SesionClinica getSesionClinica() {
        return sesionClinica;
    }

    public void setSesionClinica(SesionClinica sesionClinica) {
        this.sesionClinica = sesionClinica;
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

    public Users getValidacionPsicologo() {
        return validacionPsicologo;
    }

    public void setValidacionPsicologo(Users validacionPsicologo) {
        this.validacionPsicologo = validacionPsicologo;
    }

    public LocalDateTime getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(LocalDateTime fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }
}