package tn.esprit.devops_project.dto;

import lombok.Data;

@Data
public class SupplierDTO {
    private Long idSupplier;
    private String code;
    private String label;
    private String supplierCategory; // Use String to represent SupplierCategory enum
}
