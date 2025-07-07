package com.lm2a.tacoonline.api;

import com.lm2a.tacoonline.data.OrderRepository;
import com.lm2a.tacoonline.data.TacoRepository;
import com.lm2a.tacoonline.exceptions.OrderNotFoundException;
import com.lm2a.tacoonline.model.Order;
import com.lm2a.tacoonline.model.Taco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/order")
public class ApiOrderController {

    OrderRepository orderRepository;
    TacoRepository tacoRepository;

    public ApiOrderController(OrderRepository orderRepository, TacoRepository tacoRepository) {
        this.orderRepository = orderRepository;
        this.tacoRepository = tacoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/list")
    public ResponseEntity<Iterable<Order>> getAllOrders(){
        Iterable<Order> orders = orderRepository.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/recents/{page}")
    public ResponseEntity<Iterable<Order>> getAllOrdersByPage(@PathVariable("page") Integer page){
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Iterable<Order> orders = orderRepository.findAll(pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Order> createOrder(@RequestBody Order order){

        List<Taco> tacos = order.getTacos();

        for (Taco taco : tacos) {
            tacoRepository.save(taco);
        }

        Order savedOrder = orderRepository.save(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        orderRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }

    @PutMapping()
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) {
        Order existingOrder = orderRepository.findById(order.getId())
                .orElseThrow(OrderNotFoundException::new);

        existingOrder.setDeliveryName(order.getDeliveryName());
        existingOrder.setDeliveryStreet(order.getDeliveryStreet());
        existingOrder.setDeliveryCity(order.getDeliveryCity());
        existingOrder.setDeliveryState(order.getDeliveryState());
        existingOrder.setDeliveryZip(order.getDeliveryZip());
        existingOrder.setCcNumber(order.getCcNumber());
        existingOrder.setCcExpiration(order.getCcExpiration());
        existingOrder.setCcCVV(order.getCcCVV());
        existingOrder.setTacos(order.getTacos());

        Order updatedOrder = orderRepository.save(existingOrder);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

}
