package overcloud.blog.usecase.test.update_test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = UpdQuestionDeserializer.class)
public interface UpdQuestion {
    String getId();
    void setId(String id);
    
    String getQuestion();
    void setQuestion(String question);

    Integer getQuestionType();
    void setQuestionType(Integer questionType);

    void setUpdateFlg(Integer updateFlg);
    Integer getUpdateFlg();
}
