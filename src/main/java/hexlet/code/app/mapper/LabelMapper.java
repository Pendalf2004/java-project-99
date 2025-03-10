package hexlet.code.app.mapper;

import hexlet.code.app.DTO.label.CreateLabelDTO;
import hexlet.code.app.DTO.label.LabelDTO;
import hexlet.code.app.DTO.label.UpdateLabelDTO;
import hexlet.code.app.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = JsonNullableMapper.class,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LabelMapper {
    public abstract LabelDTO map(Label model);

    public abstract Label map(CreateLabelDTO dto);

    public abstract Label map(LabelDTO model);

    public abstract void update(UpdateLabelDTO dto, @MappingTarget Label model);
}
