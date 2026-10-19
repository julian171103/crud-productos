package com.tienda.crudproductos.service;

import com.tienda.crudproductos.exception.DatoInvalidoException;
import com.tienda.crudproductos.exception.ProductoNoEncontradoException;
import com.tienda.crudproductos.model.Producto;
import com.tienda.crudproductos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Inyección de dependencias por constructor
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    @Transactional
    public Producto crear(Producto producto) {
        validar(producto);
        producto.setId(null); // garantiza que se cree un registro nuevo
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, Producto datos) {
        Producto existente = buscarPorId(id);
        validar(datos);

        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());

        return productoRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException(id);
        }
        productoRepository.deleteById(id);
    }

    private void validar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new DatoInvalidoException("El nombre es obligatorio");
        }
        if (producto.getNombre().length() > 100) {
            throw new DatoInvalidoException("El nombre no puede superar 100 caracteres");
        }
        if (producto.getDescripcion() != null && producto.getDescripcion().length() > 255) {
            throw new DatoInvalidoException("La descripción no puede superar 255 caracteres");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new DatoInvalidoException("El precio es obligatorio y no puede ser negativo");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new DatoInvalidoException("El stock es obligatorio y no puede ser negativo");
        }
    }
}
