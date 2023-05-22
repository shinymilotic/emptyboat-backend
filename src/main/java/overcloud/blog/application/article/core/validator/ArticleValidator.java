package overcloud.blog.application.article.core.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class ArticleValidator {

    private final ArticleRepository articleRepository;

    private final TagRepository tagRepository;

    public ArticleValidator(ArticleRepository articleRepository,
                            TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
    }


    public Optional<ApiErrorDetail> validateBodyNotBlank(String body) {
        if(!StringUtils.hasText(body)) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    public Optional<ApiErrorDetail> validateDescriptionLength(String description) {
        if(!StringUtils.hasText(description)) {
            return Optional.empty();
        }

        if(description.length() < 1 || description.length() > 100) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    public Optional<ApiErrorDetail> validateTagList(Collection<String> tagList) {
        if(tagList == null || tagList.isEmpty()) {
            return Optional.empty();
        }

        List<TagEntity> tagEntities = tagRepository.checkAllTagsExistDB(tagList);
        if(tagEntities.size() != tagList.size()) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    public Optional<ApiErrorDetail> validateTitleLength(String title) {
        if(!StringUtils.hasText(title)) {
            return Optional.empty();
        }

        if(title.length() < 1 || title.length() > 60) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    public Optional<ApiErrorDetail> validateTitleExistDB(String title) {
        List<ArticleEntity> articleEntities = articleRepository.findByTitle(title);
        if(!articleEntities.isEmpty()) {
            return Optional.empty();
        }

        return Optional.empty();
    }
}
