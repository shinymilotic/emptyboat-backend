package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.repository.ITestRepository;
import overcloud.blog.repository.jparepository.JpaTestRepository;
import overcloud.blog.usecase.test.common.Answer;
import overcloud.blog.usecase.test.common.ChoiceQuestion;
import overcloud.blog.usecase.test.common.EssayQuestion;
import overcloud.blog.usecase.test.common.TestResponse;
import overcloud.blog.usecase.test.get_list_test.TestListRecord;
import overcloud.blog.usecase.user.common.UserResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TestRepositoryImpl implements ITestRepository {
    private final JpaTestRepository jpa;
    private final EntityManager entityManager;

    public TestRepositoryImpl(JpaTestRepository jpaTestRepository, EntityManager entityManager) {
        this.jpa = jpaTestRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<TestListRecord> findAll() {
        String s = "SELECT new overcloud.blog.usecase.test.get_list_test.TestListRecord(t.id, t.title, t.description) " +
                " FROM TestEntity t ";
        TypedQuery<TestListRecord> testQuery = entityManager.createQuery(s, TestListRecord.class);
        return testQuery.getResultList();
    }

    @Override
    public Optional<TestEntity> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public TestEntity save(TestEntity testEntity) {
        return jpa.save(testEntity);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<TestResponse> getTestResponse(UUID id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.title, t.description, q.id as questionId , q.question , q.question_type , author.id as authorId, author.username , author.email , author.bio , author.image ");
        sql.append(" , a.id as answerId, a.answer, a.truth  ");
        sql.append("FROM test t ");
        sql.append("inner join users author on t.author_id = author.id ");
        sql.append("inner join test_question tq on t.id = tq.test_id ");
        sql.append("inner join question q on q.id = tq.question_id ");
        sql.append("left join answer a on a.question_id = q.id ");
        sql.append("WHERE t.id = :id ");
        sql.append("ORDER BY q.id ");

        Query query = entityManager.createNativeQuery(sql.toString(), Tuple.class);
        query.setParameter("id", id);
        
        List<Tuple> results = query.getResultList();
        
        Optional<TestResponse> res = Optional.empty();
        Tuple first = results.getFirst();
        if (first != null) {
            TestResponse testResponse = new TestResponse();
            testResponse.setTitle((String) first.get("title"));
            testResponse.setDescription((String) first.get("description"));
            UserResponse author = new UserResponse();
            author.setId((UUID) first.get("authorId"));
            author.setUsername((String) first.get("username"));
            author.setEmail((String) first.get("email"));
            author.setBio((String) first.get("bio"));
            author.setImage((String) first.get("image"));
            testResponse.setAuthor(author);
            res = Optional.of(testResponse);
        } else {
            return res;
        }

        TestResponse testResponse = res.get();
        Map<UUID, ChoiceQuestion> choiceQuestionMap = new HashMap<>();
        UUID previousQuestionId = null;
        for (Tuple result : results) {
            Integer questionType = (Integer) result.get("question_type");
            UUID questionId = (UUID) result.get("questionId");
            String question = (String) result.get("question");
            
            if (questionType.equals(1) && questionId.equals(previousQuestionId)) {
                ChoiceQuestion choiceQuestion = choiceQuestionMap.get(previousQuestionId);
                UUID answerId = (UUID) result.get("answerId");
                String strAnswer = (String) result.get("answer");
                Boolean truth = (Boolean) result.get("truth");
                Answer answer = Answer.answerFactory(answerId, strAnswer, truth);
                choiceQuestion.getAnswers().add(answer);
            } else if (questionType.equals(1)) {
                ChoiceQuestion choiceQuestion = new ChoiceQuestion();
                choiceQuestion.setId(questionId.toString());
                choiceQuestion.setQuestion(question);
                choiceQuestion.setQuestionType(questionType);
                choiceQuestionMap.put(id, choiceQuestion);
                testResponse.getQuestions().add(choiceQuestion);
            } else if (questionType.equals(2)) {
                EssayQuestion essayQuestion = new EssayQuestion();
                essayQuestion.setId(questionId.toString());
                essayQuestion.setQuestion(question);
                essayQuestion.setQuestionType(questionType);
                testResponse.getQuestions().add(essayQuestion);
            }
            previousQuestionId = questionId;
        }

        return res;
    }
}
