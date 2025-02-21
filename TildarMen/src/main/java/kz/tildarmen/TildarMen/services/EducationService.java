package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.model.Education;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.repository.EducationRepository;
import kz.tildarmen.TildarMen.repository.TranslatorRepository;
import kz.tildarmen.TildarMen.requests.CreateEducationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final TranslatorRepository translatorRepository;

    public Education addEducation(Long translatorId, CreateEducationRequest request) {
        Translator translator = translatorRepository.findById(translatorId)
                .orElseThrow(() -> new RuntimeException("Translator not found with ID: " + translatorId));
        Education education = new Education();
        education.setTranslator(translator);
        education.setDegree(request.getDegree());
        education.setUniversity(request.getUniversity());
        education.setGraduationYear(request.getGraduationYear());
        return educationRepository.save(education);
    }

}
