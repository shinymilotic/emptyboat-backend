package overcloud.blog.usecase.test.update_test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdChoiceAnswer {
    private String answerId;
    private String answer;
    private Boolean truth;
    private Integer updateFlg;
}
