package hexlet.code.app.mapper;

import hexlet.code.app.DTO.User.UserDTO;
import hexlet.code.app.model.User;
import org.mapstruct.*;

@Mapper(
        uses = {jsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)


public abstract class UserMapper {

    @Mapping(target = "password", ignore = true)
    public abstract UserDTO map(User model);

    @Mapping(target = "email", source = "username")
    public abstract User map(UserDTO model);


}
