package overcloud.blog.usecase.blog.get_tags_admin;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IGetTagsAdmin {
    List<TagResponse> getTagsAdmin(int pageNumber, int itemsPerPage);
}
