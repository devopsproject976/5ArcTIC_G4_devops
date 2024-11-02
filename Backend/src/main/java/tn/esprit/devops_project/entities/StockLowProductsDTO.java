package tn.esprit.devops_project.entities;

import java.util.List;

public class StockLowProductsDTO {
    private Long idStock;
    private String title;
    private List<Product> lowStockProducts;

    // Constructor
    public StockLowProductsDTO(Long idStock, String title, List<Product> lowStockProducts) {
        this.idStock = idStock;
        this.title = title;
        this.lowStockProducts = lowStockProducts;
    }

    // Getters and Setters
    public Long getIdStock() { return idStock; }
    public void setIdStock(Long idStock) { this.idStock = idStock; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Product> getLowStockProducts() { return lowStockProducts; }
    public void setLowStockProducts(List<Product> lowStockProducts) { this.lowStockProducts = lowStockProducts; }

}
