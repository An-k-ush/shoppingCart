package org.myproject.shoppingcart.service.product;

import org.myproject.shoppingcart.model.Product;
import org.myproject.shoppingcart.request.AddProductRequest;
import org.myproject.shoppingcart.request.ProductUpdateRequest;

import java.util.List;

public interface iProductService {
    Product addProduct(AddProductRequest product);
    Product updateProduct(ProductUpdateRequest product, Long productID);
    void deleteProduct(Long id);
    Product getProductById(Long id);
    List<Product> getALlProducts();
    List<Product> getProductsByCategoryName(String categoryName);
    List<Product> getProductsByBrand(String brandName);
    List<Product> getProductsByName(String productName);
    List<Product> getProductsByCategoryNameAndBrand(String categoryName, String brandName);
    List<Product> getProductByBrandAndName(String brandName, String productName);
    Long countProductsByBrandAndName(String brandName, String productName);
}
