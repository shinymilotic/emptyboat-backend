package overcloud.blog.usecase.admin.tag.get_tags;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagListResponse {
    private List<TagResponse> tags;
    private Integer tagCount;
}
