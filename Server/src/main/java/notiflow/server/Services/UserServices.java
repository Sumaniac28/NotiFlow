package notiflow.server.Services;

import notiflow.server.Entities.UserEntity;
import notiflow.server.Repository.UserRepository;
import notiflow.server.Requests.UserLogInRequest;
import notiflow.server.Requests.UserSignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private final UserRepository userRepository;

    @Autowired
    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserSignUpRequest userRequest) {
        if (isUserExists(userRequest.getEmail(), userRequest.getPhone())) {
            throw new IllegalArgumentException("User with given email or phone already exists");
        }

        UserEntity user = new UserEntity(userRequest.getName(), userRequest.getEmail(), userRequest.getPhone(), userRequest.getPassword());

        userRepository.save(user);
    }

    public void loginUser(UserLogInRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            user = userRepository.findByPhone(loginRequest.getPhone());
        }

        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Invalid email/phone or password");
        }
    }

    public boolean isUserExists(String email, String phone) {
        return userRepository.existsByEmail(email) || userRepository.existsByPhone(phone);
    }

    public UserEntity getUser(String fromEmail) {
        UserEntity user = userRepository.findByEmail(fromEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }
}
