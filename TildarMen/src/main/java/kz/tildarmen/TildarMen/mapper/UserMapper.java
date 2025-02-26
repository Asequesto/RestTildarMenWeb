package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDto toUserDto(User user);

    @InheritInverseConfiguration
    User toUser(UserDto userDto);

    List<UserDto> toDtoList(List<User> users);
}
