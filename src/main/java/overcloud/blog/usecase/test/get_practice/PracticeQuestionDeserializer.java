package overcloud.blog.usecase.test.get_practice;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class PracticeQuestionDeserializer extends JsonDeserializer<PracticeQuestion> {
    @Override
    public PracticeQuestion deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = (ObjectNode) mapper.readTree(jp);
        Class<? extends PracticeQuestion> instanceClass = null;
        if (root.get("questionType").asInt() == 1) {
            instanceClass = PracticeChoiceQuestion.class;
        } else if (root.get("questionType").asInt() == 2) {
            instanceClass = PracticeEssayQuestion.class;
        }
        if (instanceClass == null) {
            return null;
        }
        return mapper.readValue(root.toString(), instanceClass);
    }
}
