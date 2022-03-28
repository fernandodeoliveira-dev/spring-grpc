package br.com.offtopic.grpc.service.impl;

import br.com.offtopic.grpc.domain.Product;
import br.com.offtopic.grpc.dto.ProductInputDTO;
import br.com.offtopic.grpc.dto.ProductOutputDTO;
import br.com.offtopic.grpc.exception.ProductAlreadyExistsException;
import br.com.offtopic.grpc.exception.ProductNotFoundException;
import br.com.offtopic.grpc.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("when create product is called with a valid data a product is returned")
    void createProductSuccessTest() {
        Product product = new Product(1L, "product name", 10.00, 10);

        when(productRepository.save(any())).thenReturn(product);

        ProductInputDTO inputDTO = new ProductInputDTO("product name", 10.00, 10);

        ProductOutputDTO outputDTO = productService.create(inputDTO);

        assertThat(outputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    @DisplayName("when create product is called with a duplicated name, throws ProductAlreadyExistsException")
    void createProductExceptionTest() {
        Product product = new Product(1L, "product name", 10.00, 10);

        when(productRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(product));

        ProductInputDTO inputDTO = new ProductInputDTO("product name", 10.00, 10);

        assertThatExceptionOfType(ProductAlreadyExistsException.class)
                .isThrownBy(() -> productService.create(inputDTO));
    }

    @Test
    @DisplayName("when findById product is called with a valid id a product is returned")
    void findByIdSuccessTest() {
        Long id = 1L;

        Product product = new Product(1L, "product name", 10.00, 10);

        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        ProductOutputDTO outputDTO = productService.findById(id);

        assertThat(outputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    @DisplayName("when findById product is called with an invalid id throws ProductNotFoundException")
    void findByIdProductExceptionTest() {
        Long id = 1L;

        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.findById(id));
    }

    @Test
    @DisplayName("when delete product is called with an ID should does not throw")
    void deleteSuccessTest() {
        Long id = 1L;

        Product product = new Product(1L, "product name", 10.00, 10);

        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        assertThatNoException().isThrownBy(() -> productService.findById(id));
    }

    @Test
    @DisplayName("when delete product is called with an invalid id throws ProductNotFoundException")
    void deleteExceptionTest() {
        Long id = 1L;

        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.delete(id));
    }

    @Test
    @DisplayName("when findAll products is called a list of products is returned")
    void findAllSuccessTest() {
        var products = List.of(
                new Product(1L, "product name", 10.00, 10),
                new Product(2L, "other product name", 10.00, 100)
        );

        when(productRepository.findAll()).thenReturn(products);

        var outputDTOS = productService.findAll();

        assertThat(outputDTOS)
                .extracting("id", "name", "price", "quantityInStock")
                .contains(
                  tuple(1L, "product name", 10.00, 10),
                  tuple(2L, "other product name", 10.00, 100)
                );
    }

}
