package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.ReportDto;
import kz.tildarmen.TildarMen.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "reportedTranslator.firstName", target = "translatorFirstName")
    @Mapping(source = "reportedTranslator.lastName", target = "translatorLastName")
    @Mapping(source = "reportedJob.title", target = "jobTitle")
    ReportDto toDto(Report report);

}
