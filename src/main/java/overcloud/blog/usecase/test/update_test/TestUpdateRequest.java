package overcloud.blog.usecase.test.update_test;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TestUpdateRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestUpdateRequest {
    private UpdateValue<String> title;
    private UpdateValue<String> description;
    private List<UpdQuestion> questions;
    private Integer updateFlg;
}