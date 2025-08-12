package org.myproject.shoppingcart.request;

import lombok.Data;
import org.myproject.shoppingcart.model.Category;
import java.math.BigDecimal;


@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int stockQuantity;
    private String description;
    private Category category;
}
