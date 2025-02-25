package hexlet.code.app.services;

import hexlet.code.app.DTO.taskStatus.CreateTaskStatusDTO;
import hexlet.code.app.DTO.taskStatus.TaskStatusDTO;
import hexlet.code.app.DTO.taskStatus.UpdateTaskStatusDTO;
import hexlet.code.app.exception.InUseException;
import hexlet.code.app.exception.NotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskStatusUtils {
    @Autowired
    private TaskStatusRepository repository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusMapper mapper;

    public List<TaskStatusDTO> getAll() {
        List<TaskStatus> statuses = repository.findAll();
        return statuses.stream().map(mapper::map).toList();
    }

    public TaskStatusDTO getById(long id) {
        TaskStatus status = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " doesn't exist."));
        return mapper.map(status);
    }

    public TaskStatusDTO add(CreateTaskStatusDTO data) {
        TaskStatus status = mapper.map(data);
        repository.save(status);
        return mapper.map(status);
    }

    public TaskStatusDTO update(Long id, UpdateTaskStatusDTO data) {
        TaskStatus status = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " doesn't exist."));
        mapper.update(data, status);
        repository.save(status);
        return mapper.map(status);
    }

    public void delete(Long id) {
        TaskStatus status = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " doesn't exist."));
        if (!taskRepository.findAllByTaskStatus(status).isEmpty()) {
            throw new InUseException("Status is assigned to some task");
        }
        repository.deleteById(id);
    }

    public TaskStatus getBySlug(String slug) {
        TaskStatus taskStatus = repository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Status " + slug + " does not exist"));
        return taskStatus;
    }
}
