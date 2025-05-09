package hexlet.code.services;

import hexlet.code.DTO.TaskFilterSpecDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskFilter {

    public Specification<Task> build(TaskFilterSpecDTO params) {
        return withTitleCont(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withStatus(params.getStatus()))
                .and(withLabelId(params.getLabelId()));
    }

    private Specification<Task> withLabelId(Long labelId) {
        return ((root, query, cb) -> labelId == null ? cb.conjunction()
                : cb.equal(root.get("labels").get("id"), labelId));
    }

    private Specification<Task> withStatus(String status) {
        return ((root, query, cb) -> status == null ? cb.conjunction()
                : cb.like(root.get("taskStatus").get("slug"), status));
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return ((root, query, cb) -> assigneeId == null ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), assigneeId));
    }

    private Specification<Task> withTitleCont(String titleCont) {
        return ((root, query, cb) -> titleCont == null ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + titleCont + "%"));
    }
}
