package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.TranslatorsJobsDto;
import kz.tildarmen.TildarMen.model.JobApplication;
import kz.tildarmen.TildarMen.model.JobRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TranslatorsJobsMapper {

    @Mapping(target = "type", constant = "Application")
    @Mapping(source = "id", target = "applicationId")
    TranslatorsJobsDto fromApplication(JobApplication application);


    @Mapping(source = "requestedAt", target = "appliedAt")
    @Mapping(source = "id", target = "applicationId")
    @Mapping(target = "type", constant = "Request")
    TranslatorsJobsDto fromRequest(JobRequest request);

    List<TranslatorsJobsDto> fromApplicationList(List<JobApplication> applications);

    List<TranslatorsJobsDto> fromRequestList(List<JobRequest> requests);


}
