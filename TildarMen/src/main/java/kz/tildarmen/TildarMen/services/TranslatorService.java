package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.TranslatorDto;
import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.enums.AvailabilityStatus;
import kz.tildarmen.TildarMen.enums.Role;
import kz.tildarmen.TildarMen.mapper.TranslatorMapper;
import kz.tildarmen.TildarMen.mapper.UserMapper;
import kz.tildarmen.TildarMen.model.*;
import kz.tildarmen.TildarMen.repository.TranslatorRepository;
import kz.tildarmen.TildarMen.requests.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class TranslatorService {

    private final TranslatorRepository translatorRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TranslatorMapper translatorMapper;
    private final LanguageService languageService;
    private final ServiceTypesService serviceTypesService;
    private final SpecializationService specializationService;


    public Translator getTranslatorById(Long id) {
        return translatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Translator not found with ID: " + id));
    }

    public UserDto createTranslator(CreateUserRequest request) {
        User checkUser =  userService.findUserByUserName(request.getEmail(), request.getPhoneNumber());
        if(checkUser == null) {
            Translator translator = new Translator();
            translator.setEmail(request.getEmail());
            translator.setPhoneNumber(request.getPhoneNumber());
            translator.setFirstName(request.getFirstName());
            translator.setLastName(request.getLastName());
            translator.setPassword(passwordEncoder.encode(request.getPassword()));
            translator.setRole(Role.valueOf(request.getRole().toUpperCase()));
            return userMapper.toUserDto(translatorRepository.save(translator));
        }
        return null;

    }

    public UserDto updateTranslatorAccountSettings(UpdateUserRequest request, Long id) {
        Translator translator = getTranslatorById(id);
        User checkUser = userService.findUserByPhoneNumber(request.getPhoneNumber());
        if (checkUser != null && !checkUser.getId().equals(translator.getId())) {
            throw new RuntimeException("Phone number is already in use!");
        }
        translator.setPhoneNumber(request.getPhoneNumber());
        translator.setFirstName(request.getFirstName());
        translator.setLastName(request.getLastName());
        translator.setLocation(request.getLocation());
        return userMapper.toUserDto(translatorRepository.save(translator));
    }

    public TranslatorDto updateTranslatorProfile(Long id, TranslatorProfileRequest request) {
        Translator translator = getTranslatorById(id);
        translator.setProfessionalTitle(request.getProfessionalTitle());
        translator.setBasedIn(request.getBasedIn());
        translator.setAvailability(AvailabilityStatus.valueOf(request.getAvailability().toUpperCase()));
        return translatorMapper.toDto(translatorRepository.save(translator));
    }

    public void deleteTranslator(Long id) {
        Translator translator = getTranslatorById(id);
        translatorRepository.delete(translator);
    }

    public void updatePassword(Long id, UpdatePasswordRequest request) {
        Translator translator = getTranslatorById(id);
        if (!passwordEncoder.matches(request.getOldPassword(), translator.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        if(!request.getPassword().equals(request.getRepeatPassword())) {
            throw new IllegalArgumentException("Passwords does not match");
        }
        translator.setPassword(passwordEncoder.encode(request.getPassword()));
        translatorRepository.save(translator);
    }

    public TranslatorDto addIntroduction(Long id, UpdateIntroRequest intro) {
        Translator translator = getTranslatorById(id);
        translator.setIntroduction(intro.getIntro());
        return translatorMapper.toDto(translatorRepository.save(translator));
    }

    public TranslatorDto addLanguage(Long id, String languageName) {
        Language language = languageService.getLanguageByName(languageName);
        Translator translator = getTranslatorById(id);
        if(language == null) {
            throw new RuntimeException("Language not found");
        }
        translator.getLanguages().add(language);
        return translatorMapper.toDto(translatorRepository.save(translator));
    }

    public TranslatorDto addService(Long id, String service) {
        ServiceTypes serviceTypes = serviceTypesService.getServiceTypesByName(service);
        Translator translator = getTranslatorById(id);
        if(serviceTypes == null) {
            throw new RuntimeException("Language not found");
        }
        translator.getServiceTypes().add(serviceTypes);
        return translatorMapper.toDto(translatorRepository.save(translator));
    }

    public TranslatorDto addSpecialization(Long id, String specialization) {
        Specialization newSpecialization = specializationService.getSpecializationByName(specialization);
        Translator translator = getTranslatorById(id);
        if(newSpecialization == null) {
            throw new RuntimeException("Language not found");
        }
        translator.getSpecializations().add(newSpecialization);
        return translatorMapper.toDto(translatorRepository.save(translator));

    }
}
