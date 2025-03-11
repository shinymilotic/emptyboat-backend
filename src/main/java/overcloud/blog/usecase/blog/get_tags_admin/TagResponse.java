package overcloud.blog.usecase.blog.get_tags_admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import overcloud.blog.usecase.blog.common.ITagResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {
    private String id;
    private String name;
}