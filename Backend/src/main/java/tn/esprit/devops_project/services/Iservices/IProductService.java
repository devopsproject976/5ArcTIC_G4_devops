package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;

import java.util.List;

public interface IProductService {

    Product addProduct(Product product, Long idStock);

    // 2. Mettre à jour un produit existant
    Product updateProduct(Long productId, Product updatedProduct);

    // 4. Calculer les produits en rupture de stock
    List<Product> findLowStockProducts(int threshold);

    // 5. Rechercher des produits par titre
    List<Product> searchProductsByTitle(String title);

    // 6. Rechercher des produits dans une plage de prix
    List<Product> searchProductsByPriceRange(float minPrice, float maxPrice);

    Product retrieveProduct(Long id);
    List<Product> retreiveAllProduct();
    List<Product> retrieveProductByCategory(ProductCategory category);
    void deleteProduct(Long id);
    List<Product> retreiveProductStock(Long id);


}
