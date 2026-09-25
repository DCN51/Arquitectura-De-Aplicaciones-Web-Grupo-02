package pe.edu.upc.arquiwebgrupo02.dtos;

public class RegistroUsuarioResponseDTO {
    private final Long usuarioId;
    private final String username;
    private final String rol;

    public RegistroUsuarioResponseDTO(Long usuarioId, String username, String rol) {
        this.usuarioId = usuarioId;
        this.username = username;
        this.rol = rol;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }
}
