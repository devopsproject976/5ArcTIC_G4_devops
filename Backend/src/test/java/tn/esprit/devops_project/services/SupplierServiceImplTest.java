package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupplierServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Stock stock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        stock = new Stock();
        stock.setIdStock(1L);
        stock.setTitle("Test Stock");

        product = new Product();
        product.setIdProduct(1L);
        product.setTitle("Test Product");
        product.setPrice(50);
        product.setQuantity(5);
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setStock(stock);
    }

    @Test
    void testAddProduct() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.addProduct(product, 1L);

        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getTitle());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        product.setTitle("Updated Title");
        product.setPrice(150);

        Product updatedProduct = productService.updateProduct(product.getIdProduct(), product);

        assertNotNull(updatedProduct);
        assertEquals("Updated Title", updatedProduct.getTitle());
        assertEquals(150, updatedProduct.getPrice());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testSellProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.sellProduct(product.getIdProduct(), 2);

        assertEquals(3, product.getQuantity());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testFindLowStockProducts() {
        when(productRepository.findByQuantityLessThan(5)).thenReturn(Arrays.asList(product));

        List<Product> lowStockProducts = productService.findLowStockProducts(5);

        assertNotNull(lowStockProducts);
        assertEquals(1, lowStockProducts.size());
        verify(productRepository, times(1)).findByQuantityLessThan(5);
    }

    @Test
    void testSearchProductsByTitle() {
        when(productRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(Arrays.asList(product));

        List<Product> foundProducts = productService.searchProductsByTitle("Test");

        assertNotNull(foundProducts);
        assertEquals(1, foundProducts.size());
        verify(productRepository, times(1)).findByTitleContainingIgnoreCase("Test");
    }

    @Test
    void testSearchProductsByPriceRange() {
        when(productRepository.findByPriceBetween(30, 60)).thenReturn(Arrays.asList(product));

        List<Product> foundProducts = productService.searchProductsByPriceRange(30, 60);

        assertNotNull(foundProducts);
        assertEquals(1, foundProducts.size());
        verify(productRepository, times(1)).findByPriceBetween(30, 60);
    }

    @Test
    void testRetrieveProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.retrieveProduct(1L);

        assertNotNull(foundProduct);
        assertEquals(product.getTitle(), foundProduct.getTitle());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<Product> products = productService.retreiveAllProduct();

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveProductByCategory() {
        when(productRepository.findByCategory(ProductCategory.ELECTRONICS)).thenReturn(Arrays.asList(product));

        List<Product> products = productService.retrieveProductByCategory(ProductCategory.ELECTRONICS);

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findByCategory(ProductCategory.ELECTRONICS);
    }

    @Test
    void testDeleteProduct() {
        productService.deleteProduct(product.getIdProduct());

        verify(productRepository, times(1)).deleteById(product.getIdProduct());
    }

    @Test
    void testRetrieveProductStock() {
        when(productRepository.findByStockIdStock(1L)).thenReturn(Arrays.asList(product));

        List<Product> products = productService.retreiveProductStock(1L);

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findByStockIdStock(1L);
    }
}
