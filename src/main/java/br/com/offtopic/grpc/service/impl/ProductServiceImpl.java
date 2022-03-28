package br.com.offtopic.grpc.service.impl;

import br.com.offtopic.grpc.domain.Product;
import br.com.offtopic.grpc.dto.ProductInputDTO;
import br.com.offtopic.grpc.dto.ProductOutputDTO;
import br.com.offtopic.grpc.exception.ProductAlreadyExistsException;
import br.com.offtopic.grpc.exception.ProductNotFoundException;
import br.com.offtopic.grpc.repository.ProductRepository;
import br.com.offtopic.grpc.service.IProductService;
import br.com.offtopic.grpc.util.ProductConverterUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductOutputDTO create(ProductInputDTO inputDTO) {
        checkDuplicity(inputDTO.getName());

        var product = ProductConverterUtil.productInputDtoToProduct(inputDTO);
        var productCreated = this.productRepository.save(product);

        return ProductConverterUtil.productToProductOutputDto(productCreated);
    }

    @Override
    public ProductOutputDTO findById(Long id) {
        var product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductConverterUtil.productToProductOutputDto(product);
    }

    @Override
    public void delete(Long id) {
        var product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        this.productRepository.delete(product);
    }

    @Override
    public List<ProductOutputDTO> findAll() {
        return this.productRepository.findAll().stream()
                .map(ProductConverterUtil::productToProductOutputDto)
                .collect(Collectors.toList());
    }

    private void checkDuplicity(String name) {
        this.productRepository.findByNameIgnoreCase(name)
                .ifPresent(e -> {
                    throw new ProductAlreadyExistsException(name);
                });
    }


}
