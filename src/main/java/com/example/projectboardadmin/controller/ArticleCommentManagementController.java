package com.example.projectboardadmin.controller;

import com.example.projectboardadmin.dto.response.ArticleCommentResponse;
import com.example.projectboardadmin.service.ArticleCommentManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/management/article-comments")
public class ArticleCommentManagementController {

    private final ArticleCommentManagementService articleCommentManagementService;

    @GetMapping
    public String articleComments(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        model.addAttribute("comments",articleCommentManagementService
                .getArticleComments().stream()
                .map(ArticleCommentResponse::of)
                .toList()
        );
        return "management/article-comments";
    }

    @ResponseBody
    @GetMapping("/{articleCommentId}")
    public ArticleCommentResponse articleComment(@PathVariable Long articleCommentId){
        return ArticleCommentResponse.of(
                articleCommentManagementService.getArticleComment(articleCommentId)
        );
    }

    @PostMapping("/{articleCommentId}")
    public String deleteArticleComment(@PathVariable Long articleCommentId){
        articleCommentManagementService.deleteArticleComment(articleCommentId);
        return "redirect:/management/article-comments";
    }

}
