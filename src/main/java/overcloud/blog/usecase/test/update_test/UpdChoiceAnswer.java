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
    private UpdateValue<String> answer;
    private UpdateValue<Boolean> truth;
    private Boolean updateFlg;
}
