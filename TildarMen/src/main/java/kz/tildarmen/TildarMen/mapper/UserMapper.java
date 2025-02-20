package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);
    User toUserDto(UserDto userDto);
}
