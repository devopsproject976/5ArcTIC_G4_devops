import {Component, OnInit} from '@angular/core';
import {StockService} from '../services/stock.service';

@Component({
  selector: 'app-low-stock',
  templateUrl: './low-stock.component.html',
  styleUrls: ['./low-stock.component.css']
})
export class LowStockComponent implements OnInit {
  lowStockData: any[] = [];
  lowStockProducts: any[] = [];

  constructor(private stockService: StockService) {}

  ngOnInit() {
    this.fetchLowStock();
  }

  fetchLowStock() {
    this.stockService.fetchLowStock().subscribe((response) => {
      console.log('Low stock data:', response); // Log the response for debugging
      this.lowStockData = response;
    }, (error) => {
      console.error("Error fetching low stock products:", error);
    });
  }


  getLowStockProducts(stockId: number) {
    this.stockService.getLowStockProducts(stockId).subscribe(
      products => {
        this.lowStockProducts = products; // Store the retrieved low stock products
        console.log('Low stock products for stock ID', stockId, ':', this.lowStockProducts);
      },
      error => {
        console.error('Error fetching low stock products:', error);
      }
    );
  }


  adjustPrice(productId: number) {
    this.stockService.adjustProductPrice(productId).subscribe(
      response => {
        console.log(response); // Log the response
        alert('Price adjustment successful for product ID: ' + productId);
        this.fetchLowStock(); // Refresh the low stock data after adjustment
      },
      error => {
        console.error('Error adjusting price:', error);
        alert('Error adjusting price for product ID: ' + productId);
      }
    );
  }

}
