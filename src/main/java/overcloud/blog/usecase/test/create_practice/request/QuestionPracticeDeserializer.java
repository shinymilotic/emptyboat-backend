package overcloud.blog.usecase.test.create_practice.request;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import overcloud.blog.usecase.test.common.QuestionType;
import java.io.IOException;

public class QuestionPracticeDeserializer extends JsonDeserializer<IQuestionPractice> {
    @Override
    public IQuestionPractice deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);
        Class<? extends IQuestionPractice> instanceClass = null;
        if (root.get("questionType").asInt() == QuestionType.CHOICE.getValue()) {
            instanceClass = PracticeChoiceQuestion.class;
        } else if (root.get("questionType").asInt() == QuestionType.OPEN.getValue()) {
            instanceClass = PracticeOpenQuestion.class;
        }
        if (instanceClass == null) {
            return null;
        }

        var vs = mapper.readValue(root.toString(), instanceClass);
        return vs;
    }
}