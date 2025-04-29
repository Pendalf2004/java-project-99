package hexlet.code.services;

import hexlet.code.DTO.label.CreateLabelDTO;
import hexlet.code.DTO.label.LabelDTO;
import hexlet.code.DTO.label.UpdateLabelDTO;
import hexlet.code.exception.InUseException;
import hexlet.code.exception.NotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class Label {
    @Autowired
    private LabelRepository repository;

//    @Autowired
//    private TaskRepository taskRepository;

    @Autowired
    private LabelMapper mapper;

    public List<LabelDTO> getAll() {
        List<hexlet.code.model.Label> labels = repository.findAll();
        return labels.stream().map(mapper::map).toList();
    }

    public LabelDTO getById(Long id) {
        hexlet.code.model.Label label = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Label with id " + id + " not found"));
        return mapper.map(label);
    }

    public LabelDTO add(CreateLabelDTO createData) {
        hexlet.code.model.Label label = mapper.map(createData);
        repository.save(label);
        return mapper.map(label);
    }

    public LabelDTO update(Long id, UpdateLabelDTO updateData) {
        hexlet.code.model.Label label = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Label with id " + id + " not found"));
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
