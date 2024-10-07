package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.services.Iservices.IProductService;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService {

    final ProductRepository productRepository;
    final StockRepository stockRepository;
    final InvoiceRepository invoiceRepository;

    @Override
    public Product addProduct(Product product, Long idStock) {
        Stock stock = stockRepository.findById(idStock).orElseThrow(() -> new NullPointerException("stock not found"));
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

    // Advanced Service 1: Calculate Total Invoice Amount for a Product
    public float calculateTotalInvoiceAmountForProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NullPointerException("Product not found"));

        float totalAmount = 0;

        // Loop through all invoices and get their details
        List<Invoice> invoices = invoiceRepository.findAll();

        for (Invoice invoice : invoices) {
            Set<InvoiceDetail> invoiceDetails = invoice.getInvoiceDetails();
            for (InvoiceDetail detail : invoiceDetails) {
                if (detail.getProduct().getIdProduct().equals(product.getIdProduct())) {
                    totalAmount += detail.getQuantity() * detail.getPrice();
                }
            }
        }

        return totalAmount;
    }

    // Advanced Service 2: Generate Stock Alert for Low Inventory Products
    public List<Product> getLowStockProducts(int threshold) {
        List<Product> products = productRepository.findAll();

        // Filter products based on quantity
        return products.stream()
                .filter(product -> product.getQuantity() < threshold)
                .collect(Collectors.toList());
    }
}
