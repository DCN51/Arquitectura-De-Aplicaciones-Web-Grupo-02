package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoleDTO {
    private Long roleId;

    @NotBlank(message = "Role name is required")
    @Size(max = 30, message = "Role name cannot exceed 30 characters")
    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
