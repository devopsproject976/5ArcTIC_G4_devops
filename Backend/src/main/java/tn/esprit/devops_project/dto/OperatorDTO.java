package tn.esprit.devops_project.dto;

import lombok.Data;

import java.util.Set;

@Data
public class OperatorDTO {
    private Long idOperateur;
    private String fname;
    private String lname;
    // Exclude password for security
    private Set<Long> invoiceIds; // IDs of associated invoices
}
