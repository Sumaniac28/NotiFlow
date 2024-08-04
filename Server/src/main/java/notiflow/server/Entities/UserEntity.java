package notiflow.server.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;


@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Email
    private String email;

    @Size(min = 10, message = "Phone number should have at least 10 characters")
    private int phone;

    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<EmailEntity> emails;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @Size(min = 8, message = "Password should have at least 8 characters") String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 8, message = "Password should have at least 8 characters") String password) {
        this.password = password;
    }

    @Size(min = 10, message = "Phone number should have at least 10 characters")
    public int getPhone() {
        return phone;
    }

    public void setPhone(@Size(min = 10, message = "Phone number should have at least 10 characters") int phone) {
        this.phone = phone;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserEntity() {
    }

    public UserEntity(String name, String email, int phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

}
