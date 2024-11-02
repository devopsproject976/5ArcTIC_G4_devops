import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductComponent } from './product/product.component';
import { StockComponent } from './stock/stock.component';
import { ProductListComponent } from './product-list/product-list.component';
import { ContactComponent } from './contact/contact.component';
import { LowStockComponent } from './low-stock/low-stock.component';


const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  //{ path: 'home',  component: StockComponent },
  { path: 'products', component: ProductListComponent },
  //{ path: 'product/:id', component: ProductComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'product', component: ProductComponent },
  { path: 'stock',  component: StockComponent },
  { path: 'low-stock', component: LowStockComponent },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
