package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.JobRequestDto;
import kz.tildarmen.TildarMen.enums.RequestStatus;
import kz.tildarmen.TildarMen.mapper.JobRequestMapper;
import kz.tildarmen.TildarMen.model.Employer;
import kz.tildarmen.TildarMen.model.Job;
import kz.tildarmen.TildarMen.model.JobRequest;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.repository.JobRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class JobRequestService {

    private final JobRequestRepository jobRequestRepository;
    private final EmployerService employerService;
    private final TranslatorService translatorService;
    private final JobService jobService;
    private final JobRequestMapper jobRequestMapper;

    public JobRequest findById(Long id) {
        return jobRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JobRequest with id " + id + " not found"));
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

        return jobRequestMapper.toDto(jobRequestRepository.save(jobRequest));

    }


    public void updateRequestStatus(Long jobRequestId, String status){
        JobRequest jobRequest = findById(jobRequestId);
        jobRequest.setStatus(RequestStatus.valueOf(status.toUpperCase()));
        jobRequestRepository.save(jobRequest);
    }

}
