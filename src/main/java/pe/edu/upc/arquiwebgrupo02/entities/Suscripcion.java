package pe.edu.upc.arquiwebgrupo02.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "suscripciones" )
public class Suscripcion {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "suscripcionId")
    private int suscripcionId;

    @Column(name = "fechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechaFin", nullable = true)
    private LocalDate fechaFin;

    @Column(name = "estadoSuscripcion",length = 200 ,nullable = false)
    private String estadoSuscripcion;

    @Column(name = "metodoPagoSuscripcion",length = 200)
    private String metodoPagoSuscripcion;

    @Column(name = "referenciaPagoSuscripcion",length = 200)
    private String referenciaPagoSuscripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogoPlanId", nullable = false)
    private CatalogoPlan catalogoPlan;

    public Suscripcion() {
    }

    public Suscripcion(int suscripcionId, LocalDate fechaInicio, LocalDate fechaFin, String estadoSuscripcion, String metodoPagoSuscripcion, String referenciaPagoSuscripcion, Usuario usuario, CatalogoPlan catalogoPlan) {
        this.suscripcionId = suscripcionId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estadoSuscripcion = estadoSuscripcion;
        this.metodoPagoSuscripcion = metodoPagoSuscripcion;
        this.referenciaPagoSuscripcion = referenciaPagoSuscripcion;
        this.usuario = usuario;
        this.catalogoPlan = catalogoPlan;
    }

    public int getSuscripcionId() {
        return suscripcionId;
    }

    public void setSuscripcionId(int suscripcionId) {
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CatalogoPlan getCatalogoPlan() {
        return catalogoPlan;
    }

    public void setCatalogoPlan(CatalogoPlan catalogoPlan) {
        this.catalogoPlan = catalogoPlan;
    }
}
