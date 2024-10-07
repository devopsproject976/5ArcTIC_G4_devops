package tn.esprit.devops_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.InvoiceDetail;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.services.ProductServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class AppTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    InvoiceRepository invoiceRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // JUnit test without using Mockito
    @Test
    void testCalculateTotalPriceForInvoiceDetails() {
        // Arrange
        Product product = new Product();
        product.setIdProduct(1L);
        product.setPrice(50.0f);

        InvoiceDetail detail1 = new InvoiceDetail();
        detail1.setProduct(product);
        detail1.setQuantity(2);

        InvoiceDetail detail2 = new InvoiceDetail();
        detail2.setProduct(product);
        detail2.setQuantity(3);

        // Act - We are directly calculating the total without mocks
        float total = detail1.getQuantity() * product.getPrice() + detail2.getQuantity() * product.getPrice();

        // Assert
        assertEquals(250.0f, total);
    }

    // Mockito test with mocking
    @Test
    void testCalculateTotalInvoiceAmountForProductWithMockito() {
        // Arrange
        Product product = new Product();
        product.setIdProduct(1L);
        product.setPrice(100.0f);

        InvoiceDetail invoiceDetail1 = new InvoiceDetail();
        invoiceDetail1.setProduct(product);
        invoiceDetail1.setQuantity(2);
        invoiceDetail1.setPrice(50.0f);

        Invoice invoice1 = new Invoice();
        Set<InvoiceDetail> details1 = new HashSet<>();
        details1.add(invoiceDetail1);
        invoice1.setInvoiceDetails(details1);

        // Use mocks for repository calls
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(invoiceRepository.findAll()).thenReturn(List.of(invoice1));

        // Act
        float totalAmount = productService.calculateTotalInvoiceAmountForProduct(1L);

        // Assert
        assertEquals(100.0f, totalAmount);
        verify(productRepository, times(1)).findById(1L); // Verifies the mock was called once
    }

    // Another Mockito test example
    @Test
    void testGetLowStockProductsWithMockito() {
        // Arrange
        Product product1 = new Product();
        product1.setIdProduct(1L);
        product1.setQuantity(5);

        Product product2 = new Product();
        product2.setIdProduct(2L);
        product2.setQuantity(20);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // Act
        List<Product> lowStockProducts = productService.getLowStockProducts(10);

        // Assert
        assertEquals(1, lowStockProducts.size());
        assertEquals(product1.getIdProduct(), lowStockProducts.get(0).getIdProduct());
        verify(productRepository, times(1)).findAll();
    }
}
