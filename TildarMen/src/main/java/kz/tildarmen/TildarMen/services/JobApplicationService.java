package kz.tildarmen.TildarMen.services;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.dto.TranslatorsJobsDto;
import kz.tildarmen.TildarMen.enums.RequestStatus;
import kz.tildarmen.TildarMen.mapper.JobApplicationMapper;
import kz.tildarmen.TildarMen.mapper.TranslatorsJobsMapper;
import kz.tildarmen.TildarMen.model.Job;
import kz.tildarmen.TildarMen.model.JobApplication;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
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
    private final AuthService authService;
    private final TranslatorsJobsMapper translatorsJobsMapper;

    public JobApplication findById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job application not found"));
    }

    public List<JobApplicationDto> getEmployerApplications(Long id) {
        Job job = jobService.getJobById(id);
        return jobApplicationMapper.toDtoList(job.getApplications());
    }

    public List<TranslatorsJobsDto> getTranslatorApplications(Long id){
        Translator translator = translatorService.getTranslatorById(id);

        List<TranslatorsJobsDto> applicationDto = translatorsJobsMapper.fromApplicationList(translator.getApplications());
        List<TranslatorsJobsDto> requestDto = translatorsJobsMapper.fromRequestList(translator.getJobRequests());
        applicationDto.addAll(requestDto);
        applicationDto.sort(Comparator.comparing(TranslatorsJobsDto::getAppliedAt).reversed());
        return applicationDto;
    }

    public JobApplicationDto sendApplication(Long translatorId, Long jobId) throws MessagingException {
        Translator translator = translatorService.getTranslatorById(translatorId);
        Job job = jobService.getJobById(jobId);
        if(jobApplicationRepository.existsByTranslatorIdAndJobId(translatorId, jobId)){
            throw new IllegalArgumentException("You already sent job application");
        }
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setTranslator(translator);
        application.setAppliedAt(LocalDateTime.now());
        job.increaseApplicantsCount();
        String fullName = translator.getFirstName() + " " + translator.getLastName();

        String email = job.getEmployer().getEmail();
        String subject = "Translator " + fullName + " is sending you a job application";
        String message = """
           <html>
                <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);">
                        <h2 style="color: #2c3e50;">New Job Application Received</h2>
                        <p style="font-size: 16px; color: #333;">
                            <strong>%s</strong> has applied to your job posting</strong>
                        </p>
                        <p style="font-size: 18px; color: #2c3e50; font-weight: bold; margin-top: -10px;">%s</p>
                        <p style="font-size: 14px; color: #666;">
                            You can review their translator profile and resume by logging into your account</p>
                        <br/>
                        <a href="https://tildarmen.com/dashboard" style="display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;">View Job Request</a>
                        <hr style="margin-top: 30px; border: none; border-top: 1px solid #eee;">
                        <p style="font-size: 12px; color: #999; text-align: center;">
                            TildarMen — Bridging Clients and Professional Translators
                        </p>
                    </div>
                </body>
            </html>
    """.formatted(fullName, job.getTitle());

        emailSenderService.sendEmail(email, subject, message);

        return jobApplicationMapper.toDto(jobApplicationRepository.save(application));
    }

    public void updateApplicationStatus(Long jobApplicationId, String status, User userDetails) throws MessagingException {
        JobApplication application = findById(jobApplicationId);
        User user = userService.getAuthenticatedUser();
        Job job = application.getJob();
        authService.checkPermission(userDetails, job.getEmployer().getId());
        String email = application.getTranslator().getEmail();
        String subject = "Job Application " + jobApplicationId + " got responded";
        String message = """
            <html>
                <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                        <h2 style="color: #2c3e50;">Job Application Update</h2>
                        <p style="font-size: 16px; color: #333;">
                            Your job application for <strong>%s</strong> has been <strong>%s</strong> by <strong>%s %s</strong>.
                        </p>
                        <p style="font-size: 14px; color: #666;">
                            Please check your dashboard for more details.
                        </p>
                        <hr style="border: none; border-top: 1px solid #eee;" />
                        <p style="font-size: 12px; color: #aaa; text-align: center;">
                            TildarMen • Professional Translation Platform
                        </p>
                    </div>
                </body>
            </html>
            """.formatted(job.getTitle(), status, user.getFirstName(), user.getLastName());

        emailSenderService.sendEmail(email, subject, message);
        application.setStatus(RequestStatus.valueOf(status.toUpperCase()));
    }

}
