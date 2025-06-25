package com.lm2a.tacoonline.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lm2a.tacoonline.model.Order;
import com.lm2a.tacoonline.model.Taco;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderRepoImpl implements OrderRepo {

    private SimpleJdbcInsert orderInsert;
    private SimpleJdbcInsert orderTacoInsert;
    private ObjectMapper mapper;

    public OrderRepoImpl(JdbcTemplate jdbcTemplate) {
        orderInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("Taco_Order").usingGeneratedKeyColumns("id");
        orderTacoInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("Taco_Order_Tacos");
        mapper = new ObjectMapper();
    }


    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);
        for (Taco taco : order.getTacos()) {
            saveTacoToOrder(taco, orderId);
        }
        return order;
    }

    private long saveOrderDetails(Order order) {
        Map<String, Object> map = mapper.convertValue(order, Map.class);
        map.put("placeAt", order.getPlacedAt());

        long orderId = orderInsert.executeAndReturnKey(map).longValue();
        return orderId;
    }


    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("taco", taco.getId());
        map.put("tacoOrder", orderId);
        orderTacoInsert.execute(map);
    }

}
