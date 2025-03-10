package hexlet.code.app.controller;

import hexlet.code.app.model.Authentication;
import hexlet.code.app.repository.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private UserRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private Authentication generateAuthRequest(String email, String password) {
        Authentication data = new Authentication();
        data.setUsername(email);
        data.setPassword(password);
        return data;
    }

    @Test
    public void loginSuccess() throws Exception {
        Authentication requestBody = generateAuthRequest("hexlet@example.com", "qwerty");
        var request = post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        var response = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var body = response.getResponse().getContentAsString();
        assertThat(body).isNotNull();
    }

    @Test
    public void loginFailure() throws Exception {
        Authentication requestBody = generateAuthRequest("xxx@xxx.xxx", "password");

        var request = post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody));

        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }
}
