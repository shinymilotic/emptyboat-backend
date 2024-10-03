package overcloud.blog.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "practice_choices", schema = "public")
public class PracticeChoiceEntity {
    @Id
    private UUID practiceChoiceId;

    @Column(name = "choice_answer_id")
    private UUID choiceAnswerId;

    @Column(name = "practice_id")
    private UUID practiceId;

    @ManyToOne
    @JoinColumn(name = "practice_id", insertable = false, updatable = false)
    private PracticeEntity practice;
}
