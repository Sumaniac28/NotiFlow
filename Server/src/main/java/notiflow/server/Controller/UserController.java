package notiflow.server.Controller;

import jakarta.validation.Valid;
import notiflow.server.Requests.UserLogInRequest;
import notiflow.server.Requests.UserSignUpRequest;
import notiflow.server.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private final UserServices userServices;

    @Autowired
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserSignUpRequest userRequest) {
        if (!userRequest.isPasswordMatch()) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        try {
            userServices.registerUser(userRequest);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLogInRequest loginRequest) {
        try {
            userServices.loginUser(loginRequest);
            return ResponseEntity.ok("User logged in successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}
