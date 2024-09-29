package overcloud.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Embeddable
@Getter
@Setter
public class PracticeChoiceQuestionId {
    @Column(name = "practice_id")
    private UUID practiceId;

    @Column(name = "choice_answer_id")
    private UUID choiceAnswerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PracticeChoiceQuestionId that = (PracticeChoiceQuestionId) o;

        if (!practiceId.equals(that.practiceId)) return false;
        return choiceAnswerId.equals(that.choiceAnswerId);
    }

    @Override
    public int hashCode() {
        int result = practiceId.hashCode();
        result = 31 * result + choiceAnswerId.hashCode();
        return result;
    }
}
