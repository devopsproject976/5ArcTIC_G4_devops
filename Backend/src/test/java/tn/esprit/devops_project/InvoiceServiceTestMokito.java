package tn.esprit.devops_project;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.*;
import tn.esprit.devops_project.services.InvoiceServiceImpl;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;


public class InvoiceServiceTestMokito {
    @Mock
    private InvoiceRepository invoiceRepository;



    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Long invoiceId;
    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        // Set up mock data for testing
        Supplier supplier = new Supplier();
        supplier.setSupplierCategory(SupplierCategory.CONVENTIONNE);

        Operator operator = new Operator();
        operator.setFname("Test Operator");

        Product product = new Product();
        product.setTitle("Test Product");
        product.setPrice(100.0f);
        product.setCategory(ProductCategory.ELECTRONICS);

        InvoiceDetail detail = new InvoiceDetail();
        detail.setQuantity(2);
        detail.setProduct(product);

        invoice = new Invoice();
        invoice.setSupplier(supplier);
        invoice.setOperator(operator);
        invoice.setInvoiceDetails(Collections.singleton(detail)); // Assuming setInvoiceDetails accepts a Set
        invoice.setDateCreationInvoice(new Date());

        invoiceId = 1L; // Mock ID for testing

        // Mock repository behavior
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
    }

    @Test
    public void testGenerateDetailedInvoiceSummary() {
        // Act
        InvoiceSummary summary = invoiceService.generateDetailedInvoiceSummary(invoiceId);

        // Assert
        assertNotNull(summary);

        float expectedTotalDiscount = 30; // Total discount should be correctly calculated as discussed earlier.
        float expectedTotalTax = 25.5f; // This should now match our calculation.

        float expectedTotalAmount = (2 * 100) * (1 - (0.05f + 0.10f)) + (170 * 0.15f); // Calculate expected amount with discounts and taxes

        assertEquals(expectedTotalAmount, summary.getTotalAmount(), 0.01); // Use delta for total amount
        assertEquals(expectedTotalDiscount, summary.getTotalDiscount(), 0.01); // Use delta for total discount
        assertEquals(expectedTotalTax, summary.getTotalTax(), 0.01); // Use delta for total tax
    }
}
