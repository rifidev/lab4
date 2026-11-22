package com.example.tp3.dto;

import java.time.LocalDateTime;
import java.util.List;

public record   OrderDto(Long id, LocalDateTime orderDate, List<OrderItemDto> items) {}
