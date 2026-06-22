package freshmart_service.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.TextIndexed;

@Document(collection = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    @TextIndexed
    private String nombre;         // "leche condensada"

    private String categoria;      // "lacteos", "panaderia", etc.
    private String unidad;         // "tarro 397g", "kg", "unidad"
    private String marca;          // "Nestlé", "Soprole", etc.
    private Integer precioBase;    // precio en CLP sin variación
    private String imagenUrl;
    private Boolean disponible;
}