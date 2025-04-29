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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsersServices {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserMapper mapper;

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
        repository.save(user);
        return mapper.map(user);
    }

    public UserDTO update(Long id, UpdateUserDTO updateData) {
        User user = repository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id " + id + " not found"));

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
