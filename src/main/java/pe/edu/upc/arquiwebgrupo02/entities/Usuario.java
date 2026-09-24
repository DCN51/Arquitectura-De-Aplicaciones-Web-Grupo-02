package pe.edu.upc.arquiwebgrupo02.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity //nombre de la tabla en la base de datos
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "nameUser", length = 50, nullable = false)
    private String nombre;

    @Column(name = "lastNameUser", length = 50, nullable = false)
    private String apellidos;

    @Column(name = "Email", length = 150)
    private String correoElectronico;

    @Column(name = "contrasena", length = 255)
    private String contrasenia;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "fechaNacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "genero", length = 20)
    private String genero;

    @Column(name = "numero_colegiatura", length = 20)
    private String numeroColegiatura;

    @Column(name = "especializacion", length = 50)
    private String especializacion;

    @Column(name = "aniosExperiencia")
    private int aniosExperiencia;

    @Column(name = "estado", length = 20)
    private String estado;

    public Usuario() {
    }

    public Usuario(Long usuarioId, String nombre, String apellidos, String correoElectronico, String contrasenia, String telefono, LocalDate fechaNacimiento, String genero, String numeroColegiatura, String especializacion, int aniosExperiencia, String estado) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.numeroColegiatura = numeroColegiatura;
        this.especializacion = especializacion;
        this.aniosExperiencia = aniosExperiencia;
        this.estado = estado;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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

    public int getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(int aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

