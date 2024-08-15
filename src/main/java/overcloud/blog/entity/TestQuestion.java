package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "test_questions", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestQuestion {
    @EmbeddedId
    private TestQuestionId id;

    @ManyToOne
    @MapsId(value = "testId")
    private TestEntity test;

    @ManyToOne
    @MapsId(value = "questionId")
    private QuestionEntity question;

    @Column(name = "question_order")
    private int questionOrder;
}