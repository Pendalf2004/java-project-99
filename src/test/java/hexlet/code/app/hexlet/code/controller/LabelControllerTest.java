package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.DTO.label.CreateLabelDTO;
import hexlet.code.DTO.label.UpdateLabelDTO;
import hexlet.code.DTO.user.CreateUserDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.services.Label;
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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LabelControllerTest {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppInit app;

    @Autowired
    private UsersServices usersServices;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private Label labelUtils;

    private User testUser;

    private hexlet.code.model.Label testLabel;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

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

        var createLabel = new CreateLabelDTO();
        createLabel.setName("test");
        labelUtils.add(createLabel);

        testLabel = labelMapper.map(labelUtils.getAll().getFirst());
    }

    @Test
    public void show() throws Exception {
        var response = mockMvc.perform(get("/api/labels/" + testLabel.getId())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("name").isEqualTo(testLabel.getName())
        );
    }

    @Test
    public void index() throws Exception {
        var response = mockMvc.perform(get("/api/labels")
                        .with(token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(response).contains(testLabel.getName());
    }

    @Test
    public void create() throws Exception {
        var createData = new CreateLabelDTO();
        createData.setName("new label");

        mockMvc.perform(post("/api/labels")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(createData)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var label = labelRepository.findByName(createData.getName()).orElseThrow();
        assertThat(label.getName()).isEqualTo(createData.getName());
    }

    @Test
    public void updateTest() throws Exception {
        var updateData = new UpdateLabelDTO();
        updateData.setName("update label");

        mockMvc.perform(put("/api/labels/" + testLabel.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updateData)))
                .andExpect(status().isOk());

        assertThat(labelRepository.findByName(updateData.getName())).isPresent();
    }

    @Test
    public void deleteTest() throws Exception {

        mockMvc.perform(delete("/api/labels/" + testLabel.getId())
                        .with(token))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
        assertFalse(labelRepository.findByName(testLabel.getName()).isPresent());
    }

    @Test
    public void unauthorizedTest() throws Exception {
        mockMvc.perform(get("/api/labels"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/labels/" + testLabel.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/labels/" + testLabel.getId()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/labels/" + testLabel.getId()))
                .andExpect(status().isUnauthorized());
    }
}
