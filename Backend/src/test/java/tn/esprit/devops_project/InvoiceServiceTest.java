package tn.esprit.devops_project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.*;
import tn.esprit.devops_project.services.InvoiceServiceImpl;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class InvoiceServiceTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private InvoiceServiceImpl invoiceService;

    private Long invoiceId;

    @BeforeEach
    public void setUp() {
        // Create and save Supplier
        Supplier supplier = new Supplier();
        supplier.setSupplierCategory(SupplierCategory.CONVENTIONNE);
        supplierRepository.save(supplier);

        // Create and save Operator
        Operator operator = new Operator();
        operator.setFname("Test Operator");
        operatorRepository.save(operator);

        // Create and save Product
        Product product = new Product();
        product.setTitle("Test Product");
        product.setPrice(100.0f);
        product.setCategory(ProductCategory.ELECTRONICS);
        productRepository.save(product);

        // Create and save InvoiceDetail
        InvoiceDetail detail = new InvoiceDetail();
        detail.setQuantity(2);
        detail.setProduct(product);

        // Create and save Invoice with details
        Invoice invoice = new Invoice();
        invoice.setSupplier(supplier);
        invoice.setOperator(operator);

        List<InvoiceDetail> details = new ArrayList<>();
        details.add(detail);

        invoice.setInvoiceDetails(new HashSet<>(details));
        invoice.setDateCreationInvoice(Date.from(Instant.now()));

        invoice = invoiceRepository.save(invoice);
        invoiceId = invoice.getIdInvoice(); // Store the ID for testing
    }

    @AfterEach
    public void tearDown() {
        // Clean up the database after each test if needed (optional)
        invoiceDetailRepository.deleteAll();
        invoiceRepository.deleteAll();
        productRepository.deleteAll();
        operatorRepository.deleteAll();
        supplierRepository.deleteAll();
    }

    @Test
    public void testGenerateDetailedInvoiceSummary() {
        // Act
        InvoiceSummary summary = invoiceService.generateDetailedInvoiceSummary(invoiceId); // Call the service method

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
