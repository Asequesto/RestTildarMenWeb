package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.CertificateDto;
import kz.tildarmen.TildarMen.model.Certificate;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    @Mapping(target = "title", source = "title")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "certificateFileUrl", source = "certificateFileUrl")
    CertificateDto toDto(Certificate certificate);

    @InheritInverseConfiguration
    Certificate toEntity(CertificateDto certificateDto);

    List<CertificateDto> toDtoList(List<Certificate> certificateList);
    List<Certificate> toEntityList(List<CertificateDto> certificateDtoList);

}
