package overcloud.blog.application.tag.create_tag;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateTagRequest {

    @JsonProperty("tags")
    private Iterable<String> tagList;

    public Iterable<String> getTagList() {
        return tagList;
    }

    public void setTagList(Iterable<String> tagList) {
        this.tagList = tagList;
    }
}
