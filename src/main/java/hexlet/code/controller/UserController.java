package hexlet.code.controller;

import hexlet.code.DTO.user.CreateUserDTO;
import hexlet.code.DTO.user.UpdateUserDTO;
import hexlet.code.DTO.user.UserDTO;
import hexlet.code.services.UsersServices;
import hexlet.code.utils.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UsersServices usersServices;

    @Autowired
    private AuthUtils auth;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody CreateUserDTO createData) {
        return usersServices.add(createData);
    }

    @GetMapping("/{id}")
    public UserDTO show(@PathVariable Long id) {
        return usersServices.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@userRepository.findById(#id).get().getEmail().equals(authentication.name)")
    public UserDTO update(@Valid @RequestBody UpdateUserDTO updateData, @PathVariable Long id) {
        return usersServices.update(id, updateData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userRepository.findById(#id).get().getEmail().equals(authentication.name)")
    public void delete(@PathVariable Long id) {
        usersServices.delete(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> index() {
        var content = usersServices.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(content.size()))
                .body(content);
    }
}
