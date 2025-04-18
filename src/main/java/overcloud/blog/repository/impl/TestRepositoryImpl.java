package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.repository.TestRepository;
import overcloud.blog.repository.jparepository.JpaTestRepository;
import overcloud.blog.usecase.test.common.QuestionType;
import overcloud.blog.usecase.test.create_test.response.Answer;
import overcloud.blog.usecase.test.create_test.response.ChoiceQuestion;
import overcloud.blog.usecase.test.create_test.response.OpenQuestion;
import overcloud.blog.usecase.test.get_test.response.TestResponse;
import overcloud.blog.usecase.test.get_list_test.TestListRecord;
import overcloud.blog.usecase.user.common.UserResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TestRepositoryImpl implements TestRepository {
    private final JpaTestRepository jpa;

    @PersistenceContext
    private final EntityManager entityManager;

    public TestRepositoryImpl(JpaTestRepository jpa, EntityManager entityManager) {
        this.jpa = jpa;
        this.entityManager = entityManager;
    }

    @Override
    public List<TestListRecord> findAll() {
        String s = "SELECT new overcloud.blog.usecase.test.get_list_test.TestListRecord(t.testId, t.title, t.description) " +
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
        sql.append("SELECT t.title, t.description, q.question_id as questionId , q.question, ");
        sql.append(" q.question_type , author.user_id as authorId, author.username , author.email , author.bio , author.image ");
        sql.append(" , a.choice_answer_id as answerId, a.answer, a.truth  ");
        sql.append("FROM tests t ");
        sql.append("INNER JOIN users author on t.author_id = author.user_id ");
        sql.append("LEFT JOIN questions q on q.test_id = t.test_id ");
        sql.append("LEFT JOIN choice_answers a on a.question_id = q.question_id ");
        sql.append("WHERE t.test_id = :id ");
        sql.append("ORDER BY q.question_id ");

        Query query = entityManager.createNativeQuery(sql.toString(), Tuple.class);
        query.setParameter("id", id);
        
        List<Tuple> results = query.getResultList();
        
        Optional<TestResponse> res = Optional.empty();
        if (results.isEmpty()) {
            return res;
        }
        Tuple first = results.getFirst();
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

        Map<UUID, ChoiceQuestion> choiceQuestionMap = new HashMap<>();
        UUID previousQuestionId = null;
        for (Tuple result : results) {
            UUID questionId = (UUID) result.get("questionId");
            if (questionId == null) {
                return res;
            }
            Integer questionType = (Integer) result.get("question_type");
            String question = (String) result.get("question");
            
            if (questionType.equals(QuestionType.CHOICE.getValue())) {
                UUID answerId = (UUID) result.get("answerId");

                if (answerId == null) {
                    continue;
                }

                String strAnswer = (String) result.get("answer");
                Boolean truth = (Boolean) result.get("truth");
                Answer answer = Answer.answerFactory(answerId, strAnswer, truth);

                if (questionId.equals(previousQuestionId)) {
                    ChoiceQuestion choiceQuestion = choiceQuestionMap.get(previousQuestionId);
                    choiceQuestion.getAnswers().add(answer);
                } else {
                    ChoiceQuestion choiceQuestion = new ChoiceQuestion();
                    choiceQuestion.setId(questionId.toString());
                    choiceQuestion.setQuestion(question);
                    choiceQuestion.setQuestionType(questionType);
                    choiceQuestion.getAnswers().add(answer);
                    choiceQuestionMap.put(questionId, choiceQuestion);
                    testResponse.getQuestions().add(choiceQuestion);
                }
            } else if (questionType.equals(QuestionType.OPEN.getValue())) {
                OpenQuestion openQuestion = new OpenQuestion();
                openQuestion.setId(questionId.toString());
                openQuestion.setQuestion(question);
                openQuestion.setQuestionType(questionType);
                testResponse.getQuestions().add(openQuestion);
            }
            previousQuestionId = questionId;
        }

        return res;
    }

    @Override
    public void updateTest(UUID testId, String title, String description) {
        jpa.updateTest(testId, title, description);
    }

    public List<TestEntity> getProfileTest(UUID userId) {
        return this.jpa.findByUserId(userId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jpa.deleteByUserId(userId);
    }
}
