package freshmart_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private String id;
    private String nombre;
    private String categoria;
    private String unidad;
    private String marca;
    private Integer precioBase;
    private String imagenUrl;
    private Boolean disponible;
}