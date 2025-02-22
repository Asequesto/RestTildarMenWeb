package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "role", target = "role")
    UserDto toUserDto(User user);

    @InheritInverseConfiguration
    User toUser(UserDto userDto);
}
