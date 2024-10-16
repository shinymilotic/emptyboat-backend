package overcloud.blog.usecase.test.update_test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(as = UpdOpenQuestion.class)
public class UpdOpenQuestion implements UpdQuestion {
    private String id;
    private String question;
    private Integer questionType;
    private Integer updateFlg;
}
