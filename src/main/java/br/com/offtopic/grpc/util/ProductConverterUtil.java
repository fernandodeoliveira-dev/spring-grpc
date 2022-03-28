package br.com.offtopic.grpc.util;

import br.com.offtopic.grpc.domain.Product;
import br.com.offtopic.grpc.dto.ProductInputDTO;
import br.com.offtopic.grpc.dto.ProductOutputDTO;

public class ProductConverterUtil {

    public static ProductOutputDTO productToProductOutputDto(Product product) {
        return new ProductOutputDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock()
        );
    }

    public static Product productInputDtoToProduct(ProductInputDTO product) {
        return new Product(
                null,
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock()
        );
    }
}
