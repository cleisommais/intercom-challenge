import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient, HttpEventType} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Customer} from '../model/customer';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrl: string = environment.urlEndPoint;
  private progress = new BehaviorSubject<number>(0);

  constructor(private http: HttpClient) {
  }

  getCustomer(data: any): Observable<Customer[]> {
    return this.http.post<any>(this.apiUrl + 'customers', data, {
      reportProgress: true,
      observe: 'events'
    }).pipe(map((event) => {
        switch (event.type) {
          case HttpEventType.UploadProgress:
            const progress = Math.round(100 * event.loaded / event.total);
            return {status: 'progress', message: progress};
          case HttpEventType.Response:
            return event.body;
          default:
            return this.handleError(event.type);
        }
      })
    );

    // return this.http.get<Customer[]>(this.apiUrl + 'customers').pipe(tap(res => console.log(res), error => this.handleError(error)));
  }

  private handleError(error: any) {
    console.log('Error customer service: %s', error);
    throw error;
  }
}
