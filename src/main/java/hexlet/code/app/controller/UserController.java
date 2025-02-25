package hexlet.code.app.controller;

import hexlet.code.app.DTO.user.CreateUserDTO;
import hexlet.code.app.DTO.user.UpdateUserDTO;
import hexlet.code.app.DTO.user.UserDTO;
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
    UserUtils utils;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody CreateUserDTO createData) {
        return utils.add(createData);
    }

    @GetMapping("/{id}")
    public UserDTO show(@PathVariable Long id) {
        return utils.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@Valid @RequestBody UpdateUserDTO updateData, @PathVariable Long id) {
        return utils.update(id, updateData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        utils.delete(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> index() {
        return utils.getAll();
    }

}
