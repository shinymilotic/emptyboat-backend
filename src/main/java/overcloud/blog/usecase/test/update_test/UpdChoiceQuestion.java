package overcloud.blog.usecase.test.update_test;

import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(as = UpdChoiceQuestion.class)
public class UpdChoiceQuestion implements UpdQuestion {
    private String id;
    private String question;
    private Integer questionType;
    private List<UpdChoiceAnswer> answers;
    private Integer updateFlg;
}
