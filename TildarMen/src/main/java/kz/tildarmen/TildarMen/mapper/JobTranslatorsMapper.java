package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.JobTranslatorsDto;
import kz.tildarmen.TildarMen.model.JobApplication;
import kz.tildarmen.TildarMen.model.JobRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobTranslatorsMapper {

    @Mapping(source = "translator.firstName", target = "firstName")
    @Mapping(source = "translator.lastName", target = "lastName")
    @Mapping(source = "translator.id", target = "translatorId")
    @Mapping(source = "translator.rating", target = "rating")
    @Mapping(source = "translator.profileImageUrl", target = "profileImageUrl")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "id", target = "applicationId")
    @Mapping(source = "appliedAt", target = "sendAt")
    @Mapping(source = "job.price", target = "price")
    @Mapping(target = "type", constant = "Application")
    JobTranslatorsDto fromApplication(JobApplication jobApplication);

    @Mapping(source = "translator.firstName", target = "firstName")
    @Mapping(source = "translator.lastName", target = "lastName")
    @Mapping(source = "translator.id", target = "translatorId")
    @Mapping(source = "translator.rating", target = "rating")
    @Mapping(source = "id", target = "applicationId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "requestedAt", target = "sendAt")
    @Mapping(source = "job.price", target = "price")
    @Mapping(target = "type", constant = "Request")
    JobTranslatorsDto fromRequest(JobRequest jobRequest);

    List<JobTranslatorsDto> fromApplicationList(List<JobApplication> jobApplicationList);
    List<JobTranslatorsDto> fromRequestList(List<JobRequest> jobRequestList);

}
