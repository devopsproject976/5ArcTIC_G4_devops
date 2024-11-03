import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})
export class CustomerComponent {
  data: any;
  customer: {
    name: any;
    email: any;
    phone: any;
  } = {
    name: null,
    email: null,
    phone: null,
  };

  displayForm: boolean = false; // Declare and initialize displayForm

  constructor(private customerservice: CustomerService, private dialog: MatDialog) {}

  ngOnInit() {
    this.fetchData();
  }

  fetchData() {
    this.customerservice.fetchAllData().subscribe((response) => {
      this.data = response;
      console.log(this.data); // Log the data to inspect its structure
    });
  }
  

  addCustomer() {
    this.customerservice.addCustomer(this.customer).subscribe(
      response => {
        this.data.push(response);
        this.customer = { name: null, email: null, phone: null }; // Reset form
        this.displayForm = false; // Hide form after submission
      },
      error => {
        console.error('Error adding customer', error);
      }
    );
  }

  showForm() {
    this.displayForm = true;
  }

  removeCustomer(customerId: number) {
    if (customerId) {
      this.customerservice.deleteCustomer(customerId).subscribe(() => {
        this.data = this.data.filter((item: any) => item.idCustomer !== customerId); // Use idCustomer for filtering
      }, error => {
        console.error('Error deleting customer', error);
      });
    } else {
      console.error('Invalid customer ID: ', customerId); // Log the invalid ID
    }
  }
  
  
}
