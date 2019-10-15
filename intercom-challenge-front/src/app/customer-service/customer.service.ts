import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient, HttpEventType, HttpHeaders} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {Customer} from '../model/customer';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrl: string = environment.urlEndPoint;
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'multipart/form-data; charset=utf-8' }),
  };

  constructor(private http: HttpClient) {
  }

  getCustomer(data: any): Observable<Customer[]> {
    return this.http.post<Customer[]>(this.apiUrl + 'customers', data).pipe(
      tap(res => console.log(res), error => this.handleError(error)));
  }

  private handleError(error: any) {
    console.log('Error customer service: %s', error);
    throw error;
  }
}
