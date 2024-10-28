package tn.esprit.devops_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.StockServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class StockServiceMockitoTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;


    @InjectMocks
    StockServiceImpl stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }




    /*@Test
    void testAddStock() {
        // Create a stock object to add
        Stock stock = new Stock();
        stock.setTitle("Mock Stock");
        stock.setThreshold(10);

        // Mock the behavior of stockRepository.save
        when(stockRepository.save(stock)).thenReturn(stock);

        // Call the addStock method
        Stock savedStock = stockService.addStock(stock);

        // Verify the save method is called
        verify(stockRepository).save(stock);
        assertNotNull(savedStock.getIdStock(), "Saved stock ID should not be null");
        assertEquals("Mock Stock", savedStock.getTitle());
        System.out.println("Stock saved: " + savedStock.getTitle());
    }

    // Test for retrieving a stock by ID
    @Test
    void testRetrieveStock() {
        // Create a stock object
        Stock stock = new Stock();
        stock.setIdStock(1L);
        stock.setTitle("Mock Stock");

        // Mock the behavior of stockRepository.findById
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        // Call the retrieveStock method
        Stock retrievedStock = stockService.retrieveStock(1L);

        // Verify the findById method is called
        verify(stockRepository).findById(1L);
        assertNotNull(retrievedStock, "Retrieved stock should not be null");
        assertEquals(1L, retrievedStock.getIdStock());
        System.out.println("Stock retrieved: " + retrievedStock.getTitle());
    }

    // Test for retrieving all stocks
    @Test
    void testRetrieveAllStocks() {
        // Create a list of mock stocks
        Stock stock1 = new Stock();
        stock1.setTitle("Mock Stock 1");
        Stock stock2 = new Stock();
        stock2.setTitle("Mock Stock 2");
        List<Stock> mockStocks = Arrays.asList(stock1, stock2);

        // Mock the behavior of stockRepository.findAll
        when(stockRepository.findAll()).thenReturn(mockStocks);

        // Call the retrieveAllStock method
        List<Stock> retrievedStocks = stockService.retrieveAllStock();

        // Verify the findAll method is called
        verify(stockRepository).findAll();
        assertNotNull(retrievedStocks, "Retrieved stocks should not be null");
        assertEquals(2, retrievedStocks.size(), "Should retrieve 2 stocks");
        System.out.println("All stocks retrieved: " + retrievedStocks.size());
    }

    // Test for handling "Stock not found" when retrieving stock by ID
    @Test
    void testRetrieveStockNotFound() {
        // Mock the behavior of stockRepository.findById to return an empty Optional
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the retrieveStock method and check that the exception is thrown
        assertThrows(NullPointerException.class, () -> stockService.retrieveStock(1L), "Stock not found");
        verify(stockRepository).findById(1L);
        System.out.println("Stock not found exception thrown as expected.");
    }

    // Test for deleting a stock
    @Test
    void testDeleteStock() {
        // Create a stock object
        Stock stock = new Stock();
        stock.setIdStock(1L);
        stock.setTitle("Mock Stock");

        // Mock the behavior of stockRepository.findById to return the stock
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        // Call the deleteStock method
        stockService.deleteStock(1L);

        // Verify that delete method was called
        verify(stockRepository).delete(stock);
        System.out.println("Stock deleted with ID: " + stock.getIdStock());
    }


    @Test
    void testUpdateStock() {
        Stock existingStock = new Stock();
        existingStock.setIdStock(1L);
        existingStock.setTitle("Old Title");
        existingStock.setThreshold(5);

        Stock updatedStockDetails = new Stock();
        updatedStockDetails.setTitle("New Title");
        updatedStockDetails.setThreshold(10);

        when(stockRepository.findById(1L)).thenReturn(Optional.of(existingStock));
        when(stockRepository.save(existingStock)).thenReturn(existingStock);

        Stock updatedStock = stockService.updateStock(1L, updatedStockDetails);

        assertEquals("New Title", updatedStock.getTitle());
        assertEquals(10, updatedStock.getThreshold());
        verify(stockRepository).findById(1L);
        verify(stockRepository).save(existingStock);
    }

    // Test for handling stock not found
    @Test
    void testUpdateStockNotFound() {
        Stock updatedStockDetails = new Stock();
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> stockService.updateStock(1L, updatedStockDetails));
    }*/



    @Test
    void testRetrieveLowStockProducts_Mockito() {
        // Create a stock object with a dynamic threshold
        Stock stock = new Stock();
        stock.setIdStock(1L);
        stock.setThreshold(10); // Set the dynamic threshold for the stock

        // Create a low stock product with an ID
        Product product1 = new Product();
        product1.setIdProduct(1L); // Set the product ID
        product1.setQuantity(3); // Low stock

        // Create another product with sufficient stock and an ID
        Product product2 = new Product();
        product2.setIdProduct(2L); // Set the product ID
        product2.setQuantity(15); // Not low stock

        // Mock the repository calls
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock)); // Mock finding the stock with a threshold
        when(productRepository.findByStockIdStock(1L)).thenReturn(List.of(product1, product2)); // Mock finding products

        // Call the method under test (no threshold parameter now, as it is dynamic)
        List<Product> lowStockProducts = stockService.retrieveLowStockProducts(1L);

        // Validate that only the low-stock product is returned
        assertEquals(1, lowStockProducts.size());
        assertEquals(product1.getIdProduct(), lowStockProducts.get(0).getIdProduct());
        assertEquals(product1.getQuantity(), lowStockProducts.get(0).getQuantity());


    }


    @Test
    void testAdjustPricingBasedOnStock_Mockito_Combined() {
        // Create a mock Stock object with a threshold of 10
        Stock mockStock = new Stock();
        mockStock.setTitle("MockPerfumeStock");
        mockStock.setThreshold(10); // Set a threshold of 10

        // Create a mock Product object with quantity below the threshold (price should increase)
        Product mockProductBelowThreshold = new Product();
        mockProductBelowThreshold.setTitle("MockProductBelowThreshold");
        mockProductBelowThreshold.setPrice(100.0f);
        mockProductBelowThreshold.setQuantity(5); // Below threshold
        mockProductBelowThreshold.setStock(mockStock);

        // Mock the behavior of productRepository for product below the threshold
        when(productRepository.findById(mockProductBelowThreshold.getIdProduct()))
                .thenReturn(Optional.of(mockProductBelowThreshold));

        // Test case for price increase (below threshold)
        stockService.adjustPricingBasedOnStock(mockProductBelowThreshold.getIdProduct());
        assertEquals(110.0f, mockProductBelowThreshold.getPrice(), 0.01); // Check price increased by 10%
        System.out.println("Price increased to: " + mockProductBelowThreshold.getPrice());

        // Verify that the repository's save method was called for the product
        verify(productRepository).save(mockProductBelowThreshold);
        System.out.println("Product repository save method was called for the product below the threshold.");


        // Create another mock Product object with quantity above the threshold (price should decrease)
        Product mockProductAboveThreshold = new Product();
        mockProductAboveThreshold.setTitle("MockProductAboveThreshold");
        mockProductAboveThreshold.setPrice(100.0f);
        mockProductAboveThreshold.setQuantity(15); // Above threshold
        mockProductAboveThreshold.setStock(mockStock);

        // Mock the behavior of productRepository for product above the threshold
        when(productRepository.findById(mockProductAboveThreshold.getIdProduct()))
                .thenReturn(Optional.of(mockProductAboveThreshold));

        // Test case for price decrease (above threshold)
        stockService.adjustPricingBasedOnStock(mockProductAboveThreshold.getIdProduct());
        assertEquals(90.0f, mockProductAboveThreshold.getPrice(), 0.01); // Check price decreased by 10%
        System.out.println("Price decreased to: " + mockProductAboveThreshold.getPrice());

        // Verify that the repository's save method was called for the product
        verify(productRepository).save(mockProductAboveThreshold);
        System.out.println("Product repository save method was called for the product above the threshold.");
    }



}
