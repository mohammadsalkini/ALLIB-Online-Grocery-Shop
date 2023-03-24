package rewe.at.codechallange.service;

import rewe.at.codechallange.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();

    Optional<Product> getProductById(Long productId);

    void saveProduct(Product product);
}