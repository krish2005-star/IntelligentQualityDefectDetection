package iomp.project.iomp.controller;

import iomp.project.iomp.model.Product;
import iomp.project.iomp.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepo productRepo;

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productRepo.save(product));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(
                productRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Product not found"))
        );
    }
}