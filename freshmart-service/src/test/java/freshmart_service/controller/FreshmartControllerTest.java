package freshmart_service.controller;

import freshmart_service.dto.PriceResult;
import freshmart_service.dto.ProductRequest;
import freshmart_service.dto.ProductResponse;
import freshmart_service.service.FreshmartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreshmartControllerTest {

    @Mock
    private FreshmartService freshmartService;

    @InjectMocks
    private FreshmartController controller;

    private ProductResponse productResponse() {
        return ProductResponse.builder()
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

    private ProductRequest productRequest() {
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
    void getAllProducts_shouldReturnProducts() {
        when(freshmartService.findAllProducts())
                .thenReturn(List.of(productResponse()));

        ResponseEntity<List<ProductResponse>> response = controller.getAllProducts();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());

        verify(freshmartService).findAllProducts();
    }

    @Test
    void getProductById_shouldReturnProduct() {
        when(freshmartService.findProductById("prod-1"))
                .thenReturn(productResponse());

        ResponseEntity<ProductResponse> response = controller.getProductById("prod-1");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("prod-1", response.getBody().getId());

        verify(freshmartService).findProductById("prod-1");
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() {
        ProductRequest request = productRequest();

        when(freshmartService.createProduct(request))
                .thenReturn(productResponse());

        ResponseEntity<ProductResponse> response = controller.createProduct(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Leche", response.getBody().getNombre());

        verify(freshmartService).createProduct(request);
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() {
        ProductRequest request = productRequest();

        when(freshmartService.updateProduct("prod-1", request))
                .thenReturn(productResponse());

        ResponseEntity<ProductResponse> response = controller.updateProduct("prod-1", request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("prod-1", response.getBody().getId());

        verify(freshmartService).updateProduct("prod-1", request);
    }

    @Test
    void deleteProduct_shouldReturnNoContent() {
        ResponseEntity<Void> response = controller.deleteProduct("prod-1");

        assertEquals(204, response.getStatusCode().value());

        verify(freshmartService).deleteProduct("prod-1");
    }

    @Test
    void getPrices_shouldReturnPrices() {
        PriceResult priceResult = PriceResult.builder()
                .producto("Leche")
                .unidad("litro")
                .build();

        when(freshmartService.searchPrices("leche"))
                .thenReturn(List.of(priceResult));

        ResponseEntity<List<PriceResult>> response = controller.getPrices("leche");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());

        verify(freshmartService).searchPrices("leche");
    }

    @Test
    void getPriceById_shouldReturnPrice() {
        PriceResult priceResult = PriceResult.builder()
                .producto("Leche")
                .unidad("litro")
                .build();

        when(freshmartService.getPricesForProduct("prod-1"))
                .thenReturn(priceResult);

        ResponseEntity<PriceResult> response = controller.getPriceById("prod-1");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Leche", response.getBody().getProducto());

        verify(freshmartService).getPricesForProduct("prod-1");
    }
}