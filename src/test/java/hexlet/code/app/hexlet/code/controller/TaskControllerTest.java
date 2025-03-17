package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.DTO.task.CreateTaskDTO;
import hexlet.code.DTO.task.UpdateTaskDTO;
import hexlet.code.DTO.user.CreateUserDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.services.TaskUtils;
import hexlet.code.services.UserUtils;
import hexlet.code.utils.AppInit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppInit app;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private TaskUtils taskUtils;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    private User testUser;

    private Task testTask;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    void setUp() {
        app.init();

        var createUser = new CreateUserDTO();
        createUser.setEmail("email@email.com");
        createUser.setPassword("password");
        createUser.setLastName("Ivanov");
        createUser.setFirstName("Ivan");
        userUtils.add(createUser);
        testUser = userMapper.map(userUtils.getByEmail(createUser.getEmail()));

        token = jwt().jwt(b -> b.subject(testUser.getEmail()));
        var createTask = new CreateTaskDTO();
        createTask.setContent("content");
        createTask.setTitle("title");
        createTask.setStatus("published");
        taskUtils.add(createTask);

        testTask = taskMapper.map(taskUtils.getAll().getFirst());
    }

    @Test
    public void testUpdate() throws Exception {
        var task = taskUtils.getById(testTask.getId());
        var data = new HashMap<String, String>();
        var name = "New Task Name";
        data.put("title", name);

        var request = put("/api/tasks/{id}", task.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("title").isEqualTo(data.get("title")),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus()),
                v -> v.node("taskLabelIds").isEqualTo(testTask.getLabels())
        );

        var actualTask = taskMapper.map(taskUtils.getAll().stream()
                .findFirst().get());

        assertEquals(name, actualTask.getName());
        assertEquals(testTask.getDescription(), actualTask.getDescription());
        assertEquals(testTask.getTaskStatus(), actualTask.getTaskStatus());
        assertEquals(testTask.getLabels(), actualTask.getLabels());
    }

    @Test
    public void show() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();
        String body = response.getResponse().getContentAsString();

        assertThatJson(body).and(
                task -> task.node("title").isEqualTo(testTask.getName()),
                task -> task.node("status").isEqualTo(testTask.getTaskStatus())
        );
    }

    @Test
    public void index() throws Exception {
        var response = mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response)
                .contains(String.valueOf(testTask.getAssignee().getId()))
                .contains(testTask.getName())
                .contains(testTask.getDescription())
                .contains(testTask.getTaskStatus().getSlug());
    }

    @Test
    public void update() throws Exception {
        var updateData = new UpdateTaskDTO();
        updateData.setTitle("new title");

        mockMvc.perform(put("/api/tasks/" + testTask.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updateData)))
                .andReturn().getResponse().getContentAsString();

        var updatedTask = taskUtils.getById(testTask.getId());

        assertThat(updatedTask.getTitle()).isEqualTo(updateData.getTitle());
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId())
                        .with(token))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
        assertThat(taskUtils.getAll().isEmpty());
    }

    @Test
    public void unauthorized() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/tasks/" + testTask.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/tasks/" + testTask.getId()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/tasks/" + testTask.getId()))
                .andExpect(status().isUnauthorized());
    }
}
