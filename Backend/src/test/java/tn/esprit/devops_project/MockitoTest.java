package tn.esprit.devops_project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.services.ProductServiceImpl;

import java.util.Optional;

public class MockitoTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Supplier supplier;
    private Stock stock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Create a mock supplier using builder
        supplier = Supplier.builder()
                .code("SUP001")
                .label("Test Supplier")
                .supplierCategory(SupplierCategory.CONVENTIONNE)
                .build();

        // Create a mock stock using builder
        stock = Stock.builder()
                .title("Main Stock")
                .supplier(supplier) // Associate stock with supplier
                .build();

        // Create a mock product using builder
        product = Product.builder()
                .idProduct(1L) // Assign an ID
                .title("Test Product")
                .price(100f) // Set base price
                .quantity(50)
                .category(ProductCategory.ELECTRONICS)
                .stock(stock) // Associate product with stock
                .supplier(supplier) // Associate product with supplier
                .build();
    }
    @Test
    public void testCalculateTotalPriceWithMockedProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        float result = productService.calculateTotalPrice(1L, 2);

        // Calculate expected result (considering all discounts and taxes)
        float basePrice = 100f * 2; // Base price for 2 products
        float discountedPrice = basePrice * 0.9f; // 10% discount for CONVENTIONNE supplier
        float additionalDiscountedPrice = discountedPrice * 0.9f; // Additional 10% discount for electronics
        float expected = additionalDiscountedPrice * 1.15f; // 15% tax for electronics

        // Assert
        assertEquals(expected, result, "The calculated total price is not as expected.");

        // Verify that the repository was called
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testCalculateTotalPriceProductNotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.calculateTotalPrice(1L, 2);
        });

        assertEquals("Product not found", exception.getMessage());
    }
}
