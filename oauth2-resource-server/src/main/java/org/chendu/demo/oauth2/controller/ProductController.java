package org.chendu.demo.oauth2.controller;

import org.chendu.demo.oauth2.model.Product;
import org.chendu.demo.oauth2.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Chen Du @10/31/2018.
 * Version 1.0
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> products() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        return productRepository.findById(id)
                .orElse(null);
    }

    @GetMapping("/search/")
    @PreAuthorize("hasRole('ROLE_PRODUCT_ADMIN')")
    public Product findByName(@RequestParam("name") String name) {
        return productRepository.findByNameLike("%" + name + "%");
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('ROLE_PRODUCT_ADMIN')")
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
