package com.example.projectboardadmin.controller;

import com.example.projectboardadmin.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 댓글 관리")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleCommentManagementController.class)
@ActiveProfiles("testdb")
class ArticleCommentManagementControllerTest {

    private final MockMvc mvc;

    public ArticleCommentManagementControllerTest(
            @Autowired  MockMvc mvc
    ) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 댓글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleCommentManagementView_thenReturnsArticleCommentManagementView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/management/article-comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))  // 호환되는 타입까지 매칭 : text/html;charset=UTF-8
                .andExpect(view().name("management/articleComments"))
        ;
    }


}