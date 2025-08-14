package org.myproject.shoppingcart.service.image;


import lombok.RequiredArgsConstructor;
import org.myproject.shoppingcart.exceptions.ImageNotFoundException;
import org.myproject.shoppingcart.model.Image;
import org.myproject.shoppingcart.repository.ImageRepository;
import org.myproject.shoppingcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ImageService implements iImageService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image with id " + id + " not found"));
    }
    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository::delete, () -> {
                    throw new ImageNotFoundException("Image with id " + id + " not found");
                });
    }
    @Override
    public Image saveImage(MultipartFile imageFile, Long productId) {
        return null;
    }
    @Override
    public void updateImage(MultipartFile imageFile, Long imageId) {
        Image img = getImageById(imageId);
        try {
            img.setImgName(imageFile.getOriginalFilename());
            img.setImgType(imageFile.getContentType());
            img.setImageBlob(new SerialBlob(imageFile.getBytes()));
            imageRepository.save(img);
        }
        catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}