package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.dto.ProductDTO;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.services.Iservices.IProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final IProductService productService;

    @PostMapping("/product/{idStock}")
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO, @PathVariable Long idStock) {
        return convertToDTO(productService.addProduct(convertToEntity(productDTO), idStock));
    }

    @GetMapping("/product/{id}")
    public ProductDTO retrieveProduct(@PathVariable Long id) {
        return convertToDTO(productService.retrieveProduct(id));
    }

    @GetMapping("/product")
    public List<ProductDTO> retrieveAllProduct() {
        return productService.retreiveAllProduct()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/product/stock/{id}")
    public List<ProductDTO> retrieveProductStock(@PathVariable Long id) {
        return productService.retreiveProductStock(id)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/productCategory/{category}")
    public List<ProductDTO> retrieveProductByCategory(@PathVariable ProductCategory category) {
        return productService.retrieveProductByCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // Conversion methods
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setIdProduct(product.getIdProduct());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setCategory(product.getCategory().name());
        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        return Product.builder()
                .idProduct(dto.getIdProduct())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .category(ProductCategory.valueOf(dto.getCategory()))
                .build();
    }
}
