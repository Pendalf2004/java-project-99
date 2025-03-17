package hexlet.code.DTO.task;

import hexlet.code.model.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class TaskDTO {
    private long        id;
    private int         index;
    private LocalDate   createdAt;
    private String      title;
//    @JsonProperty("assignee_id")
    private long        assigneeId;
    private String      content;
    private TaskStatus  status;
    private Set<Long>   taskLabelIds = new HashSet<>();

//    public String getName() {
//        return this.title;
//    }
//
//    public String getDescription() {
//        return this.content;
//    }
//
//    public String getTaskStatus() {
//        return this.status;
//    }
//
//    public Set getLabels() {
//        return this.taskLabelIds;
//    }

}
