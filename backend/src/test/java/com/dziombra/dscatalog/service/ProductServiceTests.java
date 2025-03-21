package com.dziombra.dscatalog.service;

import com.dziombra.dscatalog.dto.ProductDTO;
import com.dziombra.dscatalog.entities.Category;
import com.dziombra.dscatalog.entities.Product;
import com.dziombra.dscatalog.repositories.CategoryRepository;
import com.dziombra.dscatalog.repositories.ProductRepository;
import com.dziombra.dscatalog.service.exceptions.DatabaseException;
import com.dziombra.dscatalog.service.exceptions.ResourceNotFoundException;
import com.dziombra.dscatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl <Product> page;
    private Product product;
    private Category category;
    private ProductDTO productDto;

    @BeforeEach
    void setUp () throws  Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        productDto = Factory.createProductDto();
        product = Factory.createProduct();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));


        when(repository.getReferenceById(existingId)).thenReturn(product);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
        when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        when(repository.findById(existingId)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        doNothing().when(repository).deleteById(existingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        when(repository.existsById(existingId)).thenReturn(true);
        when(repository.existsById(nonExistingId)).thenReturn(false);
        when(repository.existsById(dependentId)).thenReturn(true);

        doThrow(EmptyResultDataAccessException.class)
                .when(repository).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists () {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });

    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId () {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage () {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists () {
        ProductDTO productDTO = service.findById(existingId);
        Assertions.assertNotNull(productDTO);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExists () {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists () {
        ProductDTO result = service.update(existingId, productDto);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdNotExists () {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, productDto);
        });
    }

}
