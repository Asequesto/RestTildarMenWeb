package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.enums.RequestStatus;
import kz.tildarmen.TildarMen.mapper.JobApplicationMapper;
import kz.tildarmen.TildarMen.model.Job;
import kz.tildarmen.TildarMen.model.JobApplication;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobService jobService;
    private final TranslatorService translatorService;
    private final JobApplicationMapper jobApplicationMapper;

    public JobApplication findById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job application not found"));
    }

    public Set<JobApplicationDto> getEmployerApplications(Long id) {
        Job job = jobService.getJobById(id);
        return jobApplicationMapper.toDtoSet(job.getApplications());
    }

    public Set<JobApplicationDto> getTranslatorApplications(Long id){
        Translator translator = translatorService.getTranslatorById(id);
        return jobApplicationMapper.toDtoSet(translator.getApplications());
    }

    public JobApplicationDto sendApplication(Long translatorId, Long jobId) {
        Translator translator = translatorService.getTranslatorById(translatorId);
        Job job = jobService.getJobById(jobId);
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setTranslator(translator);
        application.setAppliedAt(LocalDateTime.now());
        return jobApplicationMapper.toDto(jobApplicationRepository.save(application));
    }

    public void updateApplicationStatus(Long jobApplicationId, String status) {
        JobApplication application = findById(jobApplicationId);
        application.setStatus(RequestStatus.valueOf(status.toUpperCase()));
    }

}
