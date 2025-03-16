package overcloud.blog.usecase.blog.get_tags;

import overcloud.blog.usecase.blog.common.TagResponse;
import java.util.List;

public interface GetTagsService {
    List<TagResponse> getTags(Boolean following);
}
