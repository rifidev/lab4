package com.example.tp3.services;

import com.example.tp3.dto.OrderItemDto;
import com.example.tp3.models.Order;
import com.example.tp3.models.OrderItem;
import com.example.tp3.models.Product;
import com.example.tp3.repositories.OrderItemRepository;
import com.example.tp3.repositories.OrderRepository;
import com.example.tp3.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    @Transactional
    public OrderItemDto create(OrderItemDto dto) {
        Product product = productRepo.findById(dto.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Order order = orderRepo.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        OrderItem item = new OrderItem(null, product, dto.quantity(), order);
        OrderItem saved = orderItemRepo.save(item);
        return toDto(saved);
    }

    public OrderItemDto getById(long id) {
        var item = orderItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found"));
        return toDto(item);
    }

    public List<OrderItemDto> findAll() {
        return orderItemRepo
                .findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public OrderItemDto update(long id, OrderItemDto dto) {
        var item = orderItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found"));

        Product product = productRepo.findById(dto.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Order order = orderRepo.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        item.setProduct(product);
        item.setOrder(order);
        item.setQuantity(dto.quantity());

        OrderItem saved = orderItemRepo.save(item);
        return toDto(saved);
    }

    public void delete(long id) {
        var item = orderItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found"));

        if (item != null) orderItemRepo.delete(item);
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private OrderItemDto toDto(OrderItem item) {
        return new OrderItemDto(
                item.getId(),
                item.getProduct().getId(),
                item.getQuantity()
        );
    }
}
