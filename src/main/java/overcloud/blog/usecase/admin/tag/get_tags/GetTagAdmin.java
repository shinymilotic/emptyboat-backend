package overcloud.blog.usecase.admin.tag.get_tags;

import java.util.List;

public interface GetTagAdmin {
    TagListResponse getTagAdmin(int pageNumber, int itemsPerPage);
}
