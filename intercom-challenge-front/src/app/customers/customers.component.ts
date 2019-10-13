import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {Customer} from '../model/customer';
import {CustomerService} from '../customer-service/customer.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {

  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort: MatSort;
  displayedColumns: string[] = ['userId', 'name', 'targetDistance'];
  data: Customer[] = [];
  dataSource: MatTableDataSource<Customer> = new MatTableDataSource<Customer>(this.data);
  isLoadingResults = true;

  constructor(private api: CustomerService, private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.api.getCustomer().subscribe(
      res => {
        this.data = res.slice();
        this.dataSource = new MatTableDataSource<Customer>(this.data);
        this.isLoadingResults = false;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      err => {
        console.log(err);
        this.isLoadingResults = false;
        this.openSnackBar(err.message, 'Error');
      }
    );
  }

  openSnackBar(message: any, action: string) {
    this.snackBar.open(message, action, {
      duration: 5000,
      verticalPosition: 'top',
      horizontalPosition: 'end',
    });
  }
}
