package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "practice_choice_questions", schema = "public")
public class PracticeChoiceQuestionEntity {
    @EmbeddedId
    private PracticeChoiceQuestionId practiceChoiceQuestionId;

    @ManyToOne
    @JoinColumn(name = "practice_id", insertable = false, updatable = false)
    private PracticeEntity practice;
}
