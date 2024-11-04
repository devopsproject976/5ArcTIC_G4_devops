package tn.esprit.devops_project;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.*;
import tn.esprit.devops_project.services.InvoiceServiceImpl;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
@Transactional
public class InvoiceServiceMockitoTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Long invoiceId;
    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        Supplier supplier = Supplier.builder()
                .supplierCategory(SupplierCategory.CONVENTIONNE)
                .build();

        Product product = Product.builder()
                .title("Test Product")
                .price(100.0f)
                .category(ProductCategory.ELECTRONICS)
                .build();

        InvoiceDetail detail = InvoiceDetail.builder()
                .quantity(2)
                .product(product)
                .build();

        invoice = Invoice.builder()
                .supplier(supplier)
                .invoiceDetails(Collections.singleton(detail))
                .dateCreationInvoice(new Date())
                .build();

        invoiceId = 1L;

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
    }

    @Test
    void testGenerateDetailedInvoiceSummary() {
        InvoiceSummary summary = invoiceService.generateDetailedInvoiceSummary(invoiceId);

        assertNotNull(summary);
        float expectedTotalDiscount = 30;
        float expectedTotalTax = 25.5f;
        float expectedTotalAmount = (2 * 100) * (1 - (0.05f + 0.10f)) + (170 * 0.15f);

        assertEquals(expectedTotalAmount, summary.getTotalAmount(), 0.01);
        assertEquals(expectedTotalDiscount, summary.getTotalDiscount(), 0.01);
        assertEquals(expectedTotalTax, summary.getTotalTax(), 0.01);
    }
}
