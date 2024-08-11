package notiflow.server.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLogInRequest {

    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    @NotBlank(message = "Password is mandatory")
    private String password;

    public UserLogInRequest() {
    }

    public UserLogInRequest(String email, String phone, String password) {
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
