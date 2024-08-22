package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "practices", schema = "public")
public class PracticeEntity {
    @Id
    private UUID practiceId;

    @Column(name = "tester_id")
    private UUID testerId;

    @Column(name = "test_id")
    private UUID testId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
