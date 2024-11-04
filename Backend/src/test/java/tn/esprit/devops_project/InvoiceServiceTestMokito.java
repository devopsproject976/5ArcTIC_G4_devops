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
        // Create and save Supplier
        Supplier supplier = Supplier.builder()
                .supplierCategory(SupplierCategory.CONVENTIONNE)
                .build();
        supplierRepository.save(supplier);

        // Create and save Operator
        Operator operator = Operator.builder()
                .fname("Test Operator")
                .build();
        operatorRepository.save(operator);

        // Create and save Product
        Product product = Product.builder()
                .title("Test Product")
                .price(100.0f)
                .category(ProductCategory.ELECTRONICS)
                .build();
        productRepository.save(product);

        // Create InvoiceDetail
        InvoiceDetail detail = InvoiceDetail.builder()
                .quantity(2)
                .product(product)
                .build();

        // Create and save Invoice with details
        Invoice invoice = Invoice.builder()
                .supplier(supplier)
                .operator(operator)
                .invoiceDetails(new HashSet<>(Collections.singletonList(detail))) // Add detail to a Set
                .dateCreationInvoice(Date.from(Instant.now()))
                .build();

        invoice = invoiceRepository.save(invoice);
        invoiceId = invoice.getIdInvoice(); // Store the ID for testing
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
