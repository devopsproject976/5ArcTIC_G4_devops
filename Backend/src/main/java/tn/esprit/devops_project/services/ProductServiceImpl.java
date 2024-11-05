package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.Iservices.IProductService;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService {

    final ProductRepository productRepository;
     final StockRepository stockRepository;
    final InvoiceRepository invoiceRepository;
    final SupplierRepository supplierRepository;





    @Override
    public Product addProduct(Product product, Long idStock) {
        Stock stock = stockRepository.findById(idStock).orElseThrow(() -> new NullPointerException("Stock not found"));
        product.setStock(stock);
        return productRepository.save(product);
    }

    @Override
    public Product retrieveProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NullPointerException("Product not found"));
    }

    @Override
    public List<Product> retreiveAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> retrieveProductByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> retreiveProductStock(Long id) {
        return productRepository.findByStockIdStock(id);
    }

    // 3. Gérer la vente d'un produit et mettre à jour le stock
    @Transactional
    public void sellProduct(Long productId, int quantitySold) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NullPointerException("Product not found"));

        if (product.getQuantity() < quantitySold) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getTitle());
        }
        product.setQuantity(product.getQuantity() - quantitySold);
        productRepository.save(product);
    }

    // 4. Calculer les produits en rupture de stock
    @Override
    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }

    // 5. Rechercher des produits par titre
    @Override
    public List<Product> searchProductsByTitle(String title) {
        return productRepository.findByTitleContainingIgnoreCase(title);
    }

    // 6. Rechercher des produits dans une plage de prix
    @Override
    public List<Product> searchProductsByPriceRange(float minPrice, float maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }



    public float calculateTotalPrice(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Supplier supplier = product.getStock().getSupplier();
        float price = product.getPrice() * quantity;

        if (supplier.getSupplierCategory() == SupplierCategory.CONVENTIONNE) {
            price *= 0.9; // 10% discount
        }

        // Assume we have a method to get discount and tax rates
        Discount discount = getDiscountForProduct(product);
        if (discount != null) {
            price *= (1 - discount.getPercentage());
        }

        float taxRate = getTaxRateForProduct(product);
        price *= (1 + taxRate);

        return price;
    }

    private Discount getDiscountForProduct(Product product) {
        // Example logic: return a discount based on product category
        if (product.getCategory() == ProductCategory.ELECTRONICS) {
            return new Discount(0.1f); // 10% discount for electronics
        }
        return null; // No discount for other categories
    }

    private float getTaxRateForProduct(Product product) {
        // Example logic: return a tax rate based on product category
        if (product.getCategory() == ProductCategory.ELECTRONICS) {
            return 0.15f; // 15% tax for electronics
        }
        return 0.10f; // Default tax rate for other categories
    }



}
