package org.chendu.demo.oauth2.repository;

import org.chendu.demo.oauth2.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chen Du @10/30/2018.
 * Version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByNameLike(String name);

    List<Product> findAll();
}
