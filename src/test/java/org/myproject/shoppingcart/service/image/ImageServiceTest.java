package org.myproject.shoppingcart.service.image;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.myproject.shoppingcart.exceptions.ProductNotFoundException;
import org.myproject.shoppingcart.model.Image;
import org.myproject.shoppingcart.model.Product;
import org.myproject.shoppingcart.repository.ImageRepository;
import org.myproject.shoppingcart.repository.ProductRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private MultipartFile multipartFile;
    
    @InjectMocks
    private ImageService imageService;
    
    private Product testProduct;
    private Image testImage;
    private Long productId = 1L;
    
    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(productId);
        testProduct.setName("Test Product");
        
        testImage = new Image();
        testImage.setId(1L);
        testImage.setImgName("test.jpg");
        testImage.setImgType("image/jpeg");
        testImage.setProduct(testProduct);
    }
    
    @Test
    void saveImage_ShouldReturnSavedImage_WhenProductExists() throws IOException {
        // Given
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);
        
        // When
        Image result = imageService.saveImage(multipartFile, productId);
        
        // Then
        assertNotNull(result);
        assertEquals(testImage.getId(), result.getId());
        assertEquals(testImage.getImgName(), result.getImgName());
        assertEquals(testImage.getImgType(), result.getImgType());
        assertEquals(testProduct, result.getProduct());
        
        verify(productRepository).findById(productId);
        verify(imageRepository).save(any(Image.class));
    }
    
    @Test
    void saveImage_ShouldThrowProductNotFoundException_WhenProductDoesNotExist() {
        // Given
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        // When & Then
        ProductNotFoundException exception = assertThrows(
            ProductNotFoundException.class, 
            () -> imageService.saveImage(multipartFile, productId)
        );
        
        assertEquals("Product with id " + productId + " not found", exception.getMessage());
        verify(productRepository).findById(productId);
        verify(imageRepository, never()).save(any(Image.class));
    }
    
    @Test
    void saveImage_ShouldThrowRuntimeException_WhenIOExceptionOccurs() throws IOException {
        // Given
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getBytes()).thenThrow(new IOException("File read error"));
        
        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> imageService.saveImage(multipartFile, productId)
        );
        
        assertEquals("File read error", exception.getMessage());
        verify(productRepository).findById(productId);
        verify(imageRepository, never()).save(any(Image.class));
    }
}