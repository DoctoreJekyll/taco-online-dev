package com.lm2a.tacoonline.oldData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lm2a.tacoonline.model.Order;
import com.lm2a.tacoonline.model.Taco;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository // Indica que esta clase es un componente Spring para la capa de acceso a datos
public class OrderRepoImpl implements OrderRepo {

    //El simplejdbc este funciona con consultas simples y facilita las cosas para este tipo de consultas que no tienen joins ni tienen campos adicionales en sus tablas multi
    private SimpleJdbcInsert orderInsert;       // Para insertar en la tabla `Taco_Order`
    private SimpleJdbcInsert orderTacoInsert;   // Para insertar en la tabla `Taco_Order_Tacos` (relación muchos a muchos)
    private ObjectMapper mapper;                // Convierte objetos Java a Map<String, Object>

    // Constructor donde se configuran los insertadores
    public OrderRepoImpl(JdbcTemplate jdbcTemplate) {
        // Configura insert para la tabla de pedidos, devuelve ID generado
        orderInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");

        // Configura insert para la tabla intermedia de relación pedido-taco
        orderTacoInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Order_Tacos");

        mapper = new ObjectMapper(); // Inicializa el mapper
    }

    // Guarda un pedido (Order) y sus tacos asociados
    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date()); // Asigna la fecha actual al pedido

        long orderId = saveOrderDetails(order); // Inserta los datos del pedido y obtiene el ID
        for (Taco taco : order.getTacos()) {
            saveTacoToOrder(taco, orderId); // Inserta la relación entre pedido y taco
        }

        return order; // Devuelve el pedido con ID asignado
    }

    // Inserta los datos del pedido en `Taco_Order`
    private long saveOrderDetails(Order order) {
        // Convierte el objeto pedido a un mapa (clave-valor) que SimpleJdbcInsert puede usar
        Map<String, Object> map = mapper.convertValue(order, Map.class);

        // Corrige o asegura que la columna `placeAt` se use correctamente
        map.put("placeAt", order.getPlacedAt());

        // Ejecuta la inserción y devuelve la clave generada
        return orderInsert.executeAndReturnKey(map).longValue();
    }

    // Inserta una fila en `Taco_Order_Tacos` para relacionar pedido y taco
    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("taco", taco.getId());         // ID del taco
        map.put("tacoOrder", orderId);         // ID del pedido

        orderTacoInsert.execute(map); // Ejecuta la inserción en la tabla intermedia
    }
}
