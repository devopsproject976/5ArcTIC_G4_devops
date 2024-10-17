package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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

   private final StockRepository stockRepository;

    private final ProductRepository productRepository;
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


    public List<Product> retrieveLowStockProducts(Long stockId, int threshold) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new NullPointerException("Stock not found"));
        List<Product> products = productRepository.findByStockIdStock(stockId);

        return products.stream()
                .filter(product -> product.getQuantity() < threshold)
                .collect(Collectors.toList());
    }



}
