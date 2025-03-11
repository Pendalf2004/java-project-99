package hexlet.code.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
//import hexlet.code.app.label.LabelRepository;
//import hexlet.code.app.label.mapper.SpecifiedLabelMapper;
import hexlet.code.DTO.task.CreateTaskDTO;
import hexlet.code.DTO.task.TaskDTO;
import hexlet.code.DTO.task.UpdateTaskDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "slugToTaskStatus")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "idToLabel")
    public abstract Task map(CreateTaskDTO dto);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "labelToId")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus.slug", source = "status")
    @Mapping(target = "assignee.id", source = "assigneeId")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "idToLabel")
    public abstract Task map(TaskDTO model);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "slugToTaskStatus")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "idToLabel")
    public abstract void update(UpdateTaskDTO dto, @MappingTarget Task model);

    @Named("slugToTaskStatus")
    public TaskStatus slugToTaskStatus(String slug) {

        return taskStatusRepository.findBySlug(slug).orElseThrow();
    }


    @Named("labelToId")
    public Set<Long> labelToId(Set<Label> labels) {
        return labels == null ? null
                : labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }

    @Named("idToLabel")
    public Set<Label> idToLabel(Set<Long> labelsIds) {
        return labelsIds == null ? null
                : labelsIds.stream()
                .map(Label::new)
                .collect(Collectors.toSet());
    }
}
