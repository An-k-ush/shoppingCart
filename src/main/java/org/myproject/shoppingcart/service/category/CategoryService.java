package org.myproject.shoppingcart.service.category;

import lombok.RequiredArgsConstructor;
import org.myproject.shoppingcart.exceptions.AlreadyExistsException;
import org.myproject.shoppingcart.exceptions.CategoryNotFoundException;
import org.myproject.shoppingcart.model.Category;
import org.myproject.shoppingcart.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryService implements iCategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new AlreadyExistsException("Category with name " + category.getName() + " already exists"));
    }
    @Override
    public void deleteCategory(long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new CategoryNotFoundException("Category with id " + id + " not found");
                });
    }
    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }
    @Override
    public Category updateCategory(Category category, long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    return categoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
