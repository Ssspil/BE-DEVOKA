package com.jspp.devoka.term.repository;

import com.jspp.devoka.term.damain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TermCategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryId(String categoryId);
}
