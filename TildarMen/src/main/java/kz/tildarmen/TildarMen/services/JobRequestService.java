package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.JobRequestDto;
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


    public JobRequestDto sendRequest(Long employerId, Long translatorId, Long jobId){

        Employer employer = employerService.getEmployerById(employerId);
        Translator translator = translatorService.getTranslatorById(translatorId);
        Job job = jobService.getJobById(jobId);

        JobRequest jobRequest = new JobRequest();
        jobRequest.setEmployer(employer);
        jobRequest.setTranslator(translator);
        jobRequest.setJob(job);
        jobRequest.setRequestedAt(LocalDateTime.now());

        String fullName = employer.getFirstName() + " " + employer.getLastName();

        String email = translator.getEmail();
        String subject = "Employer " + fullName + " wants to hire you";
        String message = fullName + " is sending you a job request for " + job.getTitle();

        emailSenderService.sendEmail(email, subject, message);

        return jobRequestMapper.toDto(jobRequestRepository.save(jobRequest));

    }


    public void updateRequestStatus(Long jobRequestId, String status){
        JobRequest jobRequest = findById(jobRequestId);
        jobRequest.setStatus(RequestStatus.valueOf(status.toUpperCase()));
        User user = userService.getAuthenticatedUser();
        Job job = jobRequest.getJob();

        String email = jobRequest.getEmployer().getEmail();
        String subject = "Job Request " + jobRequestId +  " Responded";
        String message = "Your job request for - " + job.getTitle() + " has been "+ status +  " by " +
                user.getFirstName() + " " + user.getLastName();

        emailSenderService.sendEmail(email, subject, message);
        jobRequestRepository.save(jobRequest);
    }

}
