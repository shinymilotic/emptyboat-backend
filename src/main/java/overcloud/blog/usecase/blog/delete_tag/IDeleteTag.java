package overcloud.blog.usecase.blog.delete_tag;

import java.util.UUID;

public interface IDeleteTag {
    Void deleteTag(String tagId);
}
