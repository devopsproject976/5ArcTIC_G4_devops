package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.Iservices.IProductService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    // 1. Ajouter un produit avec gestion du stock
    @Override
    public Product addProduct(Product product, Long stockId) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new NullPointerException("Stock not found"));
        product.setStock(stock);
        return productRepository.save(product);
    }

    // 2. Mettre à jour un produit existant
    @Override
    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NullPointerException("Product not found"));
        existingProduct.setTitle(updatedProduct.getTitle());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());
        return productRepository.save(existingProduct);
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

    // Récupérer un produit par ID
    @Override
    public Product retrieveProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NullPointerException("Product not found"));
    }

    // Récupérer tous les produits
    @Override
    public List<Product> retreiveAllProduct() {
        return productRepository.findAll();
    }

    // Récupérer des produits par catégorie
    @Override
    public List<Product> retrieveProductByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }

    // Supprimer un produit
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Récupérer des produits associés à un stock
    @Override
    public List<Product> retreiveProductStock(Long stockId) {
        return productRepository.findByStockIdStock(stockId);
    }
}
