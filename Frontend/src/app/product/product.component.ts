import {Component, OnInit} from '@angular/core';
import { ProductService } from '../services/product.service';
import { ActivatedRoute } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { StockService } from '../services/stock.service';


@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {
  data: any;
  displayForm: boolean = false;
  closeResult!: string;
  form: boolean = false;
  stock: any;

  // Define the product object with the necessary properties
  product: {
    title: string | null;
    category: string | null;
    price: number | null;
    quantity: number | null;
  } = {
    title: null,
    category: null,
    price: null,
    quantity: null
  };

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private modalService: NgbModal,
    private stockService: StockService
  ) {}

  ngOnInit() {
    this.fetchData();
    this.getStock();
  }

  // Fetch product data by ID
  fetchData() {
    const id = this.route.snapshot.paramMap.get('id');
    this.productService.fetchData(id).subscribe((response) => {
      this.data = response;
    });
  }

  fetchAllProducts() {
    this.productService.fetchAllData().subscribe(
      (response) => {
        this.data = response; // Assuming response is an array of products
      },
      (error) => {
        console.error('Error fetching products:', error);
      }
    );
  }

  // Show the form to add or edit a product
  showForm(product?: any) {
    this.displayForm = true; // Show the form
    if (product) {
      // If a product is passed, populate the form for editing
      this.product = {
        title: product.title,
        category: product.category,
        price: product.price,
        quantity: product.quantity
      };
    } else {
      // Reset the product object if adding new
      this.product = { title: null, category: null, price: null, quantity: null };
    }
  }

  // Add a new product
  // Add a new product
  addProduct(product: { title: string | null; category: string | null; price: number | null; quantity: number | null }) {
    const id = this.route.snapshot.paramMap.get('id'); // Assuming this is the stock ID
    this.productService.addProduct(product, id).subscribe(
      (response) => {
        console.log('Product added:', response);
        this.fetchData(); // Refresh product data after adding
        this.closeForm(); // Close the form after submission
      },
      (error) => {
        console.error('Error adding product:', error);
      }
    );
  }



  // Open modal for product management
  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  // Handle modal dismissal
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  // Close the form
  closeForm() {
    this.form = false;
    this.product = { title: null, category: null, price: null, quantity: null }; // Reset the product
  }

  // Cancel the form
  cancel() {
    this.form = false;
  }

  // Fetch stock data by ID
  getStock() {
    const id = this.route.snapshot.paramMap.get('id');
    this.stockService.fetchData(id).subscribe((response) => {
      this.stock = response;
    });
  }

  // Delete a product by ID
  deleteItem(itemId: any) {
    if (confirm('Are you sure you want to delete this item?')) {
      this.productService.deleteStock(itemId).subscribe(() => {
        console.log('Item deleted');
        this.fetchData(); // Refresh the product data after deletion
      });
    }
  }
}
