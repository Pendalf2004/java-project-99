package hexlet.code.app.repository;

import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findAllByTaskStatus(TaskStatus status);

    @Query(value = """
            SELECT * FROM tasks
            WHERE tasks.assignee_id = ?1
            """, nativeQuery = true)
    List<Task> findAllByUserId(Long userId);
}
