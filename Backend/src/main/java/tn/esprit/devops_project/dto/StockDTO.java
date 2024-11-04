package tn.esprit.devops_project.dto;

import lombok.Data;

import java.util.Set;

@Data
public class StockDTO {
    private long idStock;
    private String title;
    private Set<Long> productIds; // IDs of associated products
    private Long supplierId; // Only the ID reference for Supplier
}
