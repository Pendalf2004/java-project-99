package hexlet.code.app.mapper;

import hexlet.code.app.DTO.user.CreateUserDTO;
import hexlet.code.app.DTO.user.UpdateUserDTO;
import hexlet.code.app.DTO.user.UserDTO;
import hexlet.code.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)


public abstract class UserMapper {

    public abstract UserDTO map(User model);

    public abstract User map(UserDTO model);

    public abstract User map(CreateUserDTO model);

    public abstract void update(@MappingTarget User destination, UpdateUserDTO update);
}
