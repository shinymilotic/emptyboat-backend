package overcloud.blog.usecase.blog.get_tags_admin;

import java.util.List;

public interface IGetTagAdmin {
    List<TagResponse> getTagAdmin(int pageNumber, int itemsPerPage);
}
