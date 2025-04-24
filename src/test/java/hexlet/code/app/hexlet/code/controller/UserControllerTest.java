package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.DTO.user.CreateUserDTO;
import hexlet.code.DTO.user.UserDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.services.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;


    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        mock = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();

        createData.setEmail("ya@ya.ya");
        createData.setFirstName("Ivan");
        createData.setLastName("Ivanov");
        createData.setPassword("password");
    }

    @Test
    void create() throws Exception {

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(parser.writeValueAsString(createData));
        var response = mock.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        var body = response.getResponse().getContentAsString();
        Assertions.assertThat(body.contains(createData.getEmail()));
        Assertions.assertThat(body.contains("id"));
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
        var objMapper = new ObjectMapper();
        var token = jwt().jwt(b -> b.subject(createData.getEmail()));
        var data = new HashMap<String, String>();
        data.put("firstName", "New name");
        var request = put("/api/users/{id}", utils.getByEmail(createData.getEmail()).getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(parser.writeValueAsString(data))
                .with(token);

        var result = mock.perform(request)
                    .andExpect(status().isOk())
                    .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("email").isEqualTo(createData.getEmail()),
                v -> v.node("firstName").isEqualTo(data.get("firstName")),
                v -> v.node("lastName").isEqualTo(createData.getLastName())
        );

        var actualUser = utils.getByEmail(createData.getEmail());

        assertEquals(data.get("firstName"), actualUser.getFirstName());
        assertEquals(createData.getLastName(), actualUser.getLastName());
        assertEquals(createData.getEmail(), actualUser.getEmail());

        var updatedUser = repository.findById(utils.getByEmail(createData.getEmail()).getId()).get();
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo((createData.getEmail()));
    }

    @Test
    void delete() throws Exception {
        utils.add(createData);
        var user = utils.getByEmail(createData.getEmail());
        var token = jwt().jwt(b -> b.subject(createData.getEmail()));
        if (!repository.findAll().isEmpty()) {
            var request = MockMvcRequestBuilders
                    .delete("/api/users/{id}", utils.getByEmail(createData.getEmail()).getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(token);
            var userId = repository.findByEmail(createData.getEmail()).get().getId();
            mock.perform(request)
                    .andExpect(status().isNoContent());
            Assertions.assertThat(repository.findAll()).isEmpty();
        }
    }

    @Test
    void index() throws Exception {
        utils.add(createData);
        var user = utils.getByEmail(createData.getEmail());

        var response = mock
                .perform(get("/api/users"))
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
