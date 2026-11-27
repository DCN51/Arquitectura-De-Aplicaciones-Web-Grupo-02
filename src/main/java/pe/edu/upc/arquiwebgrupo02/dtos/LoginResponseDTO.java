package pe.edu.upc.arquiwebgrupo02.dtos;

public class LoginResponseDTO {
    private String token;

    private String username;

    private String rol;

    private Long userId;

    public LoginResponseDTO(String token, String username, String rol, Long userId) {
        this.token = token;
        this.username = username;
        this.rol = rol;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }

    public Long getUserId() {
        return userId;
    }
}
