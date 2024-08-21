package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import overcloud.blog.entity.TestQuestion;
import overcloud.blog.entity.TestQuestionId;

@Repository
public interface JpaTestQuestionRepository extends JpaRepository<TestQuestion, TestQuestionId> {
    
}
