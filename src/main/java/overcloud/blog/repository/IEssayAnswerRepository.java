package overcloud.blog.repository;

import java.util.List;

import overcloud.blog.entity.EssayAnswerEntity;

public interface IEssayAnswerRepository {
    void saveAll(List<EssayAnswerEntity> essayAnswerEntities);
}
