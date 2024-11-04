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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InvoiceDetailRepository invoiceDetailRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Long invoiceId;
    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        // Mock Supplier
        Supplier supplier = Supplier.builder()
                .supplierCategory(SupplierCategory.CONVENTIONNE)
                .build();

        // Mock Operator
        Operator operator = Operator.builder()
                .fname("Test Operator")
                .build();

        // Mock Product
        Product product = Product.builder()
                .title("Test Product")
                .price(100.0f)
                .category(ProductCategory.ELECTRONICS)
                .build();

        // Mock InvoiceDetail
        InvoiceDetail detail = InvoiceDetail.builder()
                .quantity(2)
                .product(product)
                .build();

        // Create Invoice with mocked details and operator
        invoice = Invoice.builder()
                .supplier(supplier)
                .operator(operator) // Ensure the operator is associated here
                .invoiceDetails(new HashSet<>(Collections.singletonList(detail)))
                .dateCreationInvoice(Date.from(Instant.now()))
                .build();

        invoiceId = 1L; // Assign a mock ID

        // Define repository behaviors
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
    }

    @Test
    public void testGenerateDetailedInvoiceSummary() {
        // Act
        InvoiceSummary summary = invoiceService.generateDetailedInvoiceSummary(invoiceId);

        // Assert
        assertNotNull(summary);

        float expectedTotalDiscount = 30; // Total discount should be correctly calculated
        float expectedTotalTax = 25.5f; // Total tax calculation

        float expectedTotalAmount = (2 * 100) * (1 - (0.05f + 0.10f)) + (170 * 0.15f); // Expected amount

        assertEquals(expectedTotalAmount, summary.getTotalAmount(), 0.01); // Total amount with delta
        assertEquals(expectedTotalDiscount, summary.getTotalDiscount(), 0.01); // Total discount with delta
        assertEquals(expectedTotalTax, summary.getTotalTax(), 0.01); // Total tax with delta
    }
}
