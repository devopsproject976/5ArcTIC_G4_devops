package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.services.Iservices.IStockService;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.StockRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockServiceImpl implements IStockService {

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);


    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;
    @Override
    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public Stock retrieveStock(Long id) {
        return stockRepository.findById(id).orElseThrow(() -> new NullPointerException("Stock not found"));
    }

    @Override
    public List<Stock> retrieveAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public Stock updateStock(Long id, Stock updatedStock) {
        Stock existingStock = stockRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Stock not found"));

        existingStock.setTitle(updatedStock.getTitle());
        existingStock.setThreshold(updatedStock.getThreshold());

        return stockRepository.save(existingStock);
    }


    @Override
    public void deleteStock(Long id) {
        Stock stock = retrieveStock(id); // This will throw an exception if not found
        stockRepository.delete(stock);
    }



   /* public List<Product> retrieveLowStockProducts(Long stockId, int threshold) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new NullPointerException("Stock not found"));
        List<Product> products = productRepository.findByStockIdStock(stockId);

        return products.stream()
                .filter(product -> product.getQuantity() < threshold)
                .collect(Collectors.toList());
   }*/

   /* public List<Product> retrieveLowStockProducts(Long stockId, int threshold) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new NullPointerException("Stock not found"));
        List<Product> products = productRepository.findByStockIdStock(stockId);

        // Log a message if any product is below the stock threshold
        return products.stream()
                .filter(product -> {
                    if (product.getQuantity() < threshold) {
                        logger.warn("Product with ID {} is low on stock. Quantity: {}", product.getIdProduct(), product.getQuantity());
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }*/

    public List<Product> retrieveLowStockProducts(Long stockId) {
        // Retrieve the stock by ID, or throw an exception if it doesn't exist
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new NullPointerException("Stock not found"));

        // Fetch the dynamic threshold for this stock
        int threshold = stock.getThreshold();

        // Fetch the products associated with this stock
        List<Product> products = productRepository.findByStockIdStock(stockId);

        // Log a message if any product is below the dynamic stock threshold
        return products.stream()
                .filter(product -> {
                    if (product.getQuantity() < threshold) {
                        logger.warn("Product with ID {} is low on stock. Quantity: {}", product.getIdProduct(), product.getQuantity());
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }


    public void adjustPricingBasedOnStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NullPointerException("Product not found"));

        int threshold = product.getStock().getThreshold();

        if (product.getQuantity() < threshold) {
            product.setPrice(product.getPrice() * 1.1f); // Increase price by 10%
        } else {
            product.setPrice(product.getPrice() * 0.9f); // Decrease price by 10%
        }

        productRepository.save(product); // Save updated price
        logger.info("Price adjusted for product {}: {}", product.getTitle(), product.getPrice());
    }





}
