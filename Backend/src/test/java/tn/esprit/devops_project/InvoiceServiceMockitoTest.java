package tn.esprit.devops_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.*;
import tn.esprit.devops_project.services.InvoiceServiceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceMockitoTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Long invoiceId;
    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        // Create and mock Supplier
        Supplier supplier = Supplier.builder()
                .supplierCategory(SupplierCategory.CONVENTIONNE)
                .build();

        // Create and mock Operator
        Operator operator = Operator.builder()
                .fname("Test Operator")
                .build();

        // Create and mock Product
        Product product = Product.builder()
                .title("Test Product")
                .price(100.0f)
                .category(ProductCategory.ELECTRONICS)
                .build();

        // Create InvoiceDetail
        InvoiceDetail detail = InvoiceDetail.builder()
                .quantity(2)
                .product(product)
                .build();

        // Create Invoice with Operator
        invoice = Invoice.builder()
                .supplier(supplier)
                .operator(operator) // Associate operator directly here
                .invoiceDetails(new HashSet<>(Collections.singletonList(detail)))
                .dateCreationInvoice(Date.from(Instant.now()))
                .build();

        // Mock the repository to return the Invoice with operator
        invoiceId = 1L; // Assign a mock ID
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
    }

    @Test
    public void testGenerateDetailedInvoiceSummary() {
        // Act
        InvoiceSummary summary = invoiceService.generateDetailedInvoiceSummary(invoiceId);

        // Assert
        assertNotNull(summary);

        float expectedTotalDiscount = 30; // Expected discount
        float expectedTotalTax = 25.5f; // Expected tax
        float expectedTotalAmount = (2 * 100) * (1 - (0.05f + 0.10f)) + (170 * 0.15f); // Expected amount

        assertEquals(expectedTotalAmount, summary.getTotalAmount(), 0.01);
        assertEquals(expectedTotalDiscount, summary.getTotalDiscount(), 0.01);
        assertEquals(expectedTotalTax, summary.getTotalTax(), 0.01);
    }
}
