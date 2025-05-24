package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.Job8Dto;
import kz.tildarmen.TildarMen.dto.JobDto;
import kz.tildarmen.TildarMen.dto.JobTranslatorsDto;
import kz.tildarmen.TildarMen.enums.NotificationType;
import kz.tildarmen.TildarMen.mapper.*;
import kz.tildarmen.TildarMen.model.*;
import kz.tildarmen.TildarMen.repository.JobRepository;
import kz.tildarmen.TildarMen.requests.SearchJobsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final EmployerService employerService;
    private final JobMapper jobMapper;
    private final LanguageService languageService;
    private final LocationService locationService;
    private final ServiceTypesService serviceTypesService;
    private final SpecializationService specializationService;
    private final LanguageMapper languageMapper;
    private final ServiceTypesMapper serviceTypesMapper;
    private final SpecializationMapper specializationMapper;
    private final JobTranslatorsMapper jobTranslatorsMapper;
    private final NotificationService notificationService;

    public List<JobDto> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobMapper.toDtoList(jobs);
    }

    public List<JobDto> getJobsByEmployerId(Long employerId) {
        List<Job> jobs = jobRepository.findAllByEmployerIdOrderByPublicationDateDesc(employerId);
        employerService.getEmployerById(employerId);
        return jobMapper.toDtoList(jobs);
    }

    public List<Job8Dto> getFirstEightJobs() {
        List<Job> jobs = jobRepository.findTop8ByOrderByPublicationDateDesc();
        return jobMapper.toDto8List(jobs);
    }

    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
    }
    public List<JobTranslatorsDto> getJobTranslators(Long id) {
        Job job = getJobById(id);
        List<JobTranslatorsDto> requests = jobTranslatorsMapper.fromRequestList(job.getJobRequests());
        List<JobTranslatorsDto> applications = jobTranslatorsMapper.fromApplicationList(job.getApplications());
        applications.addAll(requests);
        applications.sort(Comparator.comparing(JobTranslatorsDto::getSendAt).reversed());
        return applications;
    }

    public List<JobDto> filterJobs(SearchJobsRequest request, String title) {
        List<Long> services = serviceTypesService.getAllByName(request.getServiceTypes())
                .stream().map(ServiceTypes::getId).toList();
        List<Long> locations = locationService.getAllByName(request.getLocations())
                .stream().map(Location::getId).toList();
        List<Long> languages = languageService.getAllByName(request.getLanguages())
                .stream().map(Language::getId).toList();
        List<Long> specializations = specializationService.getAllByName(request.getSpecializations())
                .stream().map(Specialization::getId).toList();
        int languageSize = languages.size();
        int specializationSize = specializations.size();
        int serviceSize = services.size();
        if(request.getSpecializations() == null || request.getSpecializations().isEmpty()){
            specializations = null;
            specializationSize = 0;
        }
        if(request.getLocations() == null || request.getLocations().isEmpty()) locations = null;
        if(request.getServiceTypes() == null || request.getServiceTypes().isEmpty()){
            services = null;
            serviceSize = 0;
        }
        if(request.getLanguages() == null || request.getLanguages().isEmpty()) {
            languages = null;
            languageSize = 0;
        }
        return jobMapper.toDtoList(jobRepository
                .filterJobs(languages, languageSize, services, serviceSize, specializations, specializationSize,
                        locations,
                        request.getStartDate(), request.getEndDate(), title));
    }

    public JobDto addJob(Long employerId, JobDto jobDto) {
        Employer employer = employerService.getEmployerById(employerId);
        Job job = new Job();
        return setJob(jobDto, employer, job);
    }

    public JobDto setJob(JobDto jobDto, Employer employer, Job job) {
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setStartDate(jobDto.getStartDate());
        job.setEndDate(jobDto.getEndDate());
        job.setPrice(jobDto.getPrice());

        job.setLanguages(addLanguages(languageMapper.toEntitySet(jobDto.getLanguages())));
        job.setLocation(locationService.getLocationByName(jobDto.getLocation()));
        job.setServiceTypes(addServiceTypes(serviceTypesMapper.toEntitySet(jobDto.getServiceTypes())));
        job.setSpecializations(addSpecializations(specializationMapper.toEntitySet(jobDto.getSpecializations())));
        job.setEmployer(employer);
        notificationService.sendNotification(employer, "Project has been posted!", "Your project " +
                job.getTitle() + " has been posted. You can check it on the projects catalog",
                NotificationType.JOB_POSTED);
        return jobMapper.toDto(jobRepository.save(job));
    }

    public Set<Language> addLanguages(Set<Language> languages) {
        Set<Language> newLanguages = new HashSet<>();
        for (Language language : languages) {
            language = languageService.getLanguageByName(language.getName());
            newLanguages.add(language);
        }
        return newLanguages;
    }

    public Set<ServiceTypes> addServiceTypes(Set<ServiceTypes> services) {
        Set<ServiceTypes> newServices = new HashSet<>();
        for (ServiceTypes service : services) {
            service = serviceTypesService.getServiceTypesByName(service.getName());
            newServices.add(service);
        }
        return newServices;
    }

    public Set<Specialization> addSpecializations(Set<Specialization> specializations) {
        Set<Specialization> newSpecializations = new HashSet<>();
        for (Specialization specialization : specializations) {
            specialization = specializationService.getSpecializationByName(specialization.getName());
            newSpecializations.add(specialization);
        }
        return newSpecializations;
    }

    public JobDto updateJobById(Long employerId, Long jobId, JobDto job) {
        Employer employer = employerService.getEmployerById(employerId);
        Job newJob = getJobById(jobId);
        return setJob(job, employer, newJob);
    }

    public void deleteJobByEmployerId(Long employerId, Long jobId) {
        employerService.getEmployerById(employerId);
        Job job = getJobById(jobId);
        jobRepository.delete(job);
    }

}
