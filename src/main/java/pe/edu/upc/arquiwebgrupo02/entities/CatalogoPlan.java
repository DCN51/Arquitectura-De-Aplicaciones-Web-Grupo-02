package pe.edu.upc.arquiwebgrupo02.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "catalogoplanes")
public class CatalogoPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalogoPlanId")
    private Long catalogoPlanId;

    @Column(name = "nombreCatalogoPlan", length = 100,nullable = false)
    private String nombreCatalogoPlan;

    @Column(name = "precioMensualCatalogoPlan",nullable = false)
    private double precioMensualCatalogoPlan;

    @Column(name = "limiteSesionesMes")
    private int limiteSesionesMes;

    @Column(name = "incluyeDiagnostico",nullable = false)
    private boolean incluyeDiagnostico;

    @Column(name = "incluyeActividades",nullable = false)
    private boolean incluyeActividades;

    @Column(name = "permiteDerivacion",nullable = false)
    private boolean permiteDerivacion;

    @Column(name = "descripcionCatalogoPlan",nullable = false)
    private String descripcionCatalogoPlan;

    @Column(name = "activoCatalogoPlan", nullable = false)
    private boolean activoCatalogoPlan;

    public CatalogoPlan() {
    }

    public CatalogoPlan(Long catalogoPlanId, String nombreCatalogoPlan, double precioMensualCatalogoPlan, int limiteSesionesMes, boolean incluyeDiagnostico, boolean incluyeActividades, boolean permiteDerivacion, String descripcionCatalogoPlan, boolean activoCatalogoPlan) {
        this.catalogoPlanId = catalogoPlanId;
        this.nombreCatalogoPlan = nombreCatalogoPlan;
        this.precioMensualCatalogoPlan = precioMensualCatalogoPlan;
        this.limiteSesionesMes = limiteSesionesMes;
        this.incluyeDiagnostico = incluyeDiagnostico;
        this.incluyeActividades = incluyeActividades;
        this.permiteDerivacion = permiteDerivacion;
        this.descripcionCatalogoPlan = descripcionCatalogoPlan;
        this.activoCatalogoPlan = activoCatalogoPlan;
    }

    public Long getCatalogoPlanId() {
        return catalogoPlanId;
    }

    public void setCatalogoPlanId(Long catalogoPlanId) {
        this.catalogoPlanId = catalogoPlanId;
    }

    public String getNombreCatalogoPlan() {
        return nombreCatalogoPlan;
    }

    public void setNombreCatalogoPlan(String nombreCatalogoPlan) {
        this.nombreCatalogoPlan = nombreCatalogoPlan;
    }

    public double getPrecioMensualCatalogoPlan() {
        return precioMensualCatalogoPlan;
    }

    public void setPrecioMensualCatalogoPlan(double precioMensualCatalogoPlan) {
        this.precioMensualCatalogoPlan = precioMensualCatalogoPlan;
    }

    public int getLimiteSesionesMes() {
        return limiteSesionesMes;
    }

    public void setLimiteSesionesMes(int limiteSesionesMes) {
        this.limiteSesionesMes = limiteSesionesMes;
    }

    public boolean isIncluyeDiagnostico() {
        return incluyeDiagnostico;
    }

    public void setIncluyeDiagnostico(boolean incluyeDiagnostico) {
        this.incluyeDiagnostico = incluyeDiagnostico;
    }

    public boolean isIncluyeActividades() {
        return incluyeActividades;
    }

    public void setIncluyeActividades(boolean incluyeActividades) {
        this.incluyeActividades = incluyeActividades;
    }

    public boolean isPermiteDerivacion() {
        return permiteDerivacion;
    }

    public void setPermiteDerivacion(boolean permiteDerivacion) {
        this.permiteDerivacion = permiteDerivacion;
    }

    public String getDescripcionCatalogoPlan() {
        return descripcionCatalogoPlan;
    }

    public void setDescripcionCatalogoPlan(String descripcionCatalogoPlan) {
        this.descripcionCatalogoPlan = descripcionCatalogoPlan;
    }

    public boolean isActivoCatalogoPlan() {
        return activoCatalogoPlan;
    }

    public void setActivoCatalogoPlan(boolean activoCatalogoPlan) {
        this.activoCatalogoPlan = activoCatalogoPlan;
    }
}
