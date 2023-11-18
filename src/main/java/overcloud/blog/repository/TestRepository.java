package overcloud.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TestEntity;

import java.util.UUID;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, UUID> {

}

