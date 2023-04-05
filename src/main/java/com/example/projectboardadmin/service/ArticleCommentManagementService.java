package com.example.projectboardadmin.service;

import com.example.projectboardadmin.dto.ArticleCommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleCommentManagementService {

    public List<ArticleCommentDto> getArticleComments() {
        return List.of();
    }

    public ArticleCommentDto getArticleComment(Long articleCommentId) {
        return null;
    }

    public void deleteArticleComment(Long articleCommentId) {

    }
}
