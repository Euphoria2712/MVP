package freshmart_service.service;

import freshmart_service.domain.Product;
import freshmart_service.dto.PriceResult;
import freshmart_service.dto.ProductRequest;
import freshmart_service.dto.ProductResponse;
import freshmart_service.exception.ResourceNotFoundException;
import freshmart_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreshmartServiceTest {

        @Mock
        private ProductRepository productRepository;

        @InjectMocks
        private FreshmartService freshmartService;

        private Product product() {
                return Product.builder()
                                .id("prod-1")
                                .nombre("Leche")
                                .categoria("Lácteos")
                                .unidad("litro")
                                .marca("Colun")
                                .precioBase(1000)
                                .imagenUrl("img.png")
                                .disponible(true)
                                .build();
        }

        private ProductRequest request() {
                ProductRequest request = new ProductRequest();
                request.setNombre("Leche");
                request.setCategoria("Lácteos");
                request.setUnidad("litro");
                request.setMarca("Colun");
                request.setPrecioBase(1000);
                request.setImagenUrl("img.png");
                request.setDisponible(true);
                return request;
        }

        @Test
        void findAllProducts_shouldReturnProducts() {
                when(productRepository.findAll())
                                .thenReturn(List.of(product()));

                List<ProductResponse> result = freshmartService.findAllProducts();

                assertEquals(1, result.size());
                assertEquals("Leche", result.get(0).getNombre());
        }

        @Test
        void findProductById_shouldReturnProduct() {
                when(productRepository.findById("prod-1"))
                                .thenReturn(Optional.of(product()));

                ProductResponse result = freshmartService.findProductById("prod-1");

                assertEquals("prod-1", result.getId());
                assertEquals("Leche", result.getNombre());
        }

        @Test
        void findProductById_shouldThrowWhenNotFound() {
                when(productRepository.findById("bad-id"))
                                .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> freshmartService.findProductById("bad-id"));
        }

        @Test
        void createProduct_shouldSaveProduct() {
                ProductRequest request = request();

                when(productRepository.save(any(Product.class)))
                                .thenAnswer(invocation -> {
                                        Product p = invocation.getArgument(0);
                                        p.setId("prod-1");
                                        return p;
                                });

                ProductResponse result = freshmartService.createProduct(request);

                assertEquals("prod-1", result.getId());
                assertEquals("Leche", result.getNombre());

                verify(productRepository).save(any(Product.class));
        }

        @Test
        void updateProduct_shouldUpdateProduct() {
                Product product = product();
                ProductRequest request = request();
                request.setNombre("Leche actualizada");

                when(productRepository.findById("prod-1"))
                                .thenReturn(Optional.of(product));

                when(productRepository.save(any(Product.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                ProductResponse result = freshmartService.updateProduct("prod-1", request);

                assertEquals("Leche actualizada", result.getNombre());
                assertEquals("Lácteos", result.getCategoria());

                verify(productRepository).save(product);
        }

        @Test
        void updateProduct_shouldThrowWhenNotFound() {
                when(productRepository.findById("bad-id"))
                                .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> freshmartService.updateProduct("bad-id", request()));

                verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        void deleteProduct_shouldDeleteProduct() {
                Product product = product();

                when(productRepository.findById("prod-1"))
                                .thenReturn(Optional.of(product));

                freshmartService.deleteProduct("prod-1");

                verify(productRepository).delete(product);
        }

        @Test
        void deleteProduct_shouldThrowWhenNotFound() {
                when(productRepository.findById("bad-id"))
                                .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> freshmartService.deleteProduct("bad-id"));

                verify(productRepository, never()).delete(any(Product.class));
        }

        @Test
        void searchPrices_shouldReturnPrices() {
                when(productRepository.findByNombreContainingIgnoreCase("leche"))
                                .thenReturn(List.of(product()));

                List<PriceResult> result = freshmartService.searchPrices("leche");

                assertEquals(1, result.size());
                assertEquals("Leche", result.get(0).getProducto());
                assertEquals(3, result.get(0).getPrecios().size());
                assertNotNull(result.get(0).getMasBarato());
        }

        @Test
        void searchPrices_shouldReturnEmptyWhenNoProducts() {
                when(productRepository.findByNombreContainingIgnoreCase("nada"))
                                .thenReturn(List.of());

                List<PriceResult> result = freshmartService.searchPrices("nada");

                assertTrue(result.isEmpty());
        }

        @Test
        void getPricesForProduct_shouldReturnPrices() {
                when(productRepository.findById("prod-1"))
                                .thenReturn(Optional.of(product()));

                PriceResult result = freshmartService.getPricesForProduct("prod-1");

                assertEquals("Leche", result.getProducto());
                assertEquals(3, result.getPrecios().size());
                assertNotNull(result.getMasBarato());
        }

        @Test
        void getPricesForProduct_shouldThrowWhenNotFound() {
                when(productRepository.findById("bad-id"))
                                .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> freshmartService.getPricesForProduct("bad-id"));
        }
}