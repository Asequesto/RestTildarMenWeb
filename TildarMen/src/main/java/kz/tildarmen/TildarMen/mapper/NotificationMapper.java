package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.NotificationDto;
import kz.tildarmen.TildarMen.model.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto toDto (Notification notification);

    List<NotificationDto> toDtoList (List<Notification> notifications);

}
