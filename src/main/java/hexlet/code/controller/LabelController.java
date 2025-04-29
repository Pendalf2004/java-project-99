package hexlet.code.controller;


import hexlet.code.DTO.label.CreateLabelDTO;
import hexlet.code.DTO.label.LabelDTO;
import hexlet.code.DTO.label.UpdateLabelDTO;
import hexlet.code.services.Label;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/labels")
public class LabelController {
    @Autowired
    private Label labelService;

    @GetMapping
    private ResponseEntity<List<LabelDTO>> index() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labelService.getAll().size()))
                .body(labelService.getAll());
    }

    @GetMapping("/{id}")
    private LabelDTO show(@PathVariable Long id) {
        return labelService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private LabelDTO create(@RequestBody CreateLabelDTO createData) {
        return labelService.add(createData);
    }

    @PutMapping("/{id}")
    private LabelDTO update(@PathVariable Long id, @Valid @RequestBody UpdateLabelDTO updateData) {
        return labelService.update(id, updateData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable Long id) {
        labelService.delete(id);
    }
}
