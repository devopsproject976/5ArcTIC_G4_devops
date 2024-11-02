import { Component } from '@angular/core';
import { ProductService } from '../services/product.service';
import {StockService} from '../services/stock.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent {

  data: any;
  searchCategory: string = '';
  constructor(private productService : ProductService, private stockService: StockService) { }
  ngOnInit() {
    this.fetchData();
  }

  fetchData() {
    this.productService.fetchAllData().subscribe((response) => {
      this.data = response;
    });
  }

  get filteredData(): any[] {
    if (this.searchCategory.trim() === '') {
      return this.data;
    } else {
      return this.data.filter((data: { category: string; }) => data.category.toLowerCase().includes(this.searchCategory.toLowerCase()));
    }
  }


  adjustPrice(productId: number) {
    this.stockService.adjustProductPrice(productId).subscribe(
      response => {
        console.log(response); // Optionally log the response
        alert('Price adjustment successful for product ID: ' + productId);
        this.fetchData(); // Refresh the product list after adjustment
      },
      error => {
        console.error('Error adjusting price:', error);
        alert('Error adjusting price for product ID: ' + productId);
      }
    );
  }


}
