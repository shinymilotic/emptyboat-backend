package overcloud.blog.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import overcloud.blog.domain.user.follow.FollowEntity;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "public")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToMany(mappedBy = "follower")
    private Set<FollowEntity> follower;

    @OneToMany(mappedBy = "followee")
    private Set<FollowEntity> followee;

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

    public Set<FollowEntity> getFollower() {
        return follower;
    }

    public void setFollower(Set<FollowEntity> follower) {
        this.follower = follower;
    }

    public Set<FollowEntity> getFollowee() {
        return followee;
    }

    public void setFollowee(Set<FollowEntity> followee) {
        this.followee = followee;
    }
}
