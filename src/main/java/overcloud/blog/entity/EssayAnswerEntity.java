package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "essay_answers", schema = "public")
public class EssayAnswerEntity {
    @Id
    private UUID essayAnswerId;

    @Column(name = "answer")
    private String answer;

    @Column(name = "question_id")
    private UUID questionId;

    @Column(name = "practice_id")
    private UUID practiceId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "practice_id", insertable = false, updatable = false)
    private PracticeEntity practice;
}
