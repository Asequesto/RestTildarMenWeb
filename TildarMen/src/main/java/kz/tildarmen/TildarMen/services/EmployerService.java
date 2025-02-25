package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.enums.Role;
import kz.tildarmen.TildarMen.mapper.UserMapper;
import kz.tildarmen.TildarMen.model.Employer;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.EmployerRepository;
import kz.tildarmen.TildarMen.requests.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public Employer getEmployerById(Long id) {
        return employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found with id: " + id));
    }

    public UserDto createEmployer(CreateUserRequest request) {
        User checkUser =  userService.findUserByUserName(request.getEmail(), request.getPhoneNumber());
        if(checkUser == null) {
            Employer employer = new Employer();
            employer.setEmail(request.getEmail());
            employer.setPhoneNumber(request.getPhoneNumber());
            employer.setFirstName(request.getFirstName());
            employer.setLastName(request.getLastName());
            employer.setPassword(passwordEncoder.encode(request.getPassword()));
            employer.setRole(Role.valueOf(request.getRole().toUpperCase()));
            return userMapper.toUserDto(employerRepository.save(employer));
        }
        return null;
    }

    public void deleteEmployerById(Long id) {
        Employer employer = getEmployerById(id);
        employerRepository.delete(employer);
    }

}
