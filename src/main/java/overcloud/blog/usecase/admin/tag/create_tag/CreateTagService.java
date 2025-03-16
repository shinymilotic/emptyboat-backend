package overcloud.blog.usecase.admin.tag.create_tag;

import java.util.List;

public interface CreateTagService {
    List<String> createTags(CreateTagRequest createTagRequest);
}
