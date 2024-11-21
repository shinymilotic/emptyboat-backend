package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "choice_answers", schema = "public")
public class ChoiceAnswerEntity {
    @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    private UUID choiceAnswerId;

    @Column(name = "question_id")
    private UUID questionId;

    @Column(name = "answer")
    private String answer;

    @Column(name = "truth")
    private boolean truth;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
