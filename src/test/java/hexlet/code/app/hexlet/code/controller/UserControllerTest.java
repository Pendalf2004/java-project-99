package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.DTO.user.CreateUserDTO;
import hexlet.code.DTO.user.UserDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.services.UserUtils;
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

    @Autowired
    private UserUtils utils;

    private CreateUserDTO createData = new CreateUserDTO();


    @BeforeEach
    public void setUp() {
        mock = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();

        createData.setEmail("ya@ya.ya");
        createData.setFirstName("Ivan");
        createData.setLastName("Ivanov");
        createData.setPassword("password");

    }

    @AfterEach
    public void clearRepo() {
        repository.deleteAll();
    }

    @Test
    void create() throws Exception {

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(parser.writeValueAsString(createData));
        mock.perform(request)
                .andExpect(status().isCreated());
        Assertions.assertThat(repository.findByEmail(createData.getEmail())).isNotEmpty();

    }

    @Test
    void show() throws Exception {
        utils.add(createData);
        var user = utils.getByEmail(createData.getEmail());
        var response = mock.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();
        UserDTO returnedUser = parser.readValue(body,
                new TypeReference<>() { });
        Assertions.assertThat(returnedUser.getId())
                .isEqualTo(user.getId());
    }

    @Test
    void update() throws Exception {
        utils.add(createData);
        var user = utils.getByEmail(createData.getEmail());
        var emailChange = new HashMap<>();
        emailChange.put("email", "ivan@google.com");

        var request = put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(parser.writeValueAsString(emailChange));
        mock.perform(request)
                .andExpect(status().isOk());

        var updatedUser = repository.findById(user.getId()).get();
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo(("ivan@google.com"));
    }

    @Test
    void delete() throws Exception {
        utils.add(createData);
        var user = utils.getByEmail(createData.getEmail());

        if (!repository.findAll().isEmpty()) {
            var userId = repository.findByEmail(createData.getEmail()).get().getId();
            mock.perform(MockMvcRequestBuilders.delete("/api/users/" + userId))
                    .andExpect(status().isNoContent());
            Assertions.assertThat(repository.findAll()).isEmpty();
        }
    }

    @Test
    void index() throws Exception {
        utils.add(createData);
        var user = utils.getByEmail(createData.getEmail());

        var response = mock.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        assertThat(body)
                .contains(String.valueOf(user.getEmail()))
                .contains(String.valueOf(user.getId()))
                .contains(String.valueOf(user.getCreatedAt()));
    }
}
