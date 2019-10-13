import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {Customer} from '../model/customer';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrl: string = environment.urlEndPoint;

  constructor(private http: HttpClient) {
  }

  getCustomer(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.apiUrl + 'customers').pipe(tap(res => console.log(res), error => this.handleError(error)));
  }

  private handleError(error: any) {
    console.log('Error consumer service: %s', error);
    throw error;
  }
}
