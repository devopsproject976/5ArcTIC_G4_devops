import { Component } from '@angular/core';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})
export class CustomerComponent {
  data: any[] = [];
  customer = { name: '', email: '', phone: '' };
  displayForm = false;

  constructor(private customerService: CustomerService) {}

  ngOnInit() {
    this.fetchData();
  }

  fetchData() {
    this.customerService.fetchAllData().subscribe(
      (response) => {
        this.data = response;
      },
      (error) => {
        console.error('Error fetching data', error);
      }
    );
  }

  showForm() {
    this.displayForm = true;
  }

  addCustomer() {
    this.customerService.addCustomer(this.customer).subscribe(
      (response) => {
        this.data.push(response);
        this.resetForm();
      },
      (error) => {
        console.error('Error adding customer', error);
      }
    );
  }

  resetForm() {
    this.customer = { name: '', email: '', phone: '' };
    this.displayForm = false; // Hide form afterscsfsfedfef
  }

  removeCustomer(customerId: number) {
    this.customerService.deleteCustomer(customerId).subscribe(() => {
      this.data = this.data.filter(item => item.idCustomer !== customerId);
    }, error => {
      console.error('Error deleting customer', error);
    });
  }
}
