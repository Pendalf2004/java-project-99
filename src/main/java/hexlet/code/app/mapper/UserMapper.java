package hexlet.code.app.mapper;

import hexlet.code.app.DTO.User.CreateUserDTO;
import hexlet.code.app.DTO.User.UpdateUserDTO;
import hexlet.code.app.DTO.User.UserDTO;
import hexlet.code.app.model.User;
import org.mapstruct.*;

@Mapper(
        uses = {jsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)


public abstract class UserMapper {

    public abstract UserDTO map(User model);

    public abstract User map(UserDTO model);

    public abstract User map(CreateUserDTO model);

    public abstract void update(@MappingTarget User destination, UpdateUserDTO update);


}
