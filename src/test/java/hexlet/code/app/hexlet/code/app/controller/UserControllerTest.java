package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.DTO.user.UserDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private WebApplicationContext wac;


    @Autowired
    private UserMapper mapper;

    @Autowired
    private ObjectMapper parser;

    @Autowired
    private UserRepository repository;

    private User testUser = new User();


    @BeforeEach
    public void setUp() {
        mock = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();

        testUser.setEmail("ya@ya.ya");
        testUser.setFirstName("Ivan");
        testUser.setLastName("Ivanov");
        testUser.setPassword("password");

    }

    @AfterEach
    public void clearRepo() {
        repository.deleteAll();
    }

    @Test
    void create() throws Exception {
        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(parser.writeValueAsString(testUser));
        mock.perform(request)
                .andExpect(status().isCreated());
        Assertions.assertThat(repository.findByEmail(testUser.getEmail())).isNotEmpty();

    }

    @Test
    void show() throws Exception {
        repository.save(testUser);
        var response = mock.perform(get("/api/users/" + testUser.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        UserDTO returnedUser = parser.readValue(body,
                new TypeReference<>() { });
        Assertions.assertThat(returnedUser.getId())
                .isEqualTo(testUser.getId());
    }

    @Test
    void update() throws Exception {
        repository.save(testUser);
        var emailChange = new HashMap<>();
        emailChange.put("email", "ivan@google.com");

        var request = put("/api/users/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(parser.writeValueAsString(emailChange));
        mock.perform(request)
                .andExpect(status().isOk());

        var updatedUser = repository.findById(testUser.getId()).get();
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo(("ivan@google.com"));
    }

    @Test
    void delete() throws Exception {
        repository.save(testUser);
        if (!repository.findAll().isEmpty()) {
            var userId = repository.findByEmail(testUser.getEmail()).get().getId();
            mock.perform(MockMvcRequestBuilders.delete("/api/users/" + userId))
                    .andExpect(status().isNoContent());
            Assertions.assertThat(repository.findAll()).isEmpty();
        }
    }

    @Test
    void index() throws Exception {
        repository.save(testUser);
        var response = mock.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        assertThat(body)
                .contains(String.valueOf(testUser.getEmail()))
                .contains(String.valueOf(testUser.getId()))
                .contains(String.valueOf(testUser.getCreatedAt()));
    }
}
