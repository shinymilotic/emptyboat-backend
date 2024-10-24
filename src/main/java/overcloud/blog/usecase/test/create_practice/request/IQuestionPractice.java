package overcloud.blog.usecase.test.create_practice.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import overcloud.blog.usecase.test.create_test.response.QuestionDeserializer;

@JsonDeserialize(using = QuestionPracticeDeserializer.class)
public interface IQuestionPractice {
    String getQuestionId();
    void setQuestionId(String questionId);

    Integer getQuestionType();
    void setQuestionType(Integer questionType);
}
