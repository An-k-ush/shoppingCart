package org.myproject.shoppingcart.repository;
import org.myproject.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductByCategoryName(String category);
    List<Product> findProductByCategoryNameAndBrand(String categoryName, String brandName);
    List<Product> findProductByName(String productName);
    List<Product> findProductByBrand(String brandName);
    List<Product> findProductByBrandAndName(String brandName, String productName);
    Long countProductByBrandAndName(String brandName, String productName);
}
