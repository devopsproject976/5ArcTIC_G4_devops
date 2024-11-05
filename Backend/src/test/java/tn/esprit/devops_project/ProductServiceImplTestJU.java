package tn.esprit.devops_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.ProductServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceImplTestJU {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductServiceImpl productService;

    private Product product;
    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setTitle("Test Stock");
        stock = stockRepository.save(stock);

        product = new Product();
        product.setTitle("Test Product");
        product.setPrice(50);
        product.setQuantity(5);
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setStock(stock);

        product = productRepository.save(product);
    }

    @Test
    void testAddProduct() {
        Product newProduct = new Product();
        newProduct.setTitle("New Product");
        newProduct.setPrice(100);
        newProduct.setQuantity(10);
        newProduct.setCategory(ProductCategory.BOOKS);

        Product savedProduct = productService.addProduct(newProduct, stock.getIdStock());

        assertNotNull(savedProduct);
        assertEquals("New Product", savedProduct.getTitle());
        assertEquals(100, savedProduct.getPrice());
    }



    @Test
    void testSellProduct() {
        int initialQuantity = product.getQuantity();
        productService.sellProduct(product.getIdProduct(), 2);

        Product updatedProduct = productRepository.findById(product.getIdProduct()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(initialQuantity - 2, updatedProduct.getQuantity());
    }

    @Test
    void testFindLowStockProducts() {
        product.setQuantity(1);  // Set low stock
        productRepository.save(product);

        List<Product> lowStockProducts = productService.findLowStockProducts(5); // Threshold is 5
        assertTrue(lowStockProducts.contains(product));
    }

    @Test
    void testSearchProductsByTitle() {
        List<Product> foundProducts = productService.searchProductsByTitle("Test");

        assertFalse(foundProducts.isEmpty());
        assertEquals(product.getTitle(), foundProducts.get(0).getTitle());
    }

    @Test
    void testSearchProductsByPriceRange() {
        List<Product> foundProducts = productService.searchProductsByPriceRange(30, 60);

        assertFalse(foundProducts.isEmpty());
        assertTrue(foundProducts.get(0).getPrice() >= 30 && foundProducts.get(0).getPrice() <= 60);
    }

    @Test
    void testRetrieveProduct() {
        Product foundProduct = productService.retrieveProduct(product.getIdProduct());

        assertNotNull(foundProduct);
        assertEquals(product.getTitle(), foundProduct.getTitle());
    }

    @Test
    void testRetrieveAllProducts() {
        List<Product> products = productService.retreiveAllProduct();

        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    void testRetrieveProductByCategory() {
        List<Product> products = productService.retrieveProductByCategory(ProductCategory.ELECTRONICS);

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(ProductCategory.ELECTRONICS, products.get(0).getCategory());
    }

    @Test
    void testDeleteProduct() {
        productService.deleteProduct(product.getIdProduct());

        assertFalse(productRepository.findById(product.getIdProduct()).isPresent());
    }

    @Test
    void testRetrieveProductStock() {
        List<Product> products = productService.retreiveProductStock(stock.getIdStock());

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(stock.getIdStock(), products.get(0).getStock().getIdStock());
    }
}