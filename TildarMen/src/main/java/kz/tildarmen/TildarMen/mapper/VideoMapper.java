package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.VideoDto;
import kz.tildarmen.TildarMen.model.Video;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoMapper {

    VideoDto toDto(Video video);

    @InheritInverseConfiguration
    Video toEntity(VideoDto videoDto);

}
