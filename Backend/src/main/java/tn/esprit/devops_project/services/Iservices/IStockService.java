package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.entities.StockLowProductsDTO;

import java.util.List;

public interface IStockService {

    Stock addStock(Stock stock);
    Product addProduct(Product product, Long idStock);
    Stock retrieveStock(Long id);
    List<Stock> retrieveAllStock();
    Stock updateStock(Long id, Stock updatedStock);
    void deleteStock(Long id);
     List<Product> retrieveLowStockProducts(Long stockId);
    List<StockLowProductsDTO> retrieveStocksWithLowProducts();
    void adjustPricingBasedOnStock(Long productId);

}
