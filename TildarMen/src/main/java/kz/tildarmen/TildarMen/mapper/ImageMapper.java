package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.ImageDto;
import kz.tildarmen.TildarMen.model.Image;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDto toDto(Image profileImage);

    @InheritInverseConfiguration
    Image toEntity(ImageDto profileImageDto);

}
