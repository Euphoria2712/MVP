package freshmart_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import freshmart_service.domain.Product;
import java.util.List;

public interface ProductRepository
        extends MongoRepository<Product, String> {

    List<Product> findByNombreContainingIgnoreCase(String nombre);
    List<Product> findByCategoria(String categoria);
}