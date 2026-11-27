package pe.edu.upc.arquiwebgrupo02.dtos;

public class PsicologoDTO {
    private Long psicologoId;
    private String username;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private String numeroColegiatura;
    private String especializacion;
    private Integer anosExperiencia;
    private String estadoVerificacion;
    private String estadoCuenta;
    private Integer maxPacientes;
    private Integer pacientesAsignados;

    public Long getPsicologoId() {
        return psicologoId;
    }

    public void setPsicologoId(Long psicologoId) {
        this.psicologoId = psicologoId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNumeroColegiatura() {
        return numeroColegiatura;
    }

    public void setNumeroColegiatura(String numeroColegiatura) {
        this.numeroColegiatura = numeroColegiatura;
    }

    public String getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(String especializacion) {
        this.especializacion = especializacion;
    }

    public Integer getAnosExperiencia() {
        return anosExperiencia;
    }

    public void setAnosExperiencia(Integer anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }

    public String getEstadoVerificacion() {
        return estadoVerificacion;
    }

    public void setEstadoVerificacion(String estadoVerificacion) {
        this.estadoVerificacion = estadoVerificacion;
    }

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(String estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public Integer getMaxPacientes() {
        return maxPacientes;
    }

    public void setMaxPacientes(Integer maxPacientes) {
        this.maxPacientes = maxPacientes;
    }

    public Integer getPacientesAsignados() {
        return pacientesAsignados;
    }

    public void setPacientesAsignados(Integer pacientesAsignados) {
        this.pacientesAsignados = pacientesAsignados;
    }
}
