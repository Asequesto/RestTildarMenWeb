package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.enums.RequestStatus;
import kz.tildarmen.TildarMen.mapper.JobApplicationMapper;
import kz.tildarmen.TildarMen.model.Job;
import kz.tildarmen.TildarMen.model.JobApplication;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobService jobService;
    private final TranslatorService translatorService;
    private final JobApplicationMapper jobApplicationMapper;
    private final EmailSenderService emailSenderService;
    private final UserService userService;

    public JobApplication findById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job application not found"));
    }

    public List<JobApplicationDto> getEmployerApplications(Long id) {
        Job job = jobService.getJobById(id);
        return jobApplicationMapper.toDtoList(job.getApplications());
    }

    public List<JobApplicationDto> getTranslatorApplications(Long id){
        Translator translator = translatorService.getTranslatorById(id);
        return jobApplicationMapper.toDtoList(translator.getApplications());
    }

    public JobApplicationDto sendApplication(Long translatorId, Long jobId) {
        Translator translator = translatorService.getTranslatorById(translatorId);
        Job job = jobService.getJobById(jobId);

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setTranslator(translator);
        application.setAppliedAt(LocalDateTime.now());
        job.increaseApplicantsCount();
        String fullName = translator.getFirstName() + " " + translator.getLastName();

        String email = job.getEmployer().getEmail();
        String subject = "Translator " + fullName + " is sending you a job application";
        String message = fullName + " is sending you a job application for " + job.getTitle();

        emailSenderService.sendEmail(email, subject, message);

        return jobApplicationMapper.toDto(jobApplicationRepository.save(application));
    }

    public void updateApplicationStatus(Long jobApplicationId, String status) {
        JobApplication application = findById(jobApplicationId);
        User user = userService.getAuthenticatedUser();
        Job job = application.getJob();

        String email = application.getTranslator().getEmail();
        String subject = "Job Application " + jobApplicationId + " got responded";
        String message = "Your job application for - " + job.getTitle() + " has been "+ status +  " by " +
                user.getFirstName() + " " + user.getLastName();

        emailSenderService.sendEmail(email, subject, message);
        application.setStatus(RequestStatus.valueOf(status.toUpperCase()));
    }

}
