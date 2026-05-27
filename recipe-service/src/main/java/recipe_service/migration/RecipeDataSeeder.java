package recipe_service.migration;

import io.mongock.api.annotations.*;
import lombok.extern.slf4j.Slf4j;
import recipe_service.domain.Ingredient;
import recipe_service.domain.Recipe;
import recipe_service.repository.RecipeRepository;

import java.time.LocalDateTime;
import java.util.List;

@ChangeUnit(id = "recipe-seeder-v1", order = "1", author = "kuanto")
@Slf4j
public class RecipeDataSeeder {

    @Execution
    public void execution(RecipeRepository recipeRepository) {

        // solo inserta si no hay datos
        if (recipeRepository.count() > 0) {
            log.info("Recetas ya existen, saltando seed");
            return;
        }

        List<Recipe> recetas = List.of(

            // PIE DE LIMON
            Recipe.builder()
                .nombre("Pie de Limón")
                .categoria("postres")
                .dificultad("facil")
                .tiempoMinutos(45)
                .porciones(8)
                .ingredientes(List.of(
                    new Ingredient("leche condensada", "1", "tarro"),
                    new Ingredient("limón", "4", "unidades"),
                    new Ingredient("galletas", "200", "gramos"),
                    new Ingredient("mantequilla", "100", "gramos")
                ))
                .pasos(List.of(
                    "Moler las galletas y mezclar con mantequilla derretida",
                    "Presionar en molde y refrigerar 20 minutos",
                    "Mezclar leche condensada con jugo de limón hasta espesar",
                    "Verter sobre la base y refrigerar mínimo 2 horas"
                ))
                .tags(List.of("postre", "frio", "sin_horno", "chileno"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(),

            // CAZUELA DE VACUNO
            Recipe.builder()
                .nombre("Cazuela de Vacuno")
                .categoria("sopas")
                .dificultad("media")
                .tiempoMinutos(90)
                .porciones(4)
                .ingredientes(List.of(
                    new Ingredient("osobuco", "4", "trozos"),
                    new Ingredient("papa", "4", "unidades"),
                    new Ingredient("zanahoria", "2", "unidades"),
                    new Ingredient("choclo", "2", "unidades"),
                    new Ingredient("zapallo", "200", "gramos"),
                    new Ingredient("arroz", "4", "cucharadas"),
                    new Ingredient("ajo", "3", "dientes"),
                    new Ingredient("sal", "1", "cucharadita")
                ))
                .pasos(List.of(
                    "Dorar el osobuco en olla con aceite caliente",
                    "Agregar agua caliente hasta cubrir la carne",
                    "Cocinar a fuego medio por 40 minutos",
                    "Agregar papas, zanahoria, choclo y zapallo",
                    "Agregar arroz y cocinar 20 minutos más",
                    "Sazonar con sal y servir caliente"
                ))
                .tags(List.of("plato_principal", "chileno", "tradicional"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(),

            // EMPANADAS DE PINO
            Recipe.builder()
                .nombre("Empanadas de Pino")
                .categoria("platos_principales")
                .dificultad("media")
                .tiempoMinutos(120)
                .porciones(12)
                .ingredientes(List.of(
                    new Ingredient("harina", "500", "gramos"),
                    new Ingredient("carne molida", "500", "gramos"),
                    new Ingredient("cebolla", "3", "unidades"),
                    new Ingredient("huevo", "2", "unidades"),
                    new Ingredient("aceitunas", "12", "unidades"),
                    new Ingredient("pasas", "50", "gramos"),
                    new Ingredient("manteca", "100", "gramos"),
                    new Ingredient("comino", "1", "cucharadita")
                ))
                .pasos(List.of(
                    "Preparar la masa mezclando harina, manteca y agua tibia",
                    "Freír la cebolla hasta dorar, agregar carne y condimentos",
                    "Dejar enfriar el pino completamente",
                    "Estirar la masa y cortar círculos de 15cm",
                    "Rellenar con pino, aceituna, pasas y huevo duro",
                    "Sellar los bordes y pintar con huevo",
                    "Hornear a 200°C por 25 minutos"
                ))
                .tags(List.of("plato_principal", "chileno", "horneado", "tradicional"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(),

            // ARROZ CON LECHE
            Recipe.builder()
                .nombre("Arroz con Leche")
                .categoria("postres")
                .dificultad("facil")
                .tiempoMinutos(40)
                .porciones(6)
                .ingredientes(List.of(
                    new Ingredient("arroz", "1", "taza"),
                    new Ingredient("leche", "1", "litro"),
                    new Ingredient("azúcar", "1", "taza"),
                    new Ingredient("canela", "2", "ramas"),
                    new Ingredient("ralladura de limón", "1", "cucharadita")
                ))
                .pasos(List.of(
                    "Cocinar el arroz en agua hasta que ablande",
                    "Agregar la leche caliente y la canela",
                    "Revolver constantemente a fuego bajo",
                    "Agregar azúcar y ralladura de limón",
                    "Cocinar hasta que espese, servir frío con canela"
                ))
                .tags(List.of("postre", "chileno", "tradicional", "facil"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(),

            // SOPAIPILLAS
            Recipe.builder()
                .nombre("Sopaipillas")
                .categoria("snacks")
                .dificultad("facil")
                .tiempoMinutos(30)
                .porciones(20)
                .ingredientes(List.of(
                    new Ingredient("harina", "500", "gramos"),
                    new Ingredient("zapallo cocido", "200", "gramos"),
                    new Ingredient("manteca", "50", "gramos"),
                    new Ingredient("sal", "1", "cucharadita"),
                    new Ingredient("aceite", "500", "mililitros")
                ))
                .pasos(List.of(
                    "Mezclar harina con zapallo, manteca y sal",
                    "Amasar hasta obtener masa homogénea",
                    "Estirar la masa y cortar círculos",
                    "Hacer agujeros con un tenedor",
                    "Freír en aceite caliente hasta dorar"
                ))
                .tags(List.of("snack", "chileno", "tradicional", "frito"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );

        recipeRepository.saveAll(recetas);
        log.info("Seed completado: {} recetas chilenas cargadas", recetas.size());
    }

    @RollbackExecution
    public void rollbackExecution(RecipeRepository recipeRepository) {
        recipeRepository.deleteAll();
        log.info("Rollback ejecutado: recetas eliminadas");
    }
}