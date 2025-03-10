package hexlet.code.app.services;

import hexlet.code.app.DTO.task.CreateTaskDTO;
import hexlet.code.app.DTO.task.TaskDTO;
import hexlet.code.app.DTO.task.UpdateTaskDTO;
import hexlet.code.app.exception.NotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskUtils {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper mapper;

    @Autowired
    private TaskFilter specification;

    public List<TaskDTO> getAll(Specification<Task> params, PageRequest page) {
        Page<Task> tasks = repository.findAll(params, page);
        return tasks.stream().map(mapper::map).toList();
    }

    public List<TaskDTO> getAll() {
        return getAll(Specification.anyOf(), PageRequest.of(0, 10000));
    }

    public TaskDTO getById(long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id " + id + " not found"));
        return mapper.map(task);
    }

    public TaskDTO add(CreateTaskDTO createData) {
        Task task = mapper.map(createData);
        repository.save(task);
        return mapper.map(task);
    }

    public TaskDTO update(Long id, UpdateTaskDTO updateData) {
        Task task = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Task with id " + id + " not found"));
        mapper.update(updateData, task);
        repository.save(task);
        return mapper.map(task);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

//    public Long getTaskUserId(Long id) {
//        User owner = repository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Task with id " + id + " not found"))
//                .getAssignee();
//        return owner == null ? null : owner.getId();
//    }
}
