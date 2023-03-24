package rewe.at.codechallange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rewe.at.codechallange.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}