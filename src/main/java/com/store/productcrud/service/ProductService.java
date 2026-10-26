package com.store.productcrud.service;

import com.store.productcrud.exception.InvalidDataException;
import com.store.productcrud.exception.ProductNotFoundException;
import com.store.productcrud.model.Product;
import com.store.productcrud.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Inyección de dependencias por constructor
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> listarTodos() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product buscarPorId(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public Product crear(Product product) {
        validar(product);
        product.setId(null); // garantiza que se cree un registro nuevo
        return productRepository.save(product);
    }

    @Transactional
    public Product actualizar(Long id, Product datos) {
        Product existente = buscarPorId(id);
        validar(datos);

        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());

        return productRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    private void validar(Product product) {
        if (product.getNombre() == null || product.getNombre().isBlank()) {
            throw new InvalidDataException("El nombre es obligatorio");
        }
        if (product.getNombre().length() > 100) {
            throw new InvalidDataException("El nombre no puede superar 100 caracteres");
        }
        if (product.getDescripcion() != null && product.getDescripcion().length() > 255) {
            throw new InvalidDataException("La descripción no puede superar 255 caracteres");
        }
        if (product.getPrecio() == null || product.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDataException("El precio es obligatorio y no puede ser negativo");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new InvalidDataException("El stock es obligatorio y no puede ser negativo");
        }
    }
}
