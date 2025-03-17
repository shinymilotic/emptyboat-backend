package overcloud.blog.usecase.admin.tag.get_tags;

public interface GetTagList {
    TagListResponse getTagList(int pageNumber, int itemsPerPage);
}
