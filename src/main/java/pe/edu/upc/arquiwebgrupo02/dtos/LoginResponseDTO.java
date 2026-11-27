package pe.edu.upc.arquiwebgrupo02.dtos;

public class LoginResponseDTO {
    private String token;

    private Long userId;

    private String username;

    private String rol;

    private String nombres;

    private String apellidos;

    public LoginResponseDTO(String token, Long userId, String username, String rol, String nombres, String apellidos) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.rol = rol;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }
}
