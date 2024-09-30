package overcloud.blog.usecase.test.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class QuestionDeserializer extends JsonDeserializer<Question> {
    @Override
    public Question deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);
        Class<? extends Question> instanceClass = null;
        if (root.get("questionType").asInt() == 1) {
            instanceClass = ChoiceQuestion.class;
        } else if (root.get("questionType").asInt() == 2) {
            instanceClass = OpenQuestion.class;
        }
        if (instanceClass == null) {
            return null;
        }
        return mapper.readValue(root.toString(), instanceClass);
    }
}
