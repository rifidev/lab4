package com.example.tp3.services;


import com.example.tp3.dto.OrderDto;
import com.example.tp3.dto.OrderItemDto;
import com.example.tp3.dto.OrderRequest;
import com.example.tp3.models.Order;
import com.example.tp3.models.OrderItem;
import com.example.tp3.models.Product;
import com.example.tp3.repositories.OrderRepository;
import com.example.tp3.repositories.ProductRepository;
import com.example.tp3.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepository;

    @Transactional
    public OrderDto create(String userName, OrderRequest dto) {

        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = new Order(null, LocalDateTime.now(), user,new ArrayList<>());

        if (dto.getOrderItems() != null) {
            for (var orderItem : dto.getOrderItems()) {
                Product product = productRepo.findById(orderItem.productId())
                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
                OrderItem item = new OrderItem(null, product, orderItem.quantity(), order);
                order.getItems().add(item);
            }
        }

        Order saved = orderRepo.save(order);
        return toDto(saved);
    }

    public OrderDto getById(long id) {
        var order = orderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return toDto(order);
    }

    public List<OrderDto> findAll() {
        return orderRepo
                .findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public OrderDto update(long id, OrderDto dto) {
        var order = orderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.getItems().clear();

        if (dto.items() != null) {
            for (OrderItemDto itemDto : dto.items()) {
                Product product = productRepo.findById(itemDto.productId())
                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
                OrderItem item = new OrderItem(null, product, itemDto.quantity(), order);
                order.getItems().add(item);
            }
        }

        Order saved = orderRepo.save(order);
        return toDto(saved);
    }

    public void delete(long id) {
        var order = orderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order != null) orderRepo.delete(order);
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private OrderDto toDto(Order order) {
        List<OrderItemDto> items = order.getItems()
                .stream()
                .map(item -> new OrderItemDto(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuantity()
                ))
                .toList();

        return new OrderDto(order.getId(), order.getOrderDate(), items);
    }

    public List<OrderDto> findByUsername(String userName) {
        var user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return  orderRepo.findByUser(user).stream().map(o -> new OrderDto(o.getId(),o.getOrderDate(),
                o.getItems().stream().map(oi -> new OrderItemDto(oi.getId(),oi.getProduct().getId(),oi.getQuantity())).toList()
        )).toList();
    }
}