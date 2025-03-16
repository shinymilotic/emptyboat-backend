package overcloud.blog.usecase.admin.tag.get_tags;

import java.util.List;

public interface GetTagAdmin {
    List<TagResponse> getTagAdmin(int pageNumber, int itemsPerPage);
}
