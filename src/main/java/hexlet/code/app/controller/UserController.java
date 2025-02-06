package hexlet.code.app.controller;

import hexlet.code.app.DTO.User.CreateUserDTO;
import hexlet.code.app.DTO.User.UpdateUserDTO;
import hexlet.code.app.DTO.User.UserDTO;
import hexlet.code.app.exception.NotFoundException;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.services.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository repository;

    @Autowired
    UserMapper mapper;

    @Autowired
    UserUtils utils;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody CreateUserDTO createData) {
        var userToCreate = mapper.map(createData);
        var hashedPass = utils.hash(userToCreate.getEmail(), userToCreate.getPassword());
        userToCreate.setPassword(hashedPass);
        repository.save(userToCreate);
        return mapper.map(userToCreate);
    }

    @GetMapping("/{id}")
    public UserDTO show(@PathVariable Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found."));
        return mapper.map(user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@Valid @RequestBody UpdateUserDTO updateData, @PathVariable Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found."));
        mapper.update(user, updateData);
        repository.save(user);
        return mapper.map(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> index() {
        var users = repository.findAll();
        return users.stream()
                .map(mapper::map)
                .toList();
    }

}
