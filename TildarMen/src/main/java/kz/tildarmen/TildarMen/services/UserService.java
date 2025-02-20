package kz.tildarmen.TildarMen.services;


import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.enums.Role;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.UserRepository;
import kz.tildarmen.TildarMen.requests.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(CreateUserRequest request) {
        User checkUser = userRepository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber());
        if(checkUser != null){
            return null;
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

        return userRepository.save(user);

    }

}
