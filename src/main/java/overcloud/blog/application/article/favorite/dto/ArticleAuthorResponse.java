package overcloud.blog.application.article.favorite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleAuthorResponse {
    @JsonProperty("username")
    private String username;
    @JsonProperty("bio")
    private String bio;
    @JsonProperty("image")
    private String image;
    @JsonProperty("following")
    private boolean following;

    @JsonProperty("email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
