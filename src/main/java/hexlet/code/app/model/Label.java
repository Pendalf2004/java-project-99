package hexlet.code.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "labels")
@Getter
@Setter
@NoArgsConstructor
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long        id;

    @Column(unique = true, columnDefinition = "TEXT")
    @NotBlank
    private String      name;

    @CreatedDate
    private LocalDate   createdAt;

    public Label(Long id) {
        this.id = id;
    }
}
