package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.jparepository.JpaPracticeRepository;
import overcloud.blog.usecase.test.common.Answer;
import overcloud.blog.usecase.test.get_practice.PracticeChoiceQuestion;
import overcloud.blog.usecase.test.get_practice.PracticeEssayQuestion;
import overcloud.blog.usecase.test.get_practice.PracticeQuestion;
import overcloud.blog.usecase.test.get_practice.PracticeResult;

import java.util.*;

@Repository
public class PracticeRepositoryImpl implements IPracticeRepository {
    private final JpaPracticeRepository jpa;
    private final EntityManager entityManager;

    public PracticeRepositoryImpl(JpaPracticeRepository jpa, EntityManager entityManager) {
        this.jpa = jpa;
        this.entityManager = entityManager;
    }

    @Override
    public List<PracticeEntity> findByTesterId(UUID testerId) {
        TypedQuery<PracticeEntity> practiceQuery = entityManager
                .createQuery("SELECT p FROM PracticeEntity p JOIN FETCH p.test  WHERE p.testerId = :testerId ORDER BY p.createdAt DESC ",
                        PracticeEntity.class)
                .setParameter("testerId", testerId);

        return practiceQuery.getResultList();
    }

    @Override
    public void deleteByTestId(UUID testId) {
        jpa.deleteByTestId(testId);
    }

    @Override
    public Optional<PracticeEntity> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public List<Object> getPracticeResult(UUID practiceId) {
        Query practiceResult = entityManager
                .createNativeQuery("select q.id as questionId, q.question, q.question_type , " +
                        " a.id as answerId, a.answer as choiceAnswer , a.truth , ea.answer as essayAnswer" +
                        " from practice p" +
                        " inner join test t on p.test_id = t.id and p.id = :practiceId " +
                        " inner join test_question tq on tq.test_id = t.id " +
                        " inner join question q ON tq.question_id = q.id " +
                        " left join practice_choices pc on pc.practice_id = p.id " +
                        " inner join answer a on pc.answer_id = a.id " +
                        " left join essay_answer ea on ea.practice_id = p.id")
                .setParameter("practiceId", practiceId);

        List<Tuple> results = practiceResult.getResultList();

        getResults(results);
        return new ArrayList<>();
    }

    List<Tuple> getResults(List<Tuple> results) {
        PracticeResult practiceResult = new PracticeResult();
        List<PracticeQuestion> questions = new ArrayList<>();
        Map<UUID, PracticeQuestion> cachedQuestions = new HashMap<>();
        for (Tuple data : results) {
            UUID questionId = (UUID) data.get("questionId");

            String question = (String) data.get("question");
            Integer questionType = (Integer) data.get("question_type");
            UUID answerId = (UUID) data.get("answerId");
            String choiceAnswer = (String) data.get("choiceAnswer");
            Boolean truth = (Boolean) data.get("truth");
            String essayAnswer = (String) data.get("essayAnswer");

            if (questionType != null && questionType.equals(1) && !cachedQuestions.containsKey(questionId)) {
                List<Answer> answers = new ArrayList<>();
                answers.add(Answer.answerFactory(answerId, choiceAnswer, truth));
                PracticeChoiceQuestion practiceChoiceQuestion = PracticeChoiceQuestion.questionFactory(questionId, question, answers);
                cachedQuestions.put(questionId, practiceChoiceQuestion);
                questions.add(practiceChoiceQuestion);
            }

            if (questionType != null && questionType.equals(2) && !cachedQuestions.containsKey(questionId)) {
                questions.add(PracticeEssayQuestion.questionFactory(questionId, question, essayAnswer));
            }

            if (cachedQuestions.containsKey(questionId)) {
                PracticeChoiceQuestion practiceChoiceQuestion = (PracticeChoiceQuestion) cachedQuestions.get(questionId);
                practiceChoiceQuestion.getAnswers().add(Answer.answerFactory(answerId, choiceAnswer, truth));
            }
        }

        practiceResult.setQuestions(questions);

        return new ArrayList<>();
    }

    @Override
    public PracticeEntity save(PracticeEntity entity) {
        return jpa.save(entity);
    }
}
