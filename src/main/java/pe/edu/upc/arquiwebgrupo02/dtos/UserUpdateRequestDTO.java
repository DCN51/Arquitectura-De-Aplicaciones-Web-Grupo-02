package pe.edu.upc.arquiwebgrupo02.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class UserUpdateRequestDTO {
    @Size(min = 1, max = 100)
    private String firstName;
    @Size(min = 1, max = 100)
    private String lastName;
    @Email
    @Size(max = 150)
    private String email;
    @Size(min = 8, max = 100)
    private String password;
    @Size(max = 20)
    private String phone;
    private LocalDate birthDate;
    @Size(max = 20)
    private String gender;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
