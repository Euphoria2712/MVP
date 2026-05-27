package freshmart_service.migration;

import freshmart_service.domain.Product;
import freshmart_service.repository.ProductRepository;
import io.mongock.api.annotations.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@ChangeUnit(id = "freshmart-seeder-v1", order = "1", author = "kuanto")
@Slf4j
public class FreshmartDataSeeder {

    @Execution
    public void execution(ProductRepository productRepository) {

        if (productRepository.count() > 0) {
            log.info("Productos ya existen, saltando seed");
            return;
        }

        List<Product> productos = List.of(

            // LACTEOS
            Product.builder()
                .nombre("leche condensada")
                .categoria("lacteos")
                .unidad("tarro 397g")
                .marca("Nestlé")
                .precioBase(1490)
                .disponible(true).build(),

            Product.builder()
                .nombre("leche entera")
                .categoria("lacteos")
                .unidad("litro")
                .marca("Soprole")
                .precioBase(990)
                .disponible(true).build(),

            Product.builder()
                .nombre("mantequilla")
                .categoria("lacteos")
                .unidad("paquete 100g")
                .marca("Soprole")
                .precioBase(1290)
                .disponible(true).build(),

            Product.builder()
                .nombre("huevos")
                .categoria("lacteos")
                .unidad("cartón 12 unidades")
                .marca("Agrosuper")
                .precioBase(2490)
                .disponible(true).build(),

            Product.builder()
                .nombre("queso")
                .categoria("lacteos")
                .unidad("paquete 200g")
                .marca("Colun")
                .precioBase(2190)
                .disponible(true).build(),

            // FRUTAS Y VERDURAS
            Product.builder()
                .nombre("limón")
                .categoria("frutas")
                .unidad("kg")
                .marca("A granel")
                .precioBase(990)
                .disponible(true).build(),

            Product.builder()
                .nombre("tomate")
                .categoria("verduras")
                .unidad("kg")
                .marca("A granel")
                .precioBase(890)
                .disponible(true).build(),

            Product.builder()
                .nombre("cebolla")
                .categoria("verduras")
                .unidad("kg")
                .marca("A granel")
                .precioBase(690)
                .disponible(true).build(),

            Product.builder()
                .nombre("papa")
                .categoria("verduras")
                .unidad("kg")
                .marca("A granel")
                .precioBase(590)
                .disponible(true).build(),

            Product.builder()
                .nombre("zapallo")
                .categoria("verduras")
                .unidad("kg")
                .marca("A granel")
                .precioBase(490)
                .disponible(true).build(),

            Product.builder()
                .nombre("zanahoria")
                .categoria("verduras")
                .unidad("kg")
                .marca("A granel")
                .precioBase(590)
                .disponible(true).build(),

            // ABARROTES
            Product.builder()
                .nombre("harina")
                .categoria("abarrotes")
                .unidad("kg")
                .marca("Selecta")
                .precioBase(790)
                .disponible(true).build(),

            Product.builder()
                .nombre("azúcar")
                .categoria("abarrotes")
                .unidad("kg")
                .marca("Iansa")
                .precioBase(890)
                .disponible(true).build(),

            Product.builder()
                .nombre("arroz")
                .categoria("abarrotes")
                .unidad("kg")
                .marca("Tucapel")
                .precioBase(990)
                .disponible(true).build(),

            Product.builder()
                .nombre("aceite")
                .categoria("abarrotes")
                .unidad("botella 1L")
                .marca("Chef")
                .precioBase(1990)
                .disponible(true).build(),

            Product.builder()
                .nombre("sal")
                .categoria("abarrotes")
                .unidad("paquete 1kg")
                .marca("Lobos")
                .precioBase(390)
                .disponible(true).build(),

            // PANADERIA
            Product.builder()
                .nombre("galletas")
                .categoria("panaderia")
                .unidad("paquete 200g")
                .marca("Costa")
                .precioBase(890)
                .disponible(true).build(),

            Product.builder()
                .nombre("pan molde")
                .categoria("panaderia")
                .unidad("paquete 500g")
                .marca("Ideal")
                .precioBase(1290)
                .disponible(true).build(),

            // CARNES
            Product.builder()
                .nombre("pollo entero")
                .categoria("carnes")
                .unidad("kg")
                .marca("Agrosuper")
                .precioBase(2990)
                .disponible(true).build(),

            Product.builder()
                .nombre("carne molida")
                .categoria("carnes")
                .unidad("kg")
                .marca("Agrosuper")
                .precioBase(4990)
                .disponible(true).build(),

            Product.builder()
                .nombre("osobuco")
                .categoria("carnes")
                .unidad("kg")
                .marca("A granel")
                .precioBase(5990)
                .disponible(true).build()
        );

        productRepository.saveAll(productos);
        log.info("Seed completado: {} productos cargados", productos.size());
    }

    @RollbackExecution
    public void rollbackExecution(ProductRepository productRepository) {
        productRepository.deleteAll();
        log.info("Rollback ejecutado: productos eliminados");
    }
}