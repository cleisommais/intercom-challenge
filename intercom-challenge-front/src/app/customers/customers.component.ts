import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {Customer} from '../model/customer';
import {CustomerService} from '../customer-service/customer.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {

  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort: MatSort;
  @Output() progress = new EventEmitter();

  form: FormGroup;
  displayedColumns: string[] = ['userId', 'name', 'targetDistance'];
  data: Customer[] = [];
  dataSource: MatTableDataSource<Customer> = new MatTableDataSource<Customer>(this.data);
  isLoadingResults = true;

  constructor(private formBuilder: FormBuilder, private api: CustomerService, private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      file: [null, Validators.required]
    });
  }

  onFormSubmit(form: NgForm) {
    this.isLoadingResults = true;
    this.api.getCustomer(form).subscribe(
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

  onFileChange(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.form.get('file').setValue(file);
    }
  }

  openSnackBar(message: any, action: string) {
    this.snackBar.open(message, action, {
      duration: 5000,
      verticalPosition: 'top',
      horizontalPosition: 'end',
    });
  }
}
