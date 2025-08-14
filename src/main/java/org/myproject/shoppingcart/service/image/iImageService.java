package org.myproject.shoppingcart.service.image;

import org.myproject.shoppingcart.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface iImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    Image saveImage(MultipartFile imageFile, Long productId);
    void updateImage(MultipartFile imageFile, Long imageId);
}
