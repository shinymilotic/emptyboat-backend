package overcloud.blog.usecase.admin.tag.update_tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTagRequest {
    private String tagId;
    private String updateTagName;
}
