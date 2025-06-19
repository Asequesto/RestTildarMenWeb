package kz.tildarmen.TildarMen.services;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.JobRequestDto;
import kz.tildarmen.TildarMen.enums.NotificationType;
import kz.tildarmen.TildarMen.enums.RequestStatus;
import kz.tildarmen.TildarMen.mapper.JobRequestMapper;
import kz.tildarmen.TildarMen.model.*;
import kz.tildarmen.TildarMen.repository.JobRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class JobRequestService {

    private final JobRequestRepository jobRequestRepository;
    private final EmployerService employerService;
    private final TranslatorService translatorService;
    private final JobService jobService;
    private final JobRequestMapper jobRequestMapper;
    private final EmailSenderService emailSenderService;
    private final UserService userService;
    private final AuthService authService;
    private final NotificationService notificationService;

    public JobRequest findById(Long id) {
        return jobRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JobRequest with id " + id + " not found"));
    }

    public List<JobRequestDto> getEmployerRequests(Long id) {
        Employer employer = employerService.getEmployerById(id);
        return jobRequestMapper.toDtoList(employer.getJobRequests());
    }

        public List<JobRequestDto> getTranslatorRequests(Long id){
            Translator translator = translatorService.getTranslatorById(id);
            return jobRequestMapper.toDtoList(translator.getJobRequests());
        }


    public JobRequestDto sendRequest(Long employerId, Long translatorId, Long jobId) throws MessagingException {

        Employer employer = employerService.getEmployerById(employerId);
        Translator translator = translatorService.getTranslatorById(translatorId);
        Job job = jobService.getJobById(jobId);
        if(jobRequestRepository.existsByTranslatorIdAndJobId(translatorId, jobId)){
            throw new IllegalArgumentException("You already have a request for this translator");
        }
        JobRequest jobRequest = new JobRequest();
        jobRequest.setEmployer(employer);
        jobRequest.setTranslator(translator);
        jobRequest.setJob(job);
        jobRequest.setRequestedAt(LocalDateTime.now());

        String fullName = employer.getFirstName() + " " + employer.getLastName();

        String email = translator.getEmail();
        String subject = "Employer " + fullName + " wants to hire you";
        String message = """
            <html>
                <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);">
                        <h2 style="color: #2c3e50;">New Job Request Received</h2>
                        <p style="font-size: 16px; color: #333;">
                            <strong>%s</strong> is sending you a job request for the position:
                        </p>
                        <p style="font-size: 18px; color: #2c3e50; font-weight: bold; margin-top: -10px;">%s</p>
                        <p style="font-size: 14px; color: #666;">
                            Please review the request and respond via your TildarMen dashboard.
                        </p>
                        <br/>
                        <a href="https://tildarmen-front.vercel.app/translator/applied-projects" style="display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;">View Job Request</a>
                        <hr style="margin-top: 30px; border: none; border-top: 1px solid #eee;">
                        <p style="font-size: 12px; color: #999; text-align: center;">
                            TildarMen — Bridging Clients and Professional Translators
                        </p>
                    </div>
                </body>
            </html>
    """.formatted(fullName, job.getTitle());

        emailSenderService.sendEmail(email, subject, message);
        notificationService.sendNotification(translator,"New Project Request Received","Employer "
                + employer.getFirstName() + " " + employer.getLastName() + " wants to hire you." +
                " You can respond to him in your profile and chat with him", employer.getProfileImageUrl(),
                NotificationType.REQUEST_SEND);

        return jobRequestMapper.toDto(jobRequestRepository.save(jobRequest));

    }


    public void updateRequestStatus(Long jobRequestId, String status, User userDetails) throws MessagingException {
        JobRequest jobRequest = findById(jobRequestId);
        jobRequest.setStatus(RequestStatus.valueOf(status.toUpperCase()));
        User user = userService.getAuthenticatedUser();
        Job job = jobRequest.getJob();
        authService.checkPermission(userDetails, jobRequest.getTranslator().getId());

        String email = jobRequest.getEmployer().getEmail();
        String subject = "Translator " + user.getFirstName() + " " + user.getLastName() +
                " responded to your job request for the " + job.getTitle() + " project";
        String message = """
            <html>
                <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">
                        <h2 style="color: #2c3e50;">Job Request Update</h2>
                        <p style="font-size: 16px; color: #333;">
                            Your job request for <strong>%s</strong> has been <strong>%s</strong> by
                            <strong>%s %s</strong>.
                        </p>
                        <p style="font-size: 14px; color: #666;">
                            Log in to your dashboard to see more details or take action.
                        </p>
                        <a href="https://tildarmen-front.vercel.app/employer/applied-project/%s"
                           style="display: inline-block; padding: 10px 20px; background-color: #4CAF50;
                                  color: white; text-decoration: none; border-radius: 5px; margin-top: 15px;">
                            View Job Status
                        </a>
                        <hr style="margin-top: 30px; border: none; border-top: 1px solid #ddd;">
                        <p style="font-size: 12px; color: #999; text-align: center;">
                            TildarMen • Trusted Platform for Quality Translation Services
                        </p>
                    </div>
                </body>
            </html>
            """.formatted(job.getTitle(), status, user.getFirstName(), user.getLastName(), job.getId());

        emailSenderService.sendEmail(email, subject, message);
        notificationService.sendNotification(jobRequest.getEmployer()
                ,"You project request has been " + status,"Translator "
                        + user.getFirstName() + " " + user.getLastName() + " has " + status +
                        " your project request!", user.getProfileImageUrl(), NotificationType.REQUEST_RESPONDED);
        jobRequestRepository.save(jobRequest);
    }

}
