package kz.tildarmen.TildarMen.services;


import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.enums.Role;
import kz.tildarmen.TildarMen.mapper.UserMapper;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.UserRepository;
import kz.tildarmen.TildarMen.requests.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto getUserById(Long id){

        return userMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserDto createUser(CreateUserRequest request) {
        User checkUser = findUserByUserName(request.getEmail(), request.getPhoneNumber()    );
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

        return userMapper.toUserDto(userRepository.save(user));

    }

    public User findUserByUserName(String email, String phoneNumber){
        return userRepository.findByEmailOrPhoneNumber(email, phoneNumber);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findUserByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    public void disconnectUser(){
        //TODO - Make an Online status for user.
    }
}
