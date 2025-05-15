package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.ReportDto;
import kz.tildarmen.TildarMen.enums.ReportReason;
import kz.tildarmen.TildarMen.enums.ReportType;
import kz.tildarmen.TildarMen.mapper.ReportMapper;
import kz.tildarmen.TildarMen.model.Job;
import kz.tildarmen.TildarMen.model.Report;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final TranslatorService translatorService;
    private final UserService userService;
    private final ReportMapper reportMapper;
    private final ImageService imageService;
    private final JobService jobService;

    public ReportDto reportTranslator(Long translatorId, Long userId, ReportDto reportDto,
                                      MultipartFile file) throws IOException {
        if(userId.equals(translatorId)){
            throw new SecurityException("You are not allowed to report yourself");
        }
        Translator translator = translatorService.getTranslatorById(translatorId);
        Report report = createReport(userId, reportDto, file);
        report.setReportedTranslator(translator);
        reportRepository.save(report);
        return reportMapper.toDto(report);
    }

    public ReportDto reportJob(Long jobId, Long userId, ReportDto reportDto,
                               MultipartFile file) throws IOException{
        Job job = jobService.getJobById(jobId);
        if(userId.equals(job.getEmployer().getId())){
            throw new SecurityException("You are not allowed to report your job");
        }
        createReport(userId, reportDto, file);
        Report report = createReport(userId, reportDto, file);
        report.setReportedJob(job);
        reportRepository.save(report);
        return reportMapper.toDto(report);
    }

    public Report createReport(Long userId, ReportDto reportDto, MultipartFile file) throws IOException {
        User user = userService.getUserById(userId);

        Report report = new Report();
        report.setType(ReportType.valueOf(reportDto.getType().toUpperCase()));
        report.setReason(ReportReason.valueOf(reportDto.getReason().toUpperCase()));
        report.setTitle(reportDto.getTitle());
        report.setDetails(reportDto.getDetails());
        if(file == null || file.isEmpty()) report.setFileUrl(null);
        else report.setFileUrl(imageService.uploadFile(file));
        report.setCreatedAt(LocalDateTime.now());
        report.setReporter(user);
        return report;
    }

}
