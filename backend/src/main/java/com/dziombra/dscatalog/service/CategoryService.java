package com.dziombra.dscatalog.service;

import com.dziombra.dscatalog.entities.Category;
import com.dziombra.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<Category> findAll () {
        return repository.findAll();
    }
}
