package rewe.at.codechallange.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rewe.at.codechallange.model.Product;
import rewe.at.codechallange.service.ProductService;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductService productService;

    public DataLoader(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
        loadProducts();
    }

    private void loadProducts() {
        Product product1 = Product
                .builder()
                .id(1L)
                .name("apple")
                .price(BigDecimal.valueOf(0.40))
                .build();

        Product product2 = Product
                .builder()
                .id(2L)
                .name("tomato")
                .price(BigDecimal.valueOf(0.20))
                .build();

        Product product3 = Product
                .builder()
                .id(3L)
                .name("banana")
                .price(BigDecimal.valueOf(0.30))
                .build();

        Product product4 = Product
                .builder()
                .id(4L)
                .name("avocado")
                .price(BigDecimal.valueOf(1.00))
                .build();

        productService.saveProduct(product1);
        productService.saveProduct(product2);
        productService.saveProduct(product3);
        productService.saveProduct(product4);
    }
}
