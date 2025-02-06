package hexlet.code.app.utils;


import hexlet.code.app.DTO.User.CreateUserDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.User;
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

//    @Autowired
//    private final TaskStatusRepository statusRepository;
//
//    @Autowired
//    private final LabelRepository labelRepository;
//
//    @Autowired
//    private final TaskRepository taskRepository;

    @Autowired
    private final UserMapper mapper;

//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        dbCleanup();
//        setAdmin();
//    }

    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPassword("qwerty");
        userRepository.save(userData);
    }

    private void setAdmin() {
        CreateUserDTO createData = new CreateUserDTO();
        createData.setEmail("hexlet@example.com");
        createData.setPassword("qwerty");
        User user = mapper.map(createData);
        userRepository.save(user);
    }

//    private void initTaskStatuses(String[] defaults) {
//        for (var str : defaults) {
//            var status = new TaskStatus();
//            status.setName(str);
//            status.setSlug(str);
//            statusRepository.save(status);
//        }
//    }
//
//    private void initLabels(String[] defaults) {
//        for (var l : defaults) {
//            var label = new Label();
//            label.setName(l);
//            labelRepository.save(label);
//        }
//    }

    private void dbCleanup() {
//        taskRepository.deleteAll();
        userRepository.deleteAll();
//        labelRepository.deleteAll();
//        statusRepository.deleteAll();
    }


}
