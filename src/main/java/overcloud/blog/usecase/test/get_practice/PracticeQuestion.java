package overcloud.blog.usecase.test.get_practice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.UUID;

@JsonDeserialize(using = PracticeQuestionDeserializer.class)
public interface PracticeQuestion {
    UUID getId();

    void setId(UUID id);

    String getQuestion();

    void setQuestion(String question);

    int getQuestionType();

    void setQuestionType(int questionType);
}
