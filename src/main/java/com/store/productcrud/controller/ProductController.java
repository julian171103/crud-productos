package com.store.productcrud.controller;

import com.store.productcrud.model.Product;
import com.store.productcrud.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> listarTodos() {
        return productService.listarTodos();
    }

    @GetMapping("/{id}")
    public Product buscarPorId(@PathVariable Long id) {
        return productService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Product> crear(@RequestBody Product product) {
        Product creado = productService.crear(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public Product actualizar(@PathVariable Long id, @RequestBody Product product) {
        return productService.actualizar(id, product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
