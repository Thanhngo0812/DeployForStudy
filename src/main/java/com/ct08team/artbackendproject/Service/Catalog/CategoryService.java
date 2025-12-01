package com.ct08team.artbackendproject.Service.Catalog;

import com.ct08team.artbackendproject.DTO.CategoryDTO;
import com.ct08team.artbackendproject.Entity.product.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    List<CategoryDTO> getCategoriesParent();

    // Thay đổi duy nhất: int -> Long để đồng bộ với kiểu ID của Entity
    List<CategoryDTO> getAllCategoriesByParent(Long parentId);
    Category createCategory(String name, Long parentId);
    public Category updateCategory(Long id, String newName);
    public void deleteCategory(Long id);


    }