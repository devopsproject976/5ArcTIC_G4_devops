package tn.esprit.devops_project.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.entities.StockLowProductsDTO;
import tn.esprit.devops_project.services.Iservices.IProductService;
import tn.esprit.devops_project.services.Iservices.IStockService;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class StockController {

    IStockService stockService;
    IProductService productService;

    @PostMapping("/stock")
    Stock addStock(@RequestBody Stock stock){
        return stockService.addStock(stock);
    }

    @PostMapping("/stock/productAdd/{idStock}")
    Product addProduct(@RequestBody Product product,@PathVariable Long idStock){
        return productService.addProduct(product,idStock);
    }

    @GetMapping("/stock/{id}")
    Stock retrieveStock(@PathVariable Long id){
        return stockService.retrieveStock(id);
    }

    @GetMapping("/stock")
    List<Stock> retrieveAllStock(){
        return stockService.retrieveAllStock();
    }

    @PutMapping("/stock/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @RequestBody Stock updatedStock) {
        Stock updated = stockService.updateStock(id, updatedStock);
        return ResponseEntity.ok(updated);
    }

    // Delete a stock item by ID
    @DeleteMapping("/stock/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stock/{stockId}/low-stock-products")
    public ResponseEntity<List<Product>> getLowStockProducts(@PathVariable Long stockId) {
        List<Product> lowStockProducts = stockService.retrieveLowStockProducts(stockId);
        return ResponseEntity.ok(lowStockProducts);
    }

    @GetMapping("/stock/low-stock")
    public ResponseEntity<List<StockLowProductsDTO>> getStocksWithLowProducts() {
        List<StockLowProductsDTO> stocksWithLowProducts = stockService.retrieveStocksWithLowProducts();
        return ResponseEntity.ok(stocksWithLowProducts);
    }

    @PutMapping("/stock/products/{productId}/adjust-price")
    public ResponseEntity<String> adjustProductPricing(@PathVariable Long productId) {
        stockService.adjustPricingBasedOnStock(productId);
        return ResponseEntity.ok("Price adjustment completed for product ID: " + productId);
    }



}
