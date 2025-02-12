package com.dziombra.dscatalog.service;

import com.dziombra.dscatalog.dto.CategoryDTO;
import com.dziombra.dscatalog.entities.Category;
import com.dziombra.dscatalog.repositories.CategoryRepository;
import com.dziombra.dscatalog.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll () {
        List<Category> result = repository.findAll();
        return result.stream().map(x -> new CategoryDTO(x)).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById (Long id) {
        Category result = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Recurso não encontrado"));
        return new CategoryDTO(result);
    }

}
