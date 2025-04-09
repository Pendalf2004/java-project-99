package hexlet.code.controller;


import hexlet.code.DTO.label.CreateLabelDTO;
import hexlet.code.DTO.label.LabelDTO;
import hexlet.code.DTO.label.UpdateLabelDTO;
import hexlet.code.services.LabelUtils;
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
    private LabelUtils labelUtils;

    @GetMapping
    private ResponseEntity<List<LabelDTO>> index() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labelUtils.getAll().size()))
                .body(labelUtils.getAll());
    }

    @GetMapping("/{id}")
    private LabelDTO show(@PathVariable Long id) {
        return labelUtils.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private LabelDTO create(@RequestBody CreateLabelDTO createData) {
        return labelUtils.add(createData);
    }

    @PutMapping("/{id}")
    private LabelDTO update(@PathVariable Long id, @Valid @RequestBody UpdateLabelDTO updateData) {
        return labelUtils.update(id, updateData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable Long id) {
        labelUtils.delete(id);
    }
}
