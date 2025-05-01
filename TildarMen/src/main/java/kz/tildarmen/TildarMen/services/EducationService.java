package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.EducationDto;
import kz.tildarmen.TildarMen.mapper.EducationMapper;
import kz.tildarmen.TildarMen.model.Education;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.repository.EducationRepository;
import kz.tildarmen.TildarMen.requests.UploadEducationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Transactional
@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final TranslatorService translatorService;
    private final EducationMapper educationMapper;
    private final ImageService imageService;

    public EducationDto addEducation(Long translatorId, MultipartFile file, UploadEducationRequest request) throws IOException {
        Translator translator = translatorService.getTranslatorById(translatorId);
        Education education = new Education();
        education.setTranslator(translator);
        return getEducationDto(translatorId, file, request, education);
    }

    public EducationDto getEducationDto(Long translatorId, MultipartFile file,
                                        UploadEducationRequest request, Education education) throws IOException {
        education.setDegree(request.getDegree());
        education.setUniversity(request.getUniversity());
        education.setGraduationYear(request.getGraduationYear());
        String url = imageService.uploadFile(translatorId, file, null);
        education.setDegreeUrl(url);
        return educationMapper.toDto(educationRepository.save(education));
    }

    public EducationDto updateEducation(Long translatorId, Long educationId,
                                        MultipartFile file, UploadEducationRequest request) throws IOException {

        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new IllegalArgumentException("Education not found"));
        if(!education.getTranslator().getId().equals(translatorId)) {
            throw new IllegalArgumentException("Education does not belong to translator");
        }
        imageService.deleteImage(education.getDegreeUrl());
        return getEducationDto(translatorId, file, request, education);
    }

    public void deleteEducation(Long translatorId, Long educationId) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new IllegalArgumentException("Education not found"));
        if(!education.getTranslator().getId().equals(translatorId)) {
            throw new IllegalArgumentException("Education does not belong to translator");
        }
        imageService.deleteImage(education.getDegreeUrl());
        educationRepository.deleteById(educationId);

    }

}
