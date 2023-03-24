package rewe.at.codechallange.service;

import org.springframework.stereotype.Service;
import rewe.at.codechallange.model.Product;
import rewe.at.codechallange.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}