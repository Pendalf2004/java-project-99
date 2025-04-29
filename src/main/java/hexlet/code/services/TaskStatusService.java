package hexlet.code.services;

import hexlet.code.DTO.taskStatus.CreateTaskStatusDTO;
import hexlet.code.DTO.taskStatus.TaskStatusDTO;
import hexlet.code.DTO.taskStatus.UpdateTaskStatusDTO;
import hexlet.code.exception.InUseException;
import hexlet.code.exception.NotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {
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
