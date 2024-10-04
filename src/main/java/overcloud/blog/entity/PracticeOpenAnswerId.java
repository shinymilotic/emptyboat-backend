package overcloud.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PracticeOpenAnswerId implements Serializable {
    @Column(name = "practice_id")
    private UUID practiceId;

    @Column(name = "question_id")
    private UUID questionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PracticeOpenAnswerId that = (PracticeOpenAnswerId) o;

        if (!practiceId.equals(that.practiceId)) return false;
        return questionId.equals(that.questionId);
    }

    @Override
    public int hashCode() {
        int result = practiceId.hashCode();
        result = 31 * result + questionId.hashCode();
        return result;
    }
}
