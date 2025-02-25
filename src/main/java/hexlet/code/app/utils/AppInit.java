package hexlet.code.app.utils;


import hexlet.code.app.DTO.user.CreateUserDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppInit implements ApplicationRunner {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TaskStatusRepository statusRepository;

    @Autowired
    private final LabelRepository labelRepository;

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final UserMapper mapper;

    public void run(ApplicationArguments args) throws Exception {
        init();
    }

    public void init() {
        dbCleanup();
        setAdmin();
        setTaskStatuses();
    }

    private void setAdmin() {
        CreateUserDTO createData = new CreateUserDTO();
        createData.setEmail("hexlet@example.com");
        createData.setPassword("qwerty");
        createData.setLastName("admin");
        createData.setFirstName("admin");
        User user = mapper.map(createData);
        userRepository.save(user);
    }

    private void setTaskStatuses() {
        var statuses = new String[]
                {"draft", "to_review", "to_be_fixed", "to_publish", "published"};
        for (var status : statuses) {
            var taskStatus = new TaskStatus();
            taskStatus.setName(status);
            taskStatus.setSlug(status);
            statusRepository.save(taskStatus);
        }
    }

    private void dbCleanup() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        labelRepository.deleteAll();
        statusRepository.deleteAll();
    }
}
