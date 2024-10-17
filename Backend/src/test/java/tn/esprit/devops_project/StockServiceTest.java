package tn.esprit.devops_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.StockServiceImpl;

import java.util.List;
import java.util.Optional;

public class StockServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    StockRepository stockRepository;

    @InjectMocks
    StockServiceImpl stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


   /* @Test
    void testRetrieveLowStockProducts_JUnit() {
        Stock stock = new Stock();
        stock.setIdStock(1L);
        stockRepository.save(stock);

        Product product1 = new Product();
        product1.setPrice(50.0f);
        product1.setQuantity(3); // Low stock
        product1.setStock(stock);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setPrice(20.0f);
        product2.setQuantity(15); // Not low stock
        product2.setStock(stock);
        productRepository.save(product2);

        List<Product> lowStockProducts = stockService.retrieveLowStockProducts(1L, 10);

        assertEquals(1, lowStockProducts.size());
        assertEquals(product1.getIdProduct(), lowStockProducts.get(0).getIdProduct());
    }*/



    @Test
    void testRetrieveLowStockProducts_JUnit() {
        // Create a stock object
        Stock stock = new Stock();
        stock.setIdStock(1L);

        // Create low stock product
        Product product1 = new Product();
        product1.setIdProduct(1L);
        product1.setPrice(50.0f);
        product1.setQuantity(3); // Low stock
        product1.setStock(stock);

        // Create another product with sufficient stock
        Product product2 = new Product();
        product2.setIdProduct(2L);
        product2.setPrice(20.0f);
        product2.setQuantity(15); // Not low stock
        product2.setStock(stock);

        // Mock the repository calls
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(productRepository.findByStockIdStock(1L)).thenReturn(List.of(product1, product2));

        // Call the method under test
        List<Product> lowStockProducts = stockService.retrieveLowStockProducts(1L, 10);

        // Validate the results
        assertEquals(1, lowStockProducts.size());
        assertEquals(product1.getIdProduct(), lowStockProducts.get(0).getIdProduct());
    }





    @Test
    void testRetrieveLowStockProducts_Mockito() {
        Stock stock = new Stock();
        stock.setIdStock(1L);

        Product product1 = new Product();
        product1.setQuantity(3); // Low stock

        Product product2 = new Product();
        product2.setQuantity(15); // Not low stock

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(productRepository.findByStockIdStock(1L)).thenReturn(List.of(product1, product2));

        List<Product> lowStockProducts = stockService.retrieveLowStockProducts(1L, 10);

        assertEquals(1, lowStockProducts.size());
        assertEquals(product1.getQuantity(), lowStockProducts.get(0).getQuantity());
    }

}
