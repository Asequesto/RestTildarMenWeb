package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.JobDto;
import kz.tildarmen.TildarMen.mapper.JobMapper;
import kz.tildarmen.TildarMen.model.*;
import kz.tildarmen.TildarMen.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


    public List<JobDto> getJobsByEmployerId(Long employerId) {
        List<Job> jobs = jobRepository.findAllByEmployerId(employerId);
        employerService.getEmployerById(employerId);
        return jobMapper.toDtoList(jobs);
    }

    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
    }

    public JobDto addJob(Long employerId, JobDto jobDto) {
        Employer employer = employerService.getEmployerById(employerId);


        Job job = new Job();
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setStartDate(jobDto.getStartDate());
        job.setEndDate(jobDto.getEndDate());
        job.setLanguages(addLanguages(jobDto.getLanguages()));
        job.setLocations(addLocations(jobDto.getLocations()));
        job.setServiceTypes(addServiceTypes(jobDto.getServiceTypes()));
        job.setSpecializations(addSpecializations(jobDto.getSpecializations()));
        job.setEmployer(employer);
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

    public Set<Location> addLocations(Set<Location> locations) {
        Set<Location> newLocations = new HashSet<>();
        for (Location location : locations) {
            location = locationService.getLocationByName(location.getCity());
            newLocations.add(location);
        }
        return newLocations;
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
        newJob.setTitle(job.getTitle());
        newJob.setDescription(job.getDescription());
        newJob.setStartDate(job.getStartDate());
        newJob.setEndDate(job.getEndDate());
        newJob.setLanguages(addLanguages(job.getLanguages()));
        newJob.setLocations(addLocations(job.getLocations()));
        newJob.setServiceTypes(addServiceTypes(job.getServiceTypes()));
        newJob.setSpecializations(addSpecializations(job.getSpecializations()));
        newJob.setEmployer(employer);
        return jobMapper.toDto(jobRepository.save(newJob));
    }

    public void deleteJobByEmployerId(Long employerId, Long jobId) {
        employerService.getEmployerById(employerId);
        Job job = getJobById(jobId);
        jobRepository.delete(job);
    }

}
