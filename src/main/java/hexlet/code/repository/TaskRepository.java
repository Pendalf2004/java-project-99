package hexlet.code.repository;

import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findAllByTaskStatus(TaskStatus status);

    List<Task> findAllByAssigneeId(@Param("assigneeId") Long userId);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.labels WHERE t.id = :taskId")
    Task findTaskWithLabelsById(@Param("taskId") Long taskId);
}
