package com.example.tp3.services;

import com.example.tp3.dto.ProductDto;
import com.example.tp3.models.Category;
import com.example.tp3.models.Product;
import com.example.tp3.repositories.CategoryRepository;
import com.example.tp3.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    @Transactional
    public ProductDto create(ProductDto dto) {
        Category cat = categoryRepo.findById(dto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Product p = new Product(null, dto.name(), dto.price(), cat);
        Product saved = productRepo.save(p);
        return new ProductDto(saved.getId(), saved.getName(), saved.getPrice(), cat.getId());
    }



    public ProductDto getById(long id){
       var product= productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

       // TODO: check getCategory -> lazy loading
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getCategory().getId());
    }


    public List<ProductDto> findAll(){
        return productRepo
                .findAll()
                .stream()
                .map(product -> new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getCategory().getId()))
                .toList();
    }

    @Transactional
    public ProductDto update(long id,ProductDto dto){
        Category cat = categoryRepo.findById(dto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        var product= productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setName(dto.name());
        product.setCategory(cat);
        product.setPrice(dto.price());
        Product saved = productRepo.save(product);
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), cat.getId());
    }
    // Méthodes read, update, delete similaires


    public void delete(long id){
        var product= productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if(product != null) productRepo.delete(product);
    }

}
