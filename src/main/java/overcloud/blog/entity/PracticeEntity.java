package overcloud.blog.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "practice", schema = "public")
public class PracticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tester_id")
    private UUID testerId;

    @Column(name = "test_id")
    private UUID testId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "score")
    private int score;

    @ManyToOne
    @JoinColumn(name = "test_id", insertable =  false, updatable = false)
    private TestEntity test;

    @OneToMany(mappedBy = "practice", orphanRemoval = true)
    private List<PracticeChoiceEntity> choices;
}
