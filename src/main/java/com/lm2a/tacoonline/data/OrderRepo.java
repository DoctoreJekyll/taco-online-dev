package com.lm2a.tacoonline.data;

import com.lm2a.tacoonline.model.Order;

public interface OrderRepo {
    Order save(Order order);
}
