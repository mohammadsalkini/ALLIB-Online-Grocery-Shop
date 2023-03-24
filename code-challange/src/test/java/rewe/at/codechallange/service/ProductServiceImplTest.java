package rewe.at.codechallange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rewe.at.codechallange.repository.ProductRepository;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        productService.getAllProducts();
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById() {
        productService.getProductById(1L);
        verify(productRepository, times(1)).findById(1L);
    }
}