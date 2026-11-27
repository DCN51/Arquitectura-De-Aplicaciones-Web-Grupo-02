package pe.edu.upc.arquiwebgrupo02.dtos;

// DTO de salida para la Query 2 (convierte el Object[] en campos con nombre)
public class CasosPorPsicologoDTO {
    private String nombrePsicologo;
    private Long cantidadCasos;

    public String getNombrePsicologo() {
        return nombrePsicologo;
    }

    public void setNombrePsicologo(String nombrePsicologo) {
        this.nombrePsicologo = nombrePsicologo;
    }

    public Long getCantidadCasos() {
        return cantidadCasos;
    }

    public void setCantidadCasos(Long cantidadCasos) {
        this.cantidadCasos = cantidadCasos;
    }
}