package org.myproject.shoppingcart.service.category;

import org.myproject.shoppingcart.model.Category;

import java.util.List;

public interface iCategoryService {
    Category addCategory(Category category);
    Category updateCategory(Category category, long id);
    void deleteCategory(long id);
    Category getCategoryById(long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
}
