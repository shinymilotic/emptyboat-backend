package overcloud.blog.usecase.test.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import overcloud.blog.usecase.test.create_test.response.QuestionDeserializer;

@JsonDeserialize(using = QuestionDeserializer.class)
public interface Question {
    String getId();
    void setId(String id);
    
    String getQuestion();
    void setQuestion(String question);

    int getQuestionType();
    void setQuestionType(int questionType);
}
