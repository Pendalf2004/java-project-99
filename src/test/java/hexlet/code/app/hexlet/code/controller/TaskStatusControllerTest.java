package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.DTO.taskStatus.CreateTaskStatusDTO;
import hexlet.code.DTO.taskStatus.UpdateTaskStatusDTO;
import hexlet.code.DTO.user.CreateUserDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.services.TaskStatusService;
import hexlet.code.services.UsersServices;
import hexlet.code.utils.AppInit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private AppInit app;

    @Autowired
    private UsersServices usersServices;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskStatusService statusUtils;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    private User testUser;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private TaskStatus testTaskStatus;

    @BeforeEach
    void setUp() {
        app.init();

        var createUser = new CreateUserDTO();
        createUser.setEmail("email@email.com");
        createUser.setPassword("password");
        createUser.setLastName("Ivanov");
        createUser.setFirstName("Ivan");
        usersServices.add(createUser);
        testUser = userMapper.map(usersServices.getByEmail(createUser.getEmail()));

        token = jwt().jwt(b -> b.subject(testUser.getEmail()));

        statusUtils.add(new CreateTaskStatusDTO("name", "slug"));
        testTaskStatus = taskStatusMapper.map(statusUtils.getAll().getFirst());
    }

    @Test
    void index() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(response.getContentAsString().contains(testTaskStatus.getName()));
    }

    @Test
    void show() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses/" + testTaskStatus.getId())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(response.getContentAsString())
                .contains(testTaskStatus.getName());
    }

    @Test
    void create() throws Exception {
        var newStatus = new CreateTaskStatusDTO("new", "new slug");

        mockMvc.perform(post("/api/task_statuses")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newStatus)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var status = taskStatusRepository.findBySlug(newStatus.getSlug()).get();
        assertThat(status.getName()).isEqualTo(newStatus.getName());
        assertThat(status.getSlug()).isEqualTo(newStatus.getSlug());
    }

    @Test
    void update() throws Exception {
        var updateData = new UpdateTaskStatusDTO("new name", "new slug");
        mockMvc.perform(put("/api/task_statuses/" + testTaskStatus.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        testTaskStatus = taskStatusMapper.map(statusUtils.getById(testTaskStatus.getId()));
        assertThat(testTaskStatus.getSlug()).isEqualTo(updateData.getSlug());
        assertThat(testTaskStatus.getName()).isEqualTo(updateData.getName());
    }

    @Test
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId())
                        .with(token))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
        assertThat(statusUtils.getAll().isEmpty());
    }

    @Test
    public void unauthorized() throws Exception {
        mockMvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/task_statuses/" + testTaskStatus.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/task_statuses"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/task_statuses/" + testTaskStatus.getId()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId()))
                .andExpect(status().isUnauthorized());
    }
}
