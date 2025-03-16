package overcloud.blog.usecase.admin.tag.create_tag;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateTagResponse {
    private Iterable<String> tags;
}
