package freshmart_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import freshmart_service.domain.Product;
import freshmart_service.dto.PriceResult;
import freshmart_service.dto.ProductRequest;
import freshmart_service.dto.ProductResponse;
import freshmart_service.dto.StorePrice;
import freshmart_service.exception.ResourceNotFoundException;
import freshmart_service.repository.ProductRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreshmartService {

    private final ProductRepository productRepository;

    private static final Map<String, Double> VARIACIONES = Map.of(
            "simermart", 1.0,
            "frescopro", 1.08,
            "megacanasta", 0.94
    );

    private static final Map<String, String> NOMBRES_TIENDA = Map.of(
            "simermart", "SimerMart",
            "frescopro", "FrescoPro",
            "megacanasta", "MegaCanasta"
    );

    public List<ProductResponse> findAllProducts() {
        log.info("Listando todos los productos");

        return productRepository.findAll()
                .stream()
                .map(this::toProductResponse)
                .toList();
    }

    public ProductResponse findProductById(String id) {
        log.info("Buscando producto id={}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado id={}", id);
                    return new ResourceNotFoundException("Producto no encontrado");
                });

        return toProductResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creando producto nombre={}", request.getNombre());

        Product product = Product.builder()
                .nombre(request.getNombre())
                .categoria(request.getCategoria())
                .unidad(request.getUnidad())
                .marca(request.getMarca())
                .precioBase(request.getPrecioBase())
                .imagenUrl(request.getImagenUrl())
                .disponible(request.getDisponible())
                .build();

        Product saved = productRepository.save(product);

        log.info("Producto creado correctamente id={}", saved.getId());

        return toProductResponse(saved);
    }

    public ProductResponse updateProduct(String id, ProductRequest request) {
        log.info("Actualizando producto id={}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Producto no encontrado id={}", id);
                    return new ResourceNotFoundException("Producto no encontrado");
                });

        product.setNombre(request.getNombre());
        product.setCategoria(request.getCategoria());
        product.setUnidad(request.getUnidad());
        product.setMarca(request.getMarca());
        product.setPrecioBase(request.getPrecioBase());
        product.setImagenUrl(request.getImagenUrl());
        product.setDisponible(request.getDisponible());

        Product updated = productRepository.save(product);

        log.info("Producto actualizado correctamente id={}", id);

        return toProductResponse(updated);
    }

    public void deleteProduct(String id) {
        log.info("Eliminando producto id={}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo eliminar. Producto no encontrado id={}", id);
                    return new ResourceNotFoundException("Producto no encontrado");
                });

        productRepository.delete(product);

        log.info("Producto eliminado correctamente id={}", id);
    }

    public List<PriceResult> searchPrices(String query) {
        log.info("Buscando precios para producto query={}", query);

        List<Product> productos =
                productRepository.findByNombreContainingIgnoreCase(query);

        if (productos.isEmpty()) {
            log.warn("No se encontraron productos para query={}", query);
            return List.of();
        }

        return productos.stream()
                .map(this::buildPriceResult)
                .collect(Collectors.toList());
    }

    public PriceResult getPricesForProduct(String productId) {
        log.info("Buscando precios para productId={}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("No se encontraron precios. Producto no encontrado id={}", productId);
                    return new ResourceNotFoundException("Producto no encontrado");
                });

        return buildPriceResult(product);
    }

    private PriceResult buildPriceResult(Product product) {
        List<StorePrice> precios = VARIACIONES.entrySet().stream()
                .map(entry -> {
                    String storeId = entry.getKey();
                    double variacion = entry.getValue();

                    int precio = aplicarOfertaDia(
                            (int) (product.getPrecioBase() * variacion)
                    );

                    return StorePrice.builder()
                            .storeId(storeId)
                            .storeName(NOMBRES_TIENDA.get(storeId))
                            .precio(precio)
                            .unidad(product.getUnidad())
                            .marca(product.getMarca())
                            .disponible(product.getDisponible())
                            .build();
                })
                .sorted(Comparator.comparingInt(StorePrice::getPrecio))
                .collect(Collectors.toList());

        return PriceResult.builder()
                .producto(product.getNombre())
                .unidad(product.getUnidad())
                .precios(precios)
                .masBarato(precios.get(0))
                .build();
    }

    private int aplicarOfertaDia(int precio) {
        DayOfWeek dia = LocalDate.now().getDayOfWeek();
        return switch (dia) {
            case WEDNESDAY -> (int) (precio * 0.92);
            case SATURDAY -> (int) (precio * 0.97);
            default -> precio;
        };
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .nombre(product.getNombre())
                .categoria(product.getCategoria())
                .unidad(product.getUnidad())
                .marca(product.getMarca())
                .precioBase(product.getPrecioBase())
                .imagenUrl(product.getImagenUrl())
                .disponible(product.getDisponible())
                .build();
    }
}