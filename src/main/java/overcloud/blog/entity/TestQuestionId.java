package overcloud.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
public class TestQuestionId implements Serializable {
    @Column(name = "test_id")
    private UUID testId;

    @Column(name = "question_id")
    private UUID questionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestQuestionId that = (TestQuestionId) o;

        if (!testId.equals(that.testId)) return false;

        return questionId.equals(that.questionId);
    }

    @Override
    public int hashCode() {
        int result = testId.hashCode();
        result = 31 * result + questionId.hashCode();
        return result;
    }
}

