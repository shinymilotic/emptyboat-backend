package overcloud.blog.entity;


import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "practice_choices", schema = "public")
public class PracticeChoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "answer_id")
    private UUID answerId;

    @Column(name = "practice_id")
    private UUID practiceId;
}
