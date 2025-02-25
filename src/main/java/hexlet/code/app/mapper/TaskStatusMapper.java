package hexlet.code.app.mapper;

import hexlet.code.app.DTO.taskStatus.CreateTaskStatusDTO;
import hexlet.code.app.DTO.taskStatus.TaskStatusDTO;
import hexlet.code.app.DTO.taskStatus.UpdateTaskStatusDTO;
import hexlet.code.app.model.TaskStatus;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {
    public abstract TaskStatus map(CreateTaskStatusDTO dto);

    public abstract TaskStatusDTO map(TaskStatus model);

    public abstract void update(UpdateTaskStatusDTO dto, @MappingTarget TaskStatus model);
}
