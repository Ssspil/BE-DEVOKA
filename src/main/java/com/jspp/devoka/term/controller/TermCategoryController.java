package com.jspp.devoka.term.controller;

import com.jspp.devoka.term.dto.TermCategoryResponse;
import com.jspp.devoka.term.service.TermCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class TermCategoryController {

    private final TermCategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<TermCategoryResponse>> getCategories() {

        return new ResponseEntity<>(categoryService.getCategoryList(), HttpStatus.OK);
    }
}
