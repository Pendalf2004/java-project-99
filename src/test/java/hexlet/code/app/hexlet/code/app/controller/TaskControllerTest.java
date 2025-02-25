package hexlet.code.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.DTO.label.CreateLabelDTO;
import hexlet.code.app.DTO.task.CreateTaskDTO;
import hexlet.code.app.DTO.user.CreateUserDTO;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.services.LabelUtils;
import hexlet.code.app.services.TaskStatusUtils;
import hexlet.code.app.services.TaskUtils;
import hexlet.code.app.services.UserUtils;
import hexlet.code.app.utils.AppInit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppInit init;

    @Autowired
    UserUtils userUtils;

    @Autowired
    private TaskUtils taskUtils;

    @Autowired
    private TaskStatusUtils taskStatusUtils;

    @Autowired
    private LabelUtils labelUtils;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LabelMapper labelMapper;

    private User testUser;

    private User user;

    private Task testTask;

    private Label testLabel;

//    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

//    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor wrongToken;

    @BeforeEach
    void setup() {
        init.init();

        var createUser = new CreateUserDTO();
        createUser.setEmail("email@email.com");
        createUser.setPassword("password");
        createUser.setLastName("Ivanov");
        createUser.setFirstName("Ivan");
        userUtils.add(createUser);
        testUser = userMapper.map(userUtils.getByEmail(createUser.getEmail()));

//        token = jwt().jwt(b -> b.subject(user.getEmail()));
//        wrongToken = jwt().jwt(b -> b.subject(wrong.getEmail()));
        var createTask = new CreateTaskDTO();
        createTask.setContent("content");
        createTask.setTitle("title");
        createTask.setStatus("published");
        taskUtils.add(createTask);

        testTask = taskMapper.map(taskUtils.getById(1));

        var createLabel = new CreateLabelDTO();
        createLabel.setName("Label Name");
        labelUtils.add(createLabel);
        testLabel = labelMapper.map(labelUtils.getById(Long.valueOf(1)));
    }

//    @Test
//    public void testShow() throws Exception {
//        MvcResult result = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with())
//                .andExpect(status().isOk())
//                .andReturn();
//        String body = result.getResponse().getContentAsString();
//
//        assertThatJson(body).and(
//                task -> task.node("title").isEqualTo(testTask.getName()),
//                task -> task.node("status").isEqualTo(testTask.getTaskStatus().getSlug())
//        );
//    }



}