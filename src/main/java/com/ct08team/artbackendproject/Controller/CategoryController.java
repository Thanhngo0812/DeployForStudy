package com.ct08team.artbackendproject.Controller;

import java.util.List;
import java.util.Map;

import com.ct08team.artbackendproject.Entity.product.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ct08team.artbackendproject.DTO.CategoryDTO;
import com.ct08team.artbackendproject.Service.Catalog.CategoryService;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
        private CategoryService categoryService;

        public CategoryController(CategoryService categoryService) {
                this.categoryService = categoryService;
        }

        /**
         * GET /api/v1/categories
         * Lấy tất cả danh mục
         */
        @GetMapping
        public ResponseEntity<List<CategoryDTO>> getAllCategories() {
                // List<CategoryDto> categories = categoryService.findAllCategories();
                // Trả về HTTP 200 OK
                // return ResponseEntity.ok(categories);
                List<CategoryDTO> categories = categoryService.getAllCategories();
                return ResponseEntity.ok(categories);
        }

        // GET /api/v1/categories/parents
        @GetMapping("/parents")
        public ResponseEntity<List<CategoryDTO>> getCategoriesParent() {
                // List<CategoryDto> categories = categoryService.findAllCategories();
                // Trả về HTTP 200 OK
                // return ResponseEntity.ok(categories);
                List<CategoryDTO> categories = categoryService.getCategoriesParent();
                return ResponseEntity.ok(categories);
        }

        // GET /api/v1/categories/{parentId}/children
        @GetMapping("/{parentId}/children")
        public ResponseEntity<List<CategoryDTO>> getCategoriesByParent(@PathVariable Long parentId) {
                // List<CategoryDto> categories = categoryService.findAllCategories();
                // Trả về HTTP 200 OK
                // return ResponseEntity.ok(categories);
                List<CategoryDTO> categories = categoryService.getAllCategoriesByParent(parentId);
                return ResponseEntity.ok(categories);
        }


    // 1. Tạo danh mục mới (Cha hoặc Con)
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Map<String, Object> payload) {
        try {
            String name = (String) payload.get("name");
            Long parentId = payload.get("parentId") != null ? ((Number) payload.get("parentId")).longValue() : null;

            Category category = categoryService.createCategory(name, parentId);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 2. Cập nhật tên danh mục
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String newName = payload.get("name");
            Category updated = categoryService.updateCategory(id, newName);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 3. Xóa danh mục
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(Map.of("message", "Xóa danh mục thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
