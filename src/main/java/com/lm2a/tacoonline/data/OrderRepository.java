package com.lm2a.tacoonline.data;

import com.lm2a.tacoonline.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
