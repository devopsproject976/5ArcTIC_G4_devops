package tn.esprit.devops_project.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long idProduct;
    private String title;
    private float price;
    private int quantity;
    private String category; // Use String to represent ProductCategory
    private Long stockId; // Only the ID reference for Stock
    private Long supplierId; // Only the ID reference for Supplier
}
