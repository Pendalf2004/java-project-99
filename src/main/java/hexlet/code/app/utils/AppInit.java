package hexlet.code.app.utils;


import hexlet.code.app.DTO.user.CreateUserDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.services.UserUtils;
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
    private final UserUtils userUtils;

    @Autowired
    private final LabelRepository labelRepository;

    @Autowired
    private final TaskRepository taskRepository;

    public void run(ApplicationArguments args) throws Exception {
        init();
    }

    public void init() {
        dbCleanup();
        setAdmin();
        setTaskStatuses();
        setLabels();
    }

    private void setAdmin() {
        CreateUserDTO createData = new CreateUserDTO();
        createData.setEmail("hexlet@example.com");
        createData.setPassword("qwerty");
        createData.setLastName("admin");
        createData.setFirstName("admin");
        userUtils.add(createData);
    }

    private void setLabels() {
        var labels = new String[]
                {"bug", "feature"};
        for (var label : labels) {
            var setLabel = new Label();
            setLabel.setName(label);
            labelRepository.save(setLabel);
        }
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
