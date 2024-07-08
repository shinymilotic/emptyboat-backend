package overcloud.blog.usecase.test.get_list_test;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TestListRecord {
    private UUID id;
    private String title;
    private String description;
}
