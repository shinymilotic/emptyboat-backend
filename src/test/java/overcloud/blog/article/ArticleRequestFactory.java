package overcloud.blog.article;

import overcloud.blog.application.article.create_article.ArticleRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArticleRequestFactory {

    public static ArticleRequest createNormalArticleRequest() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("testa dsadcreaatdasdas earticle")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring", "Clean code"))
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestNullTitle() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring", "Clean code"))
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestEmptyTitle() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring", "Clean code"))
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestNotFitSizeTitle() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")
                .description("Empty title")
                .body("Nothing inside the body")
                .tagList(List.of("Spring", "Clean code"))
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestNullDescription() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("titlecss")
                .body("Nothing inside the body")
                .tagList(List.of("Spring", "Clean code"))
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestEmptyDescription() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("titlecss")
                .description("")
                .body("Nothing inside the body")
                .tagList(List.of("Spring", "Clean code"))
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestNotFitDescription() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("titlecss")
                .description("Nothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the bodyNothing inside the body")
                .body("Nothing inside the body")
                .tagList(List.of("Spring", "Clean code"))
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestNullBody() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("titlecss")
                .description("")
                .tagList(List.of("Spring", "Clean code"))
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestBlankBody() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("titlecss")
                .description("titlecsstitlecsstitlecsstitlecss")
                .body("")
                .tagList(List.of("Spring", "Clean code"))
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestNullTag() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("titlecss")
                .description("titlecsstitlecsstitlecsstitlecss")
                .body("titlecsstitlecsstitlecsstitlecss")
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestEmptyTag() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("titlecss")
                .description("titlecsstitlecsstitlecsstitlecss")
                .body("titlecsstitlecsstitlecsstitlecss")
                .tagList(List.of())
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestDuplicateTag() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("titlecss")
                .description("titlecsstitlecsstitlecsstitlecss")
                .body("titlecsstitlecsstitlecsstitlecss")
                .tagList(List.of("Spring", "Spring", "Clean code", "Clean code"))
                .build();

        return articleRequest;
    }

    public static ArticleRequest createNormalArticleRequestNotExistTag() {
        ArticleRequest articleRequest = ArticleRequest.builder()
                .title("titlecss")
                .description("titlecsstitlecsstitlecsstitlecss")
                .body("titlecsstitlecsstitlecsstitlecss")
                .tagList(new LinkedList<String>(Arrays.asList("NoExistTag1", "NoExistTag2")))
                .build();

        return articleRequest;
    }
}
