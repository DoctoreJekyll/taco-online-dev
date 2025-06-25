package com.lm2a.tacoonline.data;

import com.lm2a.tacoonline.model.Order;

public interface OrderRepository {
    Order save(Order order);
}
