package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "practice_choice_answers", schema = "public")
public class PracticeChoiceAnswerEntity {
    @EmbeddedId
    private PracticeChoiceAnswerId practiceChoiceAnswerId;
}
