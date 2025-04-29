package hexlet.code.mapper;

import hexlet.code.DTO.user.CreateUserDTO;
import hexlet.code.DTO.user.UpdateUserDTO;
import hexlet.code.DTO.user.UserDTO;
import hexlet.code.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)


public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;


    public abstract UserDTO map(User model);

    public abstract User map(UserDTO model);

    public abstract User map(CreateUserDTO model);

    public abstract void update(@MappingTarget User destination, UpdateUserDTO update);

    @BeforeMapping
    public void hashPassword(CreateUserDTO createData) {
        var rawPassword = createData.getPassword();
        var hashedPass = passwordEncoder.encode(rawPassword);
        createData.setPassword(hashedPass);
    }

    @BeforeMapping
    private void hashPassword(UpdateUserDTO updateData) {
        var password = updateData.getPassword();
        try {
            password = passwordEncoder.encode(updateData.getPassword());
        } catch (NullPointerException | IllegalArgumentException e) {

        } finally {
            updateData.setPassword(password);
        }
    }

}
