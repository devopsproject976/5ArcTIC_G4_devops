import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8082/product';

  constructor(private http: HttpClient) {}

  addProduct(product: any, stockId: any): Observable<any> {
    //return this.http.post(`${this.apiUrl}/${stockId}`, product);
    return this.http.post(`${this.apiUrl}/3`, product);
  }

  // Fetch all stocks
  public fetchAllData(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  // Fetch stock by ID
  public fetchData(id: any): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  // Fetch quantity for a specific stock
  public fetchQuantity(id: any): Observable<any> {
    return this.http.get(`${this.apiUrl}/quantity/${id}`);
  }

  // Add new stock
  public addStock(stock: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, stock);
  }

  // Update existing stock
  public updateStock(id: any, stock: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, stock);
  }

  // Delete stock by ID
  public deleteStock(id: any): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  // Fetch low stock items (example function)
  public fetchLowStock(): Observable<any> {
    return this.http.get(`${this.apiUrl}/low-stock`);
  }

}
