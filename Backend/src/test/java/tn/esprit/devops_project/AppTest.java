package tn.esprit.devops_project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.ProductServiceImpl;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest
 class AppTest {

    @Autowired
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
     void testCalculateTotalPrice() {
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


}
