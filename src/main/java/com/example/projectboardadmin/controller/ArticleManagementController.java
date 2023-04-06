package com.example.projectboardadmin.controller;

import com.example.projectboardadmin.dto.response.ArticleResponse;
import com.example.projectboardadmin.service.ArticleManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/management/articles")
public class ArticleManagementController {

    private final ArticleManagementService articleManagementService;

    @GetMapping
    public String articles(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {

        model.addAttribute("articles", articleManagementService
                .getArticles().stream()
                .map(ArticleResponse::withoutContent)
                .toList()
        );

        return "management/articles";
    }

    @ResponseBody
    @GetMapping("/{articleId}")
    public ArticleResponse article(@PathVariable Long articleId){

        return ArticleResponse.withContent(
                articleManagementService.getArticle(articleId)
        );
    }

    @PostMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId){
        articleManagementService.deleteArticle(articleId);

        return "redirect:/management/articles";
    }

}
