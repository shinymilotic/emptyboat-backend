package overcloud.blog.application.article.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;
import overcloud.blog.domain.article.favorite.FavoriteEntity;

import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, UUID> {

    @Modifying
    @Query("DELETE FROM FavoriteEntity f WHERE f.article.slug = :slug")
    void deleteBySlug(String slug);
}
