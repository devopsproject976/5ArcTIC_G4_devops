import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrl = 'http://192.168.202.128:8082';


  constructor(private http: HttpClient) { }


  public fetchAllData(): Observable<any> {
    return this.http.get(`${this.apiUrl}/customer`);
  }

  public addCustomer(customer: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/customer`, customer);
  }


  public deleteCustomer(customerId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/customer/${customerId}`); 
  }
  

  
}
