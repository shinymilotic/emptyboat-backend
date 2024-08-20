package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "choice_answers", schema = "public")
public class AnswerEntity {
    @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    private UUID answerId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "truth")
    private boolean truth;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
