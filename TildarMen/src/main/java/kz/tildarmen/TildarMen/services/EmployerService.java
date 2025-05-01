package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.enums.Role;
import kz.tildarmen.TildarMen.mapper.UserMapper;
import kz.tildarmen.TildarMen.model.*;
import kz.tildarmen.TildarMen.repository.EmployerRepository;
import kz.tildarmen.TildarMen.repository.UserRepository;
import kz.tildarmen.TildarMen.requests.CreateUserRequest;
import kz.tildarmen.TildarMen.requests.GetEmployerProfile;
import kz.tildarmen.TildarMen.requests.UpdatePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final LocationService locationService;
    private final ReviewService reviewService;

    public Employer getEmployerById(Long id) {
        return employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found with id: " + id));
    }

    public List<UserDto> searchEmployersByName(String username) {
        List<User> users = employerRepository.searchEmployerByUsername(username.toLowerCase());
        return userMapper.toDtoList(users);
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

    public void updateProfile(GetEmployerProfile profile, Long id) {
        Employer employer = getEmployerById(id);
        if(profile.getIntroduction() != null) employer.setIntroduction(profile.getIntroduction());
        if(profile.getLocation() != null) {
            Location location = locationService.getLocationByName(profile.getLocation());
            employer.setLocation(location);
        }
        if(profile.getFirstName() != null) employer.setFirstName(profile.getFirstName());
        if(profile.getLastName() != null) employer.setLastName(profile.getLastName());
    }

    public void updatePassword(Long id, UpdatePasswordRequest request) {
        Employer employer = getEmployerById(id);
        userService.checkPassword(request, employer);
        employer.setPassword(passwordEncoder.encode(request.getPassword()));
        employerRepository.save(employer);
    }

    public void deleteEmployerById(Long id) {
        Employer employer = getEmployerById(id);

        List<Review> reviews = employer.getReviewList();

        for(Review review : reviews) {
            reviewService.deleteTranslatorReview(review.getId());
        }

        userRepository.delete(employer);
    }
}
