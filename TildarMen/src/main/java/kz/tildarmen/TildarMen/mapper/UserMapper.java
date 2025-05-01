package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDto toUserDto(User user);

    List<UserDto> toDtoList(List<User> users);
}
