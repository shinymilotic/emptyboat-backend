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
}