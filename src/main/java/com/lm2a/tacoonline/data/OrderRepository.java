package com.lm2a.tacoonline.data;

import com.lm2a.tacoonline.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Long> {
}
