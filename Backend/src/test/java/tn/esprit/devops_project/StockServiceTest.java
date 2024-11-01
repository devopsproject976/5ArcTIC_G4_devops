package tn.esprit.devops_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.StockServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
//@Transactional
@SpringBootTest

public class StockServiceTest {

    /*@Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockServiceImpl stockService;



    @Transactional
    @Test
    void testRetrieveLowStockProducts_JUnit() {
        // Create a stock object with a dynamic threshold and save it to the database
        Stock stock = new Stock();
        stock.setThreshold(10); // Set the dynamic threshold for the stock

        Stock savedStock = stockRepository.save(stock); // Save the stock to the database
        System.out.println("Saved stock: " + savedStock); // Debug log

        // Ensure that the stock is saved correctly
        assertNotNull(savedStock, "Stock should not be null after saving");
        assertNotNull(savedStock.getIdStock(), "Saved stock ID should not be null");

        // Create a low stock product and save it to the database
        Product product1 = new Product();
        product1.setPrice(50.0f);
        product1.setQuantity(3); // Low stock based on threshold
        product1.setStock(savedStock);
        productRepository.save(product1); // Save product1 to the database

        // Create another product with sufficient stock and save it to the database
        Product product2 = new Product();
        product2.setPrice(20.0f);
        product2.setQuantity(15); // Not low stock based on threshold
        product2.setStock(savedStock);
        productRepository.save(product2); // Save product2 to the database

        // Call the method under test
        List<Product> lowStockProducts = stockService.retrieveLowStockProducts(savedStock.getIdStock());

        // Validate the results
        assertEquals(1, lowStockProducts.size());
        assertEquals(product1.getIdProduct(), lowStockProducts.get(0).getIdProduct());
    }*/







    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockServiceImpl stockService;



    private Stock stock;

    @BeforeEach
    void setUp() {
        // Create a new stock object for testing
        stock = new Stock();
        stock.setTitle("Test Stock");
        stock.setThreshold(10);
    }

    @AfterEach
    void tearDown() {
        // Clean up the stock after each test
        stockRepository.deleteAll();
    }


    @Test
    void testAddStock() {
        Stock savedStock = stockService.addStock(stock);
        assertNotNull(savedStock.getIdStock(), "Saved stock ID should not be null");
        assertEquals("Test Stock", savedStock.getTitle());
        System.out.println("Stock saved: " + savedStock.getTitle());
    }

    // Test for retrieving a stock by ID
    @Test
    void testRetrieveStock() {
        Stock savedStock = stockService.addStock(stock);
        Stock retrievedStock = stockService.retrieveStock(savedStock.getIdStock());
        assertNotNull(retrievedStock, "Retrieved stock should not be null");
        assertEquals(savedStock.getIdStock(), retrievedStock.getIdStock());
        System.out.println("Stock retrieved: " + retrievedStock.getTitle());
    }

    // Test for retrieving all stocks
    @Test
    void testRetrieveAllStocks() {
        stockService.addStock(stock);
        Stock anotherStock = new Stock();
        anotherStock.setTitle("Another Stock");
        anotherStock.setThreshold(20);
        stockService.addStock(anotherStock);

        List<Stock> stocks = stockService.retrieveAllStock();
        assertEquals(2, stocks.size(), "There should be 2 stocks in the repository");
        System.out.println("All stocks retrieved: " + stocks.size());
    }

    @Test
    void testUpdateStock() {
        // Create a new stock and save it
        Stock savedStock = stockService.addStock(stock);

        // Create an updated stock object
        Stock updatedStock = new Stock();
        updatedStock.setTitle("Updated Stock");
        updatedStock.setThreshold(20);

        // Call the updateStock method
        Stock result = stockService.updateStock(savedStock.getIdStock(), updatedStock);

        // Verify that the result has the updated values
        assertEquals("Updated Stock", result.getTitle());
        assertEquals(20, result.getThreshold());
        System.out.println("Stock updated to: " + result.getTitle());
    }


    // Test for deleting a stock
    @Test
    void testDeleteStock() {
        Stock savedStock = stockService.addStock(stock);
        stockService.deleteStock(savedStock.getIdStock());
        assertThrows(NullPointerException.class, () -> stockService.retrieveStock(savedStock.getIdStock()), "Should throw exception when retrieving deleted stock");
        System.out.println("Stock deleted with ID: " + savedStock.getIdStock());
    }





@Test
    void testRetrieveLowStockProducts_JUnit() {
        // Create a stock object with a dynamic threshold and save it to the database
        Stock stock = new Stock();
        stock.setTitle("Clothes");
        stock.setThreshold(10); // Set the dynamic threshold for the stock

        Stock savedStock = stockRepository.save(stock); // Save the stock to the database
        System.out.println("Saved stock: " + savedStock.getTitle()); // Debug log

        // Ensure that the stock is saved correctly
        assertNotNull(savedStock, "Stock should not be null after saving");
        assertNotNull(savedStock.getIdStock(), "Saved stock ID should not be null");

        // Create a low stock product and save it to the database
        Product product1 = new Product();
        product1.setTitle("Robes");
        product1.setPrice(50.0f);
        product1.setQuantity(3); // Low stock based on threshold
        product1.setStock(savedStock);
        productRepository.save(product1); // Save product1 to the database
        System.out.println("Saved product1: " + product1.getTitle());

        // Create another product with sufficient stock and save it to the database
        Product product2 = new Product();
        product2.setTitle("chemise");
        product2.setPrice(20.0f);
        product2.setQuantity(15); // Not low stock based on threshold
        product2.setStock(savedStock);
        productRepository.save(product2); // Save product2 to the database
        System.out.println("Saved product2: " + product2.getTitle());

        // Call the method under test
        List<Product> lowStockProducts = stockService.retrieveLowStockProducts(savedStock.getIdStock());

        // Validate the results
        assertEquals(1, lowStockProducts.size());
        assertEquals(product1.getIdProduct(), lowStockProducts.get(0).getIdProduct());
    // Cleanup: Delete the products and the stock
    productRepository.delete(product1); // Delete product1
    productRepository.delete(product2); // Delete product2
    stockRepository.delete(savedStock); // Delete the stock
    System.out.println("deleted stock and products: " + savedStock.getTitle() +" "+ product1.getTitle() +" "+ product2.getTitle());
    }




    @Test
    void testAdjustPricingBasedOnStock() {
        // Create a stock object with a threshold and save it to the database
        Stock stock = new Stock();
        stock.setTitle("PerfumeStock");
        stock.setThreshold(10); // Set the threshold for the stock
        stockRepository.save(stock);
        System.out.println("Stock created and saved with title: " + stock.getTitle() + " and threshold: " + stock.getThreshold());

        // Create a product with quantity below the threshold (price should increase)
        Product productLowStock = new Product();
        productLowStock.setTitle("BaccaratRouge");
        productLowStock.setPrice(100.0f);
        productLowStock.setQuantity(5); // Below threshold, price should increase
        productLowStock.setStock(stock);
        Product savedProductLowStock = productRepository.save(productLowStock);
        System.out.println("Product with low stock saved: " + savedProductLowStock.getTitle() + " with quantity: " + savedProductLowStock.getQuantity());

        // Create a product with quantity above the threshold (price should decrease)
        Product productHighStock = new Product();
        productHighStock.setTitle("GoodGirl");
        productHighStock.setPrice(50.0f);
        productHighStock.setQuantity(15); // Above threshold, price should decrease
        productHighStock.setStock(stock);
        Product savedProductHighStock = productRepository.save(productHighStock);
        System.out.println("Product with high stock saved: " + savedProductHighStock.getTitle() + " with quantity: " + savedProductHighStock.getQuantity());

        // Adjust pricing for both products
        stockService.adjustPricingBasedOnStock(savedProductLowStock.getIdProduct());
        System.out.println("Price adjusted for low stock product: " + savedProductLowStock.getTitle());

        stockService.adjustPricingBasedOnStock(savedProductHighStock.getIdProduct());
        System.out.println("Price adjusted for high stock product: " + savedProductHighStock.getTitle());

        // Fetch updated products from the database
        Product updatedLowStockProduct = productRepository.findById(savedProductLowStock.getIdProduct()).get();
        Product updatedHighStockProduct = productRepository.findById(savedProductHighStock.getIdProduct()).get();

        // Verify if the price has increased by 10% for the product below threshold
        System.out.println("Verifying price for low stock product...");
        assertEquals(110.0f, updatedLowStockProduct.getPrice(), 0.01);
        System.out.println("Low stock product price verified. New price: " + updatedLowStockProduct.getPrice());

        // Verify if the price has decreased by 10% for the product above threshold
        System.out.println("Verifying price for high stock product...");
        assertEquals(45.0f, updatedHighStockProduct.getPrice(), 0.01);
        System.out.println("High stock product price verified. New price: " + updatedHighStockProduct.getPrice());

        // Cleanup: Delete the stock and products
        productRepository.delete(savedProductLowStock);
        System.out.println("Low stock product deleted: " + savedProductLowStock.getTitle());

        productRepository.delete(savedProductHighStock);
        System.out.println("High stock product deleted: " + savedProductHighStock.getTitle());

        stockRepository.delete(stock);
        System.out.println("Stock deleted: " + stock.getTitle());
    }



}
