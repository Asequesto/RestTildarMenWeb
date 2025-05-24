package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.SearchTranslatorDto;
import kz.tildarmen.TildarMen.dto.TranslatorDto;
import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.enums.AvailabilityStatus;
import kz.tildarmen.TildarMen.enums.NotificationType;
import kz.tildarmen.TildarMen.enums.Role;
import kz.tildarmen.TildarMen.mapper.SearchTranslatorMapper;
import kz.tildarmen.TildarMen.mapper.TranslatorMapper;
import kz.tildarmen.TildarMen.mapper.UserMapper;
import kz.tildarmen.TildarMen.model.*;
import kz.tildarmen.TildarMen.repository.TranslatorRepository;
import kz.tildarmen.TildarMen.requests.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private final LocationService locationService;
    private final SearchTranslatorMapper searchTranslatorMapper;
    private final NotificationService notificationService;


    public Translator getTranslatorById(Long id) {
        return translatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Translator not found with ID: " + id));
    }

    public UserDto createTranslator(CreateUserRequest request) {
        User checkUser =  userService.findUserByUserName(request.getEmail(), request.getPhoneNumber());
        if(checkUser == null) {
            Translator translator = new Translator();
            translator.setRole(Role.valueOf(request.getRole().toUpperCase()));
            translator.setEmail(request.getEmail());
            translator.setPhoneNumber(request.getPhoneNumber());
            translator.setFirstName(request.getFirstName());
            translator.setLastName(request.getLastName());
            translator.setPassword(passwordEncoder.encode(request.getPassword()));
            notificationService.sendNotification(translator, "We're excited to have you on board!",
                    "Start building your profile, showcase your skills," +
                            " and connect with clients looking for expert translators like you.",
                    NotificationType.WELCOME);
            return userMapper.toUserDto(translatorRepository.save(translator));
        }
        return null;

    }

    public List<SearchTranslatorDto> filterTranslators(String username, SearchTranslatorsRequest request) {
        List<Long> locations = locationService.getAllByName(request.getLocations())
                .stream().map(Location::getId).toList();
        List<Long> services = serviceTypesService.getAllByName(request.getServiceTypes())
                .stream().map(ServiceTypes::getId).toList();
        List<Long> languages = languageService.getAllByName(request.getLanguages())
                .stream().map(Language::getId).toList();
        List<Long> specializations = specializationService.getAllByName(request.getSpecializations())
                .stream().map(Specialization::getId).toList();
        AvailabilityStatus status =  null;
        int languageSize = languages.size();
        int specializationSize = specializations.size();
        int serviceSize = services.size();
        if(request.getLocations() == null || request.getLocations().isEmpty()) locations = null;
        if(request.getServiceTypes() == null || request.getServiceTypes().isEmpty()){
            services = null;
            serviceSize = 0;
        }
        if(request.getSpecializations() == null || request.getSpecializations().isEmpty()){
            specializations = null;
            specializationSize = 0;
        }
        if(request.getLanguages() == null || request.getLanguages().isEmpty()) {
            languages = null;
            languageSize = 0;
        }
        if(request.getAvailability() != null) status = AvailabilityStatus.valueOf(request.getAvailability().toUpperCase());
        return searchTranslatorMapper.toDtoList(
                translatorRepository.filterTranslators(username, status,
                        languages, languageSize, services,
                        serviceSize, specializations, specializationSize, locations));
    }

    public UserDto updateTranslatorAccountSettings(UpdateUserRequest request, Long id) {
        Translator translator = getTranslatorById(id);
        User checkUser = userService.findUserByPhoneNumber(request.getPhoneNumber());
        if (checkUser != null && !checkUser.getId().equals(translator.getId())) {
            throw new RuntimeException("Phone number is already in use!");
        }
        Location location = locationService.getLocationByName(request.getLocation());
        translator.setPhoneNumber(request.getPhoneNumber());
        translator.setFirstName(request.getFirstName());
        translator.setLastName(request.getLastName());
        translator.setLocation(location);
        return userMapper.toUserDto(translatorRepository.save(translator));
    }

    public TranslatorDto updateTranslatorProfile(Long id, TranslatorProfileRequest request) {
        Translator translator = getTranslatorById(id);
        translator.setProfessionalTitle(request.getTitle());
        Location location = locationService.getLocationByName(request.getBasedIn());
        translator.setLocation(location);
        translator.setAvailability(AvailabilityStatus.valueOf(request.getAvailability().toUpperCase()));
        return translatorMapper.toDto(translatorRepository.save(translator));
    }

    public void deleteTranslator(Long id) {
        Translator translator = getTranslatorById(id);
        translatorRepository.delete(translator);
    }

    public void updatePassword(Long id, UpdatePasswordRequest request) {
        Translator translator = getTranslatorById(id);
        userService.checkPassword(request, translator);
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
        if(translator.getLanguages().contains(language)) {
            throw new RuntimeException("Language already exists");
        }
        translator.getLanguages().add(language);
        return translatorMapper.toDto(translatorRepository.save(translator));
    }

    public TranslatorDto addService(Long id, String service) {
        ServiceTypes serviceTypes = serviceTypesService.getServiceTypesByName(service);
        Translator translator = getTranslatorById(id);
        if(serviceTypes == null) {
            throw new RuntimeException("Service not found");
        }
        if(translator.getServiceTypes().contains(serviceTypes)) {
            throw new RuntimeException("Service already exists");
        }
        translator.getServiceTypes().add(serviceTypes);
        return translatorMapper.toDto(translatorRepository.save(translator));
    }

    public TranslatorDto addSpecialization(Long id, String specialization) {
        Specialization newSpecialization = specializationService.getSpecializationByName(specialization);
        Translator translator = getTranslatorById(id);
        if(newSpecialization == null) {
            throw new RuntimeException("Specialization not found");
        }
        if(translator.getSpecializations().contains(newSpecialization)) {
            throw new RuntimeException("Specialization already exists");
        }
        translator.getSpecializations().add(newSpecialization);
        return translatorMapper.toDto(translatorRepository.save(translator));

    }

    public void deleteLanguage(Long id, String language) {
        Translator translator = getTranslatorById(id);
        Language deleteLanguage = languageService.getLanguageByName(language);
        boolean isThere = translator.getLanguages().remove(deleteLanguage);
        if(!isThere) {
            throw new RuntimeException("Language not found");
        }
        translatorRepository.save(translator);
    }

    public void deleteService(Long id, String service) {
        Translator translator = getTranslatorById(id);
        ServiceTypes serviceTypes = serviceTypesService.getServiceTypesByName(service);
        boolean isThere = translator.getServiceTypes().remove(serviceTypes);
        if(!isThere) {
            throw new RuntimeException("Service not found");
        }
        translatorRepository.save(translator);
    }

    public void deleteSpecialization(Long id, String specialization) {
        Translator translator = getTranslatorById(id);
        Specialization deleteSpecialization = specializationService.getSpecializationByName(specialization);
        boolean isThere = translator.getSpecializations().remove(deleteSpecialization);
        if(!isThere) {
            throw new RuntimeException("Specialization not found");
        }
        translatorRepository.save(translator);
    }
}
