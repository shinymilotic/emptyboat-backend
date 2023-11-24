package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "question", schema = "public")
public class QuestionEntity {
    @Id
    private UUID id;

    @Column(name = "question")
    private String question;

    @OneToMany(mappedBy = "question")
    private List<AnswerEntity> answers;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
