package overcloud.blog.usecase.test.create_test.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import overcloud.blog.usecase.test.common.Question;
import overcloud.blog.usecase.test.common.QuestionType;

import java.io.IOException;

public class QuestionDeserializer extends JsonDeserializer<Question> {
    @Override
    public Question deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);
        Class<? extends Question> instanceClass = null;
        if (root.get("questionType").asInt() == QuestionType.CHOICE.getValue()) {
            instanceClass = ChoiceQuestion.class;
        } else if (root.get("questionType").asInt() == QuestionType.OPEN.getValue()) {
            instanceClass = OpenQuestion.class;
        }
        if (instanceClass == null) {
            return null;
        }
        return mapper.readValue(root.toString(), instanceClass);
    }
}
