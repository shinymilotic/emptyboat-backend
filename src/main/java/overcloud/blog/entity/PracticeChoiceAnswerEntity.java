package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "practice_choice_answers", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PracticeChoiceAnswerEntity {
    @EmbeddedId
    private PracticeChoiceAnswerId id;
}
