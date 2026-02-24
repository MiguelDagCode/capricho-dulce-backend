package com.capricho.ms_catalogo.seeds;

import com.capricho.ms_catalogo.model.*;
import com.capricho.ms_catalogo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final InsumoRepository insumoRepository;
    private final RecetaRepository recetaRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("--- INICIANDO CARGA DE DATOS DEL CATÁLOGO (CON RECETAS COMPLETAS) ---");

        // 1. Crear Categorías
        Categoria catTortas = crearCategoriaSiNoExiste("Tortas");
        Categoria catPostres = crearCategoriaSiNoExiste("Postres");
        Categoria catCupcakes = crearCategoriaSiNoExiste("Cupcakes");
        Categoria catCombos = crearCategoriaSiNoExiste("Combos");

        // 2. Crear Insumos Básicos
        Insumo insHarina = crearInsumoSiNoExiste("Harina Pastelera", "KG");
        Insumo insAzucar = crearInsumoSiNoExiste("Azúcar Blanca", "KG");
        Insumo insHuevos = crearInsumoSiNoExiste("Huevos", "UNIDAD");
        Insumo insCacao = crearInsumoSiNoExiste("Cacao en Polvo", "KG");
        Insumo insQuesoCrema = crearInsumoSiNoExiste("Queso Crema", "KG");
        Insumo insFresas = crearInsumoSiNoExiste("Fresas Frescas", "KG");

        // 3. Crear Productos y capturar el objeto para armar la receta
        Producto prodChoco = crearProductoSiNoExiste(catTortas, "Torta de Chocolate Premium", 
                "Deliciosa torta de chocolate con triple capa de bizcocho húmedo, rellena y cubierta con nuestro fudge artesanal de la casa.", 
                new BigDecimal("120.00"), "https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80");

        Producto prodRedVelvet = crearProductoSiNoExiste(catTortas, "Torta Red Velvet", 
                "Elegante torta red velvet con frosting de queso crema, decorada con fresas frescas y flores comestibles. Un clásico irresistible.", 
                new BigDecimal("130.00"), "https://images.unsplash.com/photo-1616541823729-00fe0aacd32c?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80");

        Producto prodTresLeches = crearProductoSiNoExiste(catPostres, "Tres Leches Especial", 
                "Clásico postre latino bañado en nuestra mezcla secreta de tres leches, coronado con merengue italiano y canela.", 
                new BigDecimal("95.00"), "https://images.unsplash.com/photo-1464349095431-e9a21285b5f3?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80");

        Producto prodCupcakes = crearProductoSiNoExiste(catCupcakes, "Cupcakes de Vainilla x6", 
                "Caja de 6 esponjosos cupcakes de vainilla con buttercream de colores pasteles. Ideales para compartir.", 
                new BigDecimal("45.00"), "https://images.unsplash.com/photo-1486427944299-d1955d23e34d?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80");

        Producto prodCombo = crearProductoSiNoExiste(catCombos, "Combo Dulce Celebración", 
                "El combo perfecto: Torta mediana a elección, 6 cupcakes variados y caja de macarons surtidos.", 
                new BigDecimal("165.00"), "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80");

        Producto prodCheesecake = crearProductoSiNoExiste(catPostres, "Cheesecake de Frutos Rojos", 
                "Cremoso cheesecake estilo New York horneado sobre base de galleta, cubierto con mermelada casera de frutos rojos.", 
                new BigDecimal("85.00"), "https://images.unsplash.com/photo-1533134242443-d4fd215305ad?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80");

        // 4. Crear Recetas (Vincular Productos con Insumos)
        
        // Receta: Torta de Chocolate
        crearRecetaSiNoExiste(prodChoco, insHarina, new BigDecimal("0.500"));
        crearRecetaSiNoExiste(prodChoco, insAzucar, new BigDecimal("0.300"));
        crearRecetaSiNoExiste(prodChoco, insHuevos, new BigDecimal("4.000"));
        crearRecetaSiNoExiste(prodChoco, insCacao, new BigDecimal("0.250"));

        // Receta: Torta Red Velvet
        crearRecetaSiNoExiste(prodRedVelvet, insHarina, new BigDecimal("0.400"));
        crearRecetaSiNoExiste(prodRedVelvet, insAzucar, new BigDecimal("0.350"));
        crearRecetaSiNoExiste(prodRedVelvet, insQuesoCrema, new BigDecimal("0.600"));
        crearRecetaSiNoExiste(prodRedVelvet, insFresas, new BigDecimal("0.200"));

        // Receta: Tres Leches Especial
        crearRecetaSiNoExiste(prodTresLeches, insHarina, new BigDecimal("0.300"));
        crearRecetaSiNoExiste(prodTresLeches, insAzucar, new BigDecimal("0.200"));
        crearRecetaSiNoExiste(prodTresLeches, insHuevos, new BigDecimal("6.000"));

        // Receta: Cupcakes de Vainilla x6
        crearRecetaSiNoExiste(prodCupcakes, insHarina, new BigDecimal("0.250"));
        crearRecetaSiNoExiste(prodCupcakes, insAzucar, new BigDecimal("0.150"));
        crearRecetaSiNoExiste(prodCupcakes, insHuevos, new BigDecimal("3.000"));

        // Receta: Combo Dulce Celebración (Gasta bastante inventario)
        crearRecetaSiNoExiste(prodCombo, insHarina, new BigDecimal("0.800"));
        crearRecetaSiNoExiste(prodCombo, insAzucar, new BigDecimal("0.600"));
        crearRecetaSiNoExiste(prodCombo, insHuevos, new BigDecimal("8.000"));
        crearRecetaSiNoExiste(prodCombo, insCacao, new BigDecimal("0.150"));

        // Receta: Cheesecake de Frutos Rojos
        crearRecetaSiNoExiste(prodCheesecake, insQuesoCrema, new BigDecimal("0.800"));
        crearRecetaSiNoExiste(prodCheesecake, insAzucar, new BigDecimal("0.250"));
        crearRecetaSiNoExiste(prodCheesecake, insFresas, new BigDecimal("0.400"));
        crearRecetaSiNoExiste(prodCheesecake, insHuevos, new BigDecimal("2.000"));

        log.info("--- CARGA DE DATOS DEL CATÁLOGO FINALIZADA ---");
    }

    private Categoria crearCategoriaSiNoExiste(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    Categoria categoria = new Categoria();
                    categoria.setNombre(nombre);
                    categoria.setActivo(true);
                    log.info(" > Creando categoría: {}", nombre);
                    return categoriaRepository.save(categoria);
                });
    }

    private Insumo crearInsumoSiNoExiste(String nombre, String unidadMedida) {
        return insumoRepository.findByNombre(nombre) 
                .orElseGet(() -> {
                    Insumo insumo = new Insumo();
                    insumo.setNombre(nombre);
                    insumo.setUnidadMedida(unidadMedida);
                    insumo.setActivo(true);
                    log.info(" > Creando insumo: {}", nombre);
                    return insumoRepository.save(insumo);
                });
    }

    private Producto crearProductoSiNoExiste(Categoria categoria, String nombre, String descripcion, BigDecimal precio, String imgUrl) {
        return productoRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    Producto producto = new Producto();
                    producto.setCategoria(categoria);
                    producto.setNombre(nombre);
                    producto.setDescripcion(descripcion);
                    producto.setPrecio(precio);
                    producto.setImgUrl(imgUrl);
                    producto.setEstado(true);
                    log.info(" > Producto '{}' creado con éxito.", nombre);
                    return productoRepository.save(producto);
                });
    }

    private void crearRecetaSiNoExiste(Producto producto, Insumo insumo, BigDecimal cantidad) {
        RecetaId id = new RecetaId(producto.getIdProducto(), insumo.getIdInsumo());
        
        if (!recetaRepository.existsById(id)) {
            Receta receta = new Receta();
            receta.setId(id);
            receta.setProducto(producto);
            receta.setInsumo(insumo);
            receta.setCantidadInsumo(cantidad);
            receta.setActivo(true);
            
            recetaRepository.save(receta);
            log.info(" > Receta agregada: {} lleva {} {}", producto.getNombre(), cantidad, insumo.getNombre());
        }
    }
}