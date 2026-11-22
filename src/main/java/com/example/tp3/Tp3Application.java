package com.example.tp3;

import com.example.tp3.models.Category;
import com.example.tp3.models.Order;
import com.example.tp3.models.OrderItem;
import com.example.tp3.models.Product;
import com.example.tp3.repositories.CategoryRepository;
import com.example.tp3.repositories.OrderRepository;
import com.example.tp3.repositories.ProductRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Tp3Application {

    public static void main(String[] args) {
        SpringApplication.run(Tp3Application.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository) {

        return args -> {

            if (categoryRepository.count() > 0) return;

            // Categories
            Category electronics = new Category();
            electronics.setName("Electronics");

            Category books = new Category();
            books.setName("Books");

            categoryRepository.saveAll(
                    java.util.List.of(electronics, books)
            );

            // Products
            Product laptop = new Product(null, "Laptop", 8500.0, electronics);
            Product phone = new Product(null, "Smartphone", 4500.0, electronics);
            Product javaBook = new Product(null, "Java Book", 250.0, books);

            productRepository.saveAll(
                    java.util.List.of(laptop, phone, javaBook)
            );

            // Order
            Order order = new Order();
            order.setItems(new java.util.ArrayList<>());

            OrderItem item1 = new OrderItem(null, laptop, 1, order);
            OrderItem item2 = new OrderItem(null, phone, 2, order);

            order.getItems().addAll(java.util.List.of(item1, item2));

            orderRepository.save(order);

            System.out.println("Database seeded successfully!");
        };
    }
}