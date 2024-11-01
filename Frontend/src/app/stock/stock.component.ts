import {Component, OnInit} from '@angular/core';
import {StockService} from '../services/stock.service';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-stock',
  templateUrl: './stock.component.html',
  styleUrls: ['./stock.component.css']
})
export class StockComponent implements OnInit {
  data: any;
  displayForm = false;
  stock: any = {
    idStock: null,
    title: '',
    threshold: null
  };
  constructor(private stockService: StockService, private dialog: MatDialog) {}

  ngOnInit() {
    this.fetchData();
  }

  // Fetch all stock data
  fetchData() {
    this.stockService.fetchAllData().subscribe((response) => {
      this.data = response;
    });
  }

  // Show the form to add or edit stock
  showForm(stock?: any) {
    if (stock) {
      // Populate the form for editing
      this.stock = { ...stock }; // Clone the stock for editing
    } else {
      // Reset the form for adding new stock
      this.stock = { idStock: null, title: '' };
    }
    this.displayForm = true;
  }

  // Submit the form for adding or updating stock
  submitForm() {
    if (this.stock.idStock) {
      // Update existing stock
      this.stockService.updateStock(this.stock.idStock, this.stock).subscribe(() => {
        this.fetchData();
        this.closeForm();
      });
    } else {
      // Add new stock
      this.stockService.addStock(this.stock).subscribe(() => {
        this.fetchData();
        this.closeForm();
      });
    }
  }

  // Delete a stock item
  deleteStock(idStock: any) {
    this.stockService.deleteStock(idStock).subscribe(() => {
      this.fetchData();
    });
  }

  // Close the form
  closeForm() {
    this.displayForm = false;
    this.stock = { idStock: null, title: '' }; // Reset the stock object
  }
}
