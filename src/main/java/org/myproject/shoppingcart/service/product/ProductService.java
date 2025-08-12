package org.myproject.shoppingcart.service.product;

import lombok.RequiredArgsConstructor;
import org.myproject.shoppingcart.exceptions.ProductNotFoundException;
import org.myproject.shoppingcart.model.Category;
import org.myproject.shoppingcart.model.Product;
import org.myproject.shoppingcart.repository.CategoryRepository;
import org.myproject.shoppingcart.repository.ProductRepository;
import org.myproject.shoppingcart.request.AddProductRequest;
import org.myproject.shoppingcart.request.ProductUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class ProductService implements iProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest product) {

        Category category = Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(product.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        product.setCategory(category);
        return productRepository.save(createProduct(product, category));
    }

    private Product createProduct(AddProductRequest product, Category category) {
        return new Product(
                product.getName(),
                product.getBrand(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getDescription(),
                category
        );
    }

    @Override
    public Product updateProduct(ProductUpdateRequest product, Long productID) {
        return productRepository.findById(productID)
                .map(existingProduct -> updateExistingProduct(existingProduct, product))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productID));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest required) {
        existingProduct.setName(required.getName());
        existingProduct.setBrand(required.getBrand());
        existingProduct.setPrice(required.getPrice());
        existingProduct.setStockQuantity(required.getStockQuantity());
        existingProduct.setDescription(required.getDescription());
        Category category = Optional.ofNullable(categoryRepository.findByName(required.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(required.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ProductNotFoundException("Product not found");
        });
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getALlProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategoryName(String categoryName) {
        return productRepository.findProductByCategoryName(categoryName);
    }

    @Override
    public List<Product> getProductsByBrand(String brandName) {
        return productRepository.findProductByBrand(brandName);
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return productRepository.findProductByName(productName);
    }

    @Override
    public List<Product> getProductsByCategoryNameAndBrand(String categoryName, String brandName) {
        return productRepository.findProductByCategoryNameAndBrand(categoryName, brandName);
    }
    @Override
    public List<Product> getProductByBrandAndName(String brandName, String productName) {
        return productRepository.findProductByBrandAndName(brandName, productName);
    }

    @Override
    public Long countProductsByBrandAndName(String brandName, String productName) {
        return productRepository.countProductByBrandAndName(brandName, productName);
    }
}
