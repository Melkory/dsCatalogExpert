package com.dziombra.dscatalog.tests;

import com.dziombra.dscatalog.dto.ProductDTO;
import com.dziombra.dscatalog.entities.Category;
import com.dziombra.dscatalog.entities.Product;
import org.checkerframework.checker.units.qual.C;

import java.time.Instant;

public class Factory {

    public static Product createProduct () {

        Product product = new Product(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDto () {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }



}
