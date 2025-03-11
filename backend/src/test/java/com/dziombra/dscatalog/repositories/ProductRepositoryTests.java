package com.dziombra.dscatalog.repositories;

import com.dziombra.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long exintingId;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUp () throws Exception {
        exintingId = 1L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists () {
        repository.deleteById(exintingId);

        Optional <Product> result = repository.findById(exintingId);
        Assertions.assertFalse(result.isPresent());
    }

}
