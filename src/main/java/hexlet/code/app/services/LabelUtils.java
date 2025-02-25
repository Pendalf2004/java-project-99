package hexlet.code.app.services;

import hexlet.code.app.DTO.label.CreateLabelDTO;
import hexlet.code.app.DTO.label.LabelDTO;
import hexlet.code.app.DTO.label.UpdateLabelDTO;
import hexlet.code.app.exception.InUseException;
import hexlet.code.app.exception.NotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class LabelUtils {
    @Autowired
    private LabelRepository repository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelMapper mapper;

    public List<LabelDTO> getAll() {
        List<Label> labels = repository.findAll();
        return labels.stream().map(mapper::map).toList();
    }

    public LabelDTO getById(Long id) {
        Label label = repository.findById(id).orElseThrow(() -> new NotFoundException("Label with id " + id + " not found"));
        return mapper.map(label);
    }

    public LabelDTO add(CreateLabelDTO createData) {
        Label label = mapper.map(createData);
        repository.save(label);
        return mapper.map(label);
    }

    public LabelDTO update(Long id, UpdateLabelDTO updateData) {
        Label label = repository.findById(id).orElseThrow(() -> new NotFoundException("Label with id " + id + " not found"));
        mapper.update(updateData, label);
        repository.save(label);
        return mapper.map(label);
    }

    public void delete(Long id) {
        Set<Long> links = repository.getIdFromCrossTable(id);
        if (!links.isEmpty()) {
            throw new InUseException("Label is assigned to some task");
        }
        repository.deleteById(id);
    }
}
