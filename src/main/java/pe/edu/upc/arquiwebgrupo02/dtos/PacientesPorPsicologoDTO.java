package pe.edu.upc.arquiwebgrupo02.dtos;

public class PacientesPorPsicologoDTO {
    private String nombrePsicologo;
    private Long cantidadPacientes;

    public String getNombrePsicologo() {
        return nombrePsicologo;
    }

    public void setNombrePsicologo(String nombrePsicologo) {
        this.nombrePsicologo = nombrePsicologo;
    }

    public Long getCantidadPacientes() {
        return cantidadPacientes;
    }

    public void setCantidadPacientes(Long cantidadPacientes) {
        this.cantidadPacientes = cantidadPacientes;
    }
}
