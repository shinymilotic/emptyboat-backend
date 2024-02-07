package overcloud.blog.repository;

import overcloud.blog.entity.QuestionEntity;

import java.util.List;

public interface IQuestionRepository {
    QuestionEntity save(QuestionEntity entity);

    List<QuestionEntity> saveAll(List<QuestionEntity> entityList);
}
