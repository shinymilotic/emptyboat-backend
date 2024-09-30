package overcloud.blog.usecase.test.update_test;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

public class UpdQuestionDeserializer extends JsonDeserializer<UpdQuestion> {
    @Override
    public UpdQuestion deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);
        Class<? extends UpdQuestion> instanceClass = null;
        if (root.get("questionType").asInt() == 1) {
            instanceClass = UpdChoiceQuestion.class;
        } else if (root.get("questionType").asInt() == 2) {
            instanceClass = UpdOpenQuestion.class;
        }
        if (instanceClass == null) {
            return null;
        }
        return mapper.readValue(root.toString(), instanceClass);
    }
}
