package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.CertificateDto;
import kz.tildarmen.TildarMen.model.Certificate;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    CertificateDto toDto(Certificate certificate);

    @InheritInverseConfiguration
    Certificate toEntity(CertificateDto certificateDto);

    List<CertificateDto> toDtoList(List<Certificate> certificateList);

}
