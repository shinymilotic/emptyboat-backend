package overcloud.blog.usecase.blog.create_tag;

import java.util.List;

public interface CreateTagService {
    List<String> createTags(CreateTagRequest createTagRequest);
}
