package com.dziombra.dscatalog.repositories;

import com.dziombra.dscatalog.entities.Product;
import com.dziombra.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long exintingId;
    private long countTotalProducts;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUp () throws Exception {
        exintingId = 1L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists () {
        repository.deleteById(exintingId);

        Optional <Product> result = repository.findById(exintingId);
        Assertions.assertFalse(result.isPresent());
    }

    public void saveShouldPersistWithAutoincrementWhenIdIsNull () {
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository
                .save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

}
