package overcloud.blog.application.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import overcloud.blog.application.article.ArticleEntity;
import overcloud.blog.application.article.favorite.FavoriteEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "public")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email")
    @NotNull
    @Email
    private String email;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "bio")
    private String bio;

    @Column(name = "image")
    private String image;

    @ManyToMany
    @JoinTable(
            name = "follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followee_id")
    )
    private Set<UserEntity> followee;

    @ManyToMany(mappedBy = "followee")
    private Set<UserEntity> follower;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FavoriteEntity> favorites;

    @OneToMany(mappedBy = "author")
    private List<ArticleEntity> authorize;

    public List<ArticleEntity> getAuthorize() {
        return authorize;
    }

    public void setAuthorize(List<ArticleEntity> authorize) {
        this.authorize = authorize;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<UserEntity> getFollower() {
        return follower;
    }

    public void setFollower(Set<UserEntity> follower) {
        this.follower = follower;
    }

    public Set<UserEntity> getFollowee() {
        return followee;
    }

    public void setFollowee(Set<UserEntity> followee) {
        this.followee = followee;
    }

    public List<FavoriteEntity> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<FavoriteEntity> favorites) {
        this.favorites = favorites;
    }

    @Override
    public boolean equals(Object o) {
        UserEntity that = (UserEntity) o;

        if(that == null) {
            return false;
        }

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
