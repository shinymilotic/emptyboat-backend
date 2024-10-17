package overcloud.blog.usecase.test.update_test;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import overcloud.blog.usecase.test.common.QuestionType;

import java.io.IOException;

public class UpdQuestionDeserializer extends JsonDeserializer<UpdQuestion> {
    @Override
    public UpdQuestion deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);
        Class<? extends UpdQuestion> instanceClass = null;
        if (root.get("questionType").asInt() == QuestionType.CHOICE.getValue()) {
            instanceClass = UpdChoiceQuestion.class;
        } else if (root.get("questionType").asInt() == QuestionType.OPEN.getValue()) {
            instanceClass = UpdOpenQuestion.class;
        }
        if (instanceClass == null) {
            return null;
        }
        return mapper.readValue(root.toString(), instanceClass);
    }
}
