package freshmart_service.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import freshmart_service.domain.Product;
import freshmart_service.dto.PriceResult;
import freshmart_service.dto.StorePrice;
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

    // variaciones de precio por tienda
    private static final Map<String, Double> VARIACIONES = Map.of(
        "simermart",   1.0,
        "frescopro",   1.08,
        "megacanasta", 0.94
    );

    private static final Map<String, String> NOMBRES_TIENDA = Map.of(
        "simermart",   "SimerMart",
        "frescopro",   "FrescoPro",
        "megacanasta", "MegaCanasta"
    );

    public List<PriceResult> searchPrices(String query) {
        List<Product> productos =
            productRepository.findByNombreContainingIgnoreCase(query);

        if (productos.isEmpty()) {
            log.warn("No se encontraron productos para: {}", query);
            return List.of();
        }

        return productos.stream()
            .map(this::buildPriceResult)
            .collect(Collectors.toList());
    }

    public PriceResult getPricesForProduct(String productId) {
        return productRepository.findById(productId)
            .map(this::buildPriceResult)
            .orElse(null);
    }

    private PriceResult buildPriceResult(Product product) {
        List<StorePrice> precios = VARIACIONES.entrySet().stream()
            .map(entry -> {
                String storeId   = entry.getKey();
                double variacion = entry.getValue();

                // aplica variación + oferta del día
                int precio = aplicarOfertaDia(
                    (int)(product.getPrecioBase() * variacion)
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

    // simula ofertas reales por día de la semana
    private int aplicarOfertaDia(int precio) {
        DayOfWeek dia = LocalDate.now().getDayOfWeek();
        return switch (dia) {
            case WEDNESDAY -> (int)(precio * 0.92); // miércoles de ofertas
            case SATURDAY  -> (int)(precio * 0.97); // oferta fin de semana
            default        -> precio;
        };
    }

    // cargar catálogo base de productos de prueba
    /* public void cargarProductosPrueba() {
        if (productRepository.count() > 0) return;

        List<Product> productos = List.of(
            Product.builder().nombre("leche condensada")
                .categoria("lacteos").unidad("tarro 397g")
                .marca("Nestlé").precioBase(1490).disponible(true).build(),
            Product.builder().nombre("limón")
                .categoria("frutas").unidad("kg")
                .marca("A granel").precioBase(990).disponible(true).build(),
            Product.builder().nombre("galletas")
                .categoria("panaderia").unidad("paquete 200g")
                .marca("Costa").precioBase(890).disponible(true).build(),
            Product.builder().nombre("mantequilla")
                .categoria("lacteos").unidad("paquete 100g")
                .marca("Soprole").precioBase(1290).disponible(true).build(),
            Product.builder().nombre("harina")
                .categoria("abarrotes").unidad("kg")
                .marca("Selecta").precioBase(790).disponible(true).build(),
            Product.builder().nombre("azúcar")
                .categoria("abarrotes").unidad("kg")
                .marca("Iansa").precioBase(890).disponible(true).build(),
            Product.builder().nombre("huevos")
                .categoria("lacteos").unidad("cartón 12 unidades")
                .marca("Agrosuper").precioBase(2490).disponible(true).build(),
            Product.builder().nombre("aceite")
                .categoria("abarrotes").unidad("botella 1L")
                .marca("Chef").precioBase(1990).disponible(true).build(),
            Product.builder().nombre("arroz")
                .categoria("abarrotes").unidad("kg")
                .marca("Tucapel").precioBase(990).disponible(true).build(),
            Product.builder().nombre("pollo entero")
                .categoria("carnes").unidad("kg")
                .marca("Agrosuper").precioBase(2990).disponible(true).build()
        );

        productRepository.saveAll(productos);
        log.info("Catálogo base cargado: {} productos", productos.size());
    }
*/
}