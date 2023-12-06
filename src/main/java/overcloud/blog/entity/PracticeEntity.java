package overcloud.blog.entity;

import java.time.LocalDateTime;
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

    @ManyToOne
    @JoinColumn(name = "tester_id")
    private UserEntity tester;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private TestEntity test;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
