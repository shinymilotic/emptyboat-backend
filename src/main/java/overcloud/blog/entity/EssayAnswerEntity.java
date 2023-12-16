package overcloud.blog.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "essay_answer", schema = "public")
public class EssayAnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "answer")
    private String answer;

    @Column(name = "question_id")
    private UUID questionId;

    @Column(name = "practice_id")
    private UUID practiceId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
