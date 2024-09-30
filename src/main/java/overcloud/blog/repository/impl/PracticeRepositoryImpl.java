package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.jparepository.JpaPracticeRepository;
import overcloud.blog.usecase.test.get_practice.*;
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
    public List<Tuple> findByTesterId(UUID testerId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT p.practiceId, t.title, p.createdAt FROM PracticeEntity p ");
        query.append("INNER JOIN TestEntity t ON p.testId = t.testId ");
        query.append("WHERE p.testerId = :testerId ORDER BY p.createdAt DESC ");

        TypedQuery<Tuple> practiceQuery = entityManager
                .createQuery(query.toString(), Tuple.class)
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
    public PracticeResult getPracticeResult(UUID practiceId) {
        Query practiceResult = entityManager
                .createNativeQuery("select q.question_id as questionId, q.question, q.question_type , " +
                        " a.choice_answer_id as answerId, a.answer as answer , a.truth , ea.answer as openAnswer, " +
                        " pc.answer_id = a.choice_answer_id as choice" +
                        " FROM practices p" +
                        " INNER JOIN tests t ON p.test_id = t.test_id and p.practice_id = :practiceId " +
                        " INNER JOIN test_questions tq ON tq.test_id = t.test_id " +
                        " INNER JOIN questions q ON tq.question_id = q.question_id " +
                        " LEFT JOIN choice_answers a ON a.question_id  = q.question_id " +
                        " LEFT JOIN practice_choice_answers pc ON pc.practice_id = p.practice_id AND pc.answer_id = a.choice_answer_id " +
                        " LEFT JOIN practice_open_answers ea ON ea.practice_id = p.practice_id AND ea.question_id = q.question_id ", Tuple.class)
                .setParameter("practiceId", practiceId);

        List<Tuple> results = practiceResult.getResultList();

        return getResults(results);
    }

    PracticeResult getResults(List<Tuple> results) {
        PracticeResult practiceResult = new PracticeResult();
        List<PracticeQuestion> questions = new ArrayList<>();
        Map<UUID, PracticeQuestion> cachedQuestions = new HashMap<>();
        for (Tuple data : results) {
            UUID questionId = (UUID) data.get("questionId");

            String question = (String) data.get("question");
            Integer questionType = (Integer) data.get("question_type");
            UUID answerId = (UUID) data.get("answerId");
            String choiceAnswer = (String) data.get("answer");
            Boolean truth = (Boolean) data.get("truth");
            String openAnswer = (String) data.get("openAnswer");
            Boolean choice = (Boolean) data.get("choice");

            if (questionType != null && questionType.equals(1) && !cachedQuestions.containsKey(questionId)) {
                List<PracticeAnswer> answers = new ArrayList<>();
                answers.add(PracticeAnswer.answerFactory(answerId, choiceAnswer, truth, choice));
                PracticeChoiceQuestion practiceChoiceQuestion = PracticeChoiceQuestion.questionFactory(questionId, question, answers);
                cachedQuestions.put(questionId, practiceChoiceQuestion);
                questions.add(practiceChoiceQuestion);
            } else if (cachedQuestions.containsKey(questionId)) {
                PracticeChoiceQuestion practiceChoiceQuestion = (PracticeChoiceQuestion) cachedQuestions.get(questionId);
                practiceChoiceQuestion.getAnswers()
                    .add(PracticeAnswer.answerFactory(answerId, choiceAnswer, truth, choice));
            }

            if (questionType != null && questionType.equals(2) && !cachedQuestions.containsKey(questionId)) {
                questions.add(PracticeOpenQuestion.questionFactory(questionId, question, openAnswer));
            }
        }

        practiceResult.setQuestions(questions);

        return practiceResult;
    }

    @Override
    public PracticeEntity save(PracticeEntity entity) {
        return jpa.save(entity);
    }
}
