package overcloud.blog.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PraceticeChoiceId implements Serializable {
    @Column(name = "practice_id")
    private UUID practiceId;

    @Column(name = "answer_id")
    private UUID answerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PraceticeChoiceId that = (PraceticeChoiceId) o;

        if (!practiceId.equals(that.practiceId)) return false;
        return answerId.equals(that.answerId);
    }

    @Override
    public int hashCode() {
        int result = practiceId.hashCode();
        result = 31 * result + answerId.hashCode();
        return result;
    }
}
