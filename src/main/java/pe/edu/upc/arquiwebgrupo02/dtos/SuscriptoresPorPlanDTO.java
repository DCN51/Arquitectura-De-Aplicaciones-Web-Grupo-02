package pe.edu.upc.arquiwebgrupo02.dtos;

public class SuscriptoresPorPlanDTO {

    private String nombrePlan;
    private Long cantidadSuscriptores;

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public Long getCantidadSuscriptores() {
        return cantidadSuscriptores;
    }

    public void setCantidadSuscriptores(Long cantidadSuscriptores) {
        this.cantidadSuscriptores = cantidadSuscriptores;
    }

}
