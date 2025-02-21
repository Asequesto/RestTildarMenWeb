package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.enums.Role;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.TranslatorRepository;
import kz.tildarmen.TildarMen.requests.CreateUserRequest;
import kz.tildarmen.TildarMen.requests.UpdateUserRequest;
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


    public Translator getTranslatorById(Long id) {
        return translatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Translator not found with ID: " + id));
    }

    public User createTranslator(CreateUserRequest request) {
        User checkUser =  userService.findUserByUserName(request.getEmail(), request.getPhoneNumber());
        if(checkUser == null) {
            Translator translator = new Translator();
            translator.setEmail(request.getEmail());
            translator.setPhoneNumber(request.getPhoneNumber());
            translator.setFirstName(request.getFirstName());
            translator.setLastName(request.getLastName());
            translator.setPassword(passwordEncoder.encode(request.getPassword()));
            translator.setRole(Role.valueOf(request.getRole().toUpperCase()));
            return translatorRepository.save(translator);
        }
        return null;

    }

    public Translator updateTranslator(UpdateUserRequest request, Long id) {
        Translator translator = getTranslatorById(id);
        User checkUser = userService.findUserByPhoneNumber(request.getPhoneNumber());
        if (checkUser != null && !checkUser.getId().equals(translator.getId())) {
            throw new RuntimeException("Phone number is already in use!");
        }
        translator.setPhoneNumber(request.getPhoneNumber());
        translator.setFirstName(request.getFirstName());
        translator.setLastName(request.getLastName());
        translator.setLocation(request.getLocation());
        return translatorRepository.save(translator);
    }

    public void deleteTranslator(Long id) {
        Translator translator = getTranslatorById(id);
        translatorRepository.delete(translator);

        translatorRepository.delete(translator);
    }
}
