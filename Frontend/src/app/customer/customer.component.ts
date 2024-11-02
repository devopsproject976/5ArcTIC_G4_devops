import { Component } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
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

  constructor(private customerservice: CustomerService, private dialog: MatDialog) {
  }

  ngOnInit() {
    this.fetchData();
  }

  fetchData() {
    this.customerservice.fetchAllData().subscribe((response) => {
      this.data = response;
    });
  }

  // showForm() {
  //   this.displayForm = true;
  // }

  // submitForm() {
  //  // Hide the form after submission
  //   this.displayForm = false;
  // }

  // addStock(customer: any) {
  //   return this.customerservice.addCustomer(customer).subscribe((response) => {
  //     this.data = response;
  //     this.displayForm = false;
  //     this.fetchData();
  //   });
  // }

}
