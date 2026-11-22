package com.example.tp3.services;


import com.example.tp3.dto.CategoryDto;
import com.example.tp3.models.Category;
import com.example.tp3.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepo;

    @Transactional
    public CategoryDto create(CategoryDto dto) {
        Category category = new Category(null, dto.name(), new java.util.ArrayList<>());
        Category saved = categoryRepo.save(category);
        return new CategoryDto(saved.getId(), saved.getName());
    }

    public CategoryDto getById(long id) {
        var category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return new CategoryDto(category.getId(), category.getName());
    }

    public List<CategoryDto> findAll() {
        return categoryRepo
                .findAll()
                .stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .toList();
    }

    @Transactional
    public CategoryDto update(long id, CategoryDto dto) {
        var category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        category.setName(dto.name());
        Category saved = categoryRepo.save(category);
        return new CategoryDto(saved.getId(), saved.getName());
    }

    public void delete(long id) {
        var category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (category != null) categoryRepo.delete(category);
    }
}
