package hexlet.code.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "task_statuses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class TaskStatus implements BaseModel{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    @ToString.Include
    private String name;

    @Column(unique = true, nullable = false)
    @ToString.Include
    private String slug;

    @CreatedDate
    private LocalDate createdAt;
}
