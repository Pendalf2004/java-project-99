package hexlet.code.services;

import hexlet.code.DTO.user.CreateUserDTO;
import hexlet.code.DTO.user.UpdateUserDTO;
import hexlet.code.DTO.user.UserDTO;
import hexlet.code.exception.InUseException;
import hexlet.code.exception.NotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserUtils {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserMapper mapper;

    public UserUtils(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean check(String providedPass, String encodedPassword) {
        return passwordEncoder.matches(providedPass, encodedPassword);
    }

    public List<UserDTO> getAll() {
        List<User> users = repository.findAll();
        return users.stream().map(mapper::map).toList();
    }

    public UserDTO getById(long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        return mapper.map(user);
    }

    public UserDTO add(CreateUserDTO createData) {
        User user = mapper.map(createData);
        var hashedPass = hash(createData.getPassword());
        user.setPassword(hashedPass);
        repository.save(user);
        return mapper.map(user);
    }

    public UserDTO update(Long id, UpdateUserDTO updateData) {
        User user = repository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id " + id + " not found"));
        String pass = user.getPassword();
        try {
            pass = hash(updateData.getPassword());
        } catch (NullPointerException | IllegalArgumentException e) {

        } finally {
            user.setPassword(pass);
        }
        mapper.update(user, updateData);
        repository.save(user);
        return mapper.map(user);
    }

    public void delete(Long id) {
        List<Task> tasks = taskRepository.findAllByUserId(id);
        if (!tasks.isEmpty()) {
            throw new InUseException("User has assigned tasks");
        }
        repository.deleteById(id);
    }

    public UserDTO getByEmail(String email) {
        return mapper.map(repository.findByEmail(email).get());
    }

}
