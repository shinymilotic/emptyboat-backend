package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "practice_open_answers", schema = "public")
public class PracticeOpenAnswerEntity {
    @EmbeddedId
    private PracticeOpenAnswerId id;

    @Column(name = "answer")
    private String answer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
