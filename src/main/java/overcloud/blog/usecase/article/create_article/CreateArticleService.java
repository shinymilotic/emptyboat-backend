package overcloud.blog.usecase.article.create_article;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.repository.jparepository.JpaArticleTagRepository;
import overcloud.blog.usecase.article.core.AuthorResponse;
import overcloud.blog.usecase.article.core.utils.ArticleUtils;
import overcloud.blog.usecase.tag.core.TagError;
import overcloud.blog.repository.jparepository.JpaTagRepository;
import overcloud.blog.usecase.user.core.UserError;
import overcloud.blog.entity.ArticleTagId;
import overcloud.blog.entity.ArticleTag;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.TagEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CreateArticleService {

    private final SpringAuthenticationService authenticationService;

    private final JpaTagRepository tagRepository;

    private final JpaArticleRepository articleRepository;

    private final ObjectsValidator<ArticleRequest> validator;

    private final JpaArticleTagRepository articleTagRepository;

    private KafkaTemplate<String, String> kafkaTemplate;


    public CreateArticleService(SpringAuthenticationService authenticationService,
                                JpaTagRepository tagRepository,
                                JpaArticleRepository articleRepository,
                                ObjectsValidator<ArticleRequest> validator,
                                JpaArticleTagRepository articleTagRepository,
                                KafkaTemplate<String, String> kafkaTemplate) {
        this.authenticationService = authenticationService;
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
        this.validator = validator;
        this.articleTagRepository = articleTagRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public ArticleResponse createArticle(ArticleRequest articleRequest) throws JsonProcessingException {
        List<String> distinctTags = filterDistinctTags(articleRequest.getTagList());
        articleRequest.setTagList(distinctTags);
        Optional<ApiError> apiError = validator.validate(articleRequest);

        if(apiError.isPresent()) {
            throw new InvalidDataException(apiError.get());
        }

        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        List<TagEntity> tagEntities = tagRepository.findByTagName(articleRequest.getTagList());
        if(distinctTags.size() > tagEntities.size()) {
            throw new InvalidDataException(ApiError.from(TagError.TAG_NO_EXISTS));
        }

        ArticleEntity articleEntity = initArticleEntity(articleRequest, currentUser, tagEntities);
        ArticleEntity savedArticleEntity = articleRepository.save(articleEntity);
        List<ArticleTag> articleTags = toArticleTag(tagEntities, savedArticleEntity);
        articleTagRepository.saveAll(articleTags);
        ArticleResponse response = toCreateArticleResponse(savedArticleEntity);
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(response);
        sendMessage(message);
        return response;
    }

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("notifications", message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
    }

    private List<String> filterDistinctTags(List<String> tags) {
        if(tags != null) {
            tags = tags.stream().distinct().toList();
        } else {
            tags = new ArrayList<>();
        }

        return tags;
    }

    public ArticleEntity initArticleEntity(ArticleRequest articleRequest, UserEntity author, List<TagEntity> tagEntities) {
        String title = articleRequest.getTitle();
        String body = articleRequest.getBody();
        String description = articleRequest.getDescription();
        String slug = ArticleUtils.toSlug(title);
        LocalDateTime now = LocalDateTime.now();

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setId(UuidCreator.getTimeOrderedEpoch());
        articleEntity.setAuthor(author);
        articleEntity.setBody(body);
        articleEntity.setDescription(description);
        articleEntity.setSlug(slug);
        articleEntity.setTitle(title);
        articleEntity.setCreatedAt(now);
        articleEntity.setUpdatedAt(now);

        return articleEntity;
    }

    public List<ArticleTag> toArticleTag(List<TagEntity> tagEntities, ArticleEntity articleEntity) {
        return tagEntities.stream()
                .map(tagEntity -> {
                    ArticleTagId articleTagId = new ArticleTagId();
                    articleTagId.setArticleId(articleEntity.getId());
                    articleTagId.setTagId(tagEntity.getId());
                    return ArticleTag.builder()
                            .id(articleTagId)
                            .tag(tagEntity)
                            .article(articleEntity)
                            .build();})
                .collect(Collectors.toList());
    }

    public ArticleResponse toCreateArticleResponse(ArticleEntity articleEntity) {
        return ArticleResponse.builder()
                .id(articleEntity.getId().toString())
                .title(articleEntity.getTitle())
                .body(articleEntity.getBody())
                .description(articleEntity.getDescription())
                .tagList(articleEntity.getTagNameList())
                .author(toAuthorResponse(articleEntity.getAuthor()))
                .slug(articleEntity.getSlug())
                .createdAt(articleEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")))
                .updatedAt(articleEntity.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm")))
                .favorited(false)
                .build();
    }

    private AuthorResponse toAuthorResponse(UserEntity userEntity) {
        return AuthorResponse.builder()
              .bio(userEntity.getBio())
              .username(userEntity.getUsername())
              .image(userEntity.getImage())
              .build();
    }
}
