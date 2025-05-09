package hexlet.code.controller;

import hexlet.code.DTO.TaskFilterSpecDTO;
import hexlet.code.DTO.task.CreateTaskDTO;
import hexlet.code.DTO.task.TaskDTO;
import hexlet.code.DTO.task.UpdateTaskDTO;
import hexlet.code.services.TaskFilter;
import hexlet.code.services.TasksService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TasksService tasksService;

    @Autowired
    private TaskFilter specification;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAll(TaskFilterSpecDTO taskParamsDTO,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "100") Integer pageSize) {
        var spec = specification.build(taskParamsDTO);
        var pageable = PageRequest.of(page - 1, pageSize);
        var result = tasksService.getAll(spec, pageable);
        return ResponseEntity.ok()
                .header("X-Total-Count",
                        String.valueOf(tasksService.getAll(spec,
                                PageRequest.of(0, Integer.MAX_VALUE)).size()))
                .body(result);
    }

    @GetMapping("/{id}")
    public TaskDTO show(@PathVariable Long id) {
        return tasksService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody CreateTaskDTO createData) {
        return tasksService.add(createData);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@Valid @RequestBody UpdateTaskDTO updateData, @PathVariable Long id) {
        return tasksService.update(id, updateData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tasksService.delete(id);
    }
}
