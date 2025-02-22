package kz.tildarmen.TildarMen.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.WorkExperienceDto;
import kz.tildarmen.TildarMen.mapper.WorkExperienceMapper;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.model.WorkExperience;
import kz.tildarmen.TildarMen.repository.WorkExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final WorkExperienceMapper workExperienceMapper;
    private final TranslatorService translatorService;

    public List<WorkExperienceDto> findAllByTranslatorId(Long translatorId) {
        return workExperienceMapper.toDtoList(workExperienceRepository.findAllByTranslatorId(translatorId));
    }

    public WorkExperienceDto addWorkExperience(WorkExperienceDto workExperienceDto, Long translatorId) {
        WorkExperience workExperience = workExperienceMapper.toEntity(workExperienceDto);
        Translator translator = translatorService.getTranslatorById(translatorId);
        workExperience.setTranslator(translator);
        return workExperienceMapper.toDto(workExperienceRepository.save(workExperience));
    }

    public WorkExperienceDto updateWorkExperience(WorkExperienceDto workDto, Long translatorId, Long workId){
        WorkExperience work = workExperienceRepository.findById(workId)
                .orElseThrow(() -> new EntityNotFoundException("Work experience not found"));
        if(!work.getTranslator().getId().equals(translatorId)) {
            throw new IllegalArgumentException("Work experience does not belong to translator");
        }
        work.setCompanyName(workDto.getCompanyName());
        work.setDescription(workDto.getDescription());
        work.setPosition(workDto.getPosition());
        work.setStartDate(workDto.getStartDate());
        work.setEndDate(workDto.getEndDate());
        return workExperienceMapper.toDto(workExperienceRepository.save(work));
    }

    public void deleteWorkExperience(Long translatorId, Long workExperienceId) {
        WorkExperience work = workExperienceRepository.findById(workExperienceId)
                .orElseThrow(() -> new EntityNotFoundException("Work experience not found"));
        if(!work.getTranslator().getId().equals(translatorId)) {
            throw new IllegalArgumentException("Work experience does not belong to translator");
        }
        workExperienceRepository.deleteById(workExperienceId);
    }

}
