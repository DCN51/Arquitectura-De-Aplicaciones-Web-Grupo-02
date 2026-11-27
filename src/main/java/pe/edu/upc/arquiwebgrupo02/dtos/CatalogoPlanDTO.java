package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class CatalogoPlanDTO {


    private Long catalogoPlanId;

    @NotBlank(message = "El nombre del plan es obligatorio")
    private String nombreCatalogoPlan;

    // PositiveOrZero porque el plan free cuesta 0
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private double precioMensualCatalogoPlan;

    // -1 = sesiones ilimitadas
    private int limiteSesionesMes;

    private boolean incluyeDiagnostico;

    private boolean incluyeActividades;

    private boolean permiteDerivacion;

    @NotBlank(message = "La descripción del plan es obligatoria")
    private String descripcionCatalogoPlan;

    private boolean activoCatalogoPlan;

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
