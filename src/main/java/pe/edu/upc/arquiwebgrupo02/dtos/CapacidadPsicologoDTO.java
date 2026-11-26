package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CapacidadPsicologoDTO {
    @NotNull(message = "La capacidad máxima es obligatoria")
    @Min(value = 1, message = "La capacidad máxima debe ser al menos 1 paciente")
    @Max(value = 500, message = "La capacidad máxima no puede superar los 500 pacientes")
    private Integer maxPacientes;

    public Integer getMaxPacientes() {
        return maxPacientes;
    }

    public void setMaxPacientes(Integer maxPacientes) {
        this.maxPacientes = maxPacientes;
    }
}
