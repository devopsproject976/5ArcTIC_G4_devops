import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StockService {
  private apiUrl = 'http://localhost:8082/stock';

  constructor(private http: HttpClient) {}

  // Fetch all stock data
  public fetchAllData(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  // Fetch data for a specific stock item by ID
  public fetchData(id: any): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  // Fetch quantity for a specific stock item by ID
  public fetchQuantity(id: any): Observable<any> {
    return this.http.get(`${this.apiUrl}/quantity/${id}`);
  }

  // Add a new stock item
  public addStock(stock: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, stock);
  }

  // Update an existing stock item by ID
  public updateStock(id: any, stock: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, stock);
  }

  // Delete a stock item by ID
  public deleteStock(id: any): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  // Fetch low stock items (if applicable)
  public fetchLowStock(): Observable<any> {
    return this.http.get(`${this.apiUrl}/lowstock`);
  }

}
