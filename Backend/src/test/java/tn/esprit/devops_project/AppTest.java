package tn.esprit.devops_project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.InvoiceServiceImpl;
import tn.esprit.devops_project.services.ProductServiceImpl;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AppTest {

    @Mock
    private InvoiceRepository invoiceRepository;



    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Long invoiceId;
    private Invoice invoice;

    @BeforeEach
public void setUp() {
    // Create and save supplier using builder
    supplier = supplierRepository.save(
        Supplier.builder()
                .code("SUP001")
                .label("Test Supplier")
                .supplierCategory(SupplierCategory.CONVENTIONNE) // Set to CONVENTIONNE
                .build()
    );

    // Create and save stock using builder
    stock = stockRepository.save(
        Stock.builder()
                .title("Main Stock")
                .supplier(supplier) // Associate supplier with stock
                .build()
    );

    // Create and save product using builder
    product = productRepository.save(
        Product.builder()
                .title("Test Product")
                .price(100f) // Set base price
                .quantity(50)
                .category(ProductCategory.ELECTRONICS)
                .stock(stock) // Associate product with stock
                .supplier(supplier) // Associate product with supplier
                .build()
    );
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

    /*@Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductServiceImpl productService;

    private Product product;
    private Supplier supplier;
    private Stock stock;

    @BeforeEach
    public void setUp() {
        // Create and save supplier
        supplier = new Supplier();
        supplier.setCode("SUP001");
        supplier.setLabel("Test Supplier");
        supplier.setSupplierCategory(SupplierCategory.CONVENTIONNE); // Set to CONVENTIONNE
        supplier = supplierRepository.save(supplier); // Save to DB

        // Create and save stock
        stock = new Stock();
        stock.setTitle("Main Stock");
        stock.setSupplier(supplier); // Associate supplier with stock
        stock = stockRepository.save(stock); // Save to DB

        // Create and save product
        product = new Product();
        product.setTitle("Test Product");
        product.setPrice(100f); // Set base price
        product.setQuantity(50);
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setStock(stock); // Associate product with stock
        product.setSupplier(supplier); // Associate product with supplier
        product = productRepository.save(product); // Save the product here
    }

    @Test
    public void testCalculateTotalPrice() {
        // Call the method to test with a quantity of 2
        float result = productService.calculateTotalPrice(product.getIdProduct(), 2);

        // Calculate expected result (with discounts and taxes)
        float basePrice = 100f * 2; // Base price for 2 products
        float discountedPrice = basePrice * 0.9f; // 10% discount for CONVENTIONNE supplier
        float additionalDiscountedPrice = discountedPrice * 0.9f; // Additional 10% discount for electronics
        float expected = additionalDiscountedPrice * 1.15f; // 15% tax for electronics

        // Compare the result with the expected value
        assertEquals(expected, result, "The calculated total price is not as expected.");
    }

    @AfterEach
    public void tearDown() {
        // Clean up the test data in reverse order of creation to avoid foreign key issues
        if (product != null) {
            productRepository.delete(product);
        }
        if (stock != null) {
            stockRepository.delete(stock);
        }
        if (supplier != null) {
            supplierRepository.delete(supplier);
        }
    }*/
}
