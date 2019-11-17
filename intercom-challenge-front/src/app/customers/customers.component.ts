import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {Customer} from '../model/customer';
import {CustomerService} from '../customer-service/customer.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {FormBuilder, FormGroup, NgForm, Validators} from '@angular/forms';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {

  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: false}) sort: MatSort;

  uploadForm: FormGroup;
  displayedColumns: string[] = ['userId', 'name', 'targetDistance'];
  data: Customer[] = [];
  dataSource: MatTableDataSource<Customer> = new MatTableDataSource<Customer>(this.data);
  isLoadingResults = true;

  constructor(private formBuilder: FormBuilder, private api: CustomerService, private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.isLoadingResults = false;
    this.uploadForm = this.formBuilder.group({
      fileToSend: [null, Validators.required]
    });
  }

  cleanDataGrid() {
    this.dataSource = new MatTableDataSource<Customer>(null);
  }

  onFormSubmit(form: NgForm) {
    this.isLoadingResults = true;
    const formData = new FormData();
    formData.append('file', this.uploadForm.get('fileToSend').value);
    this.api.getCustomer(formData).subscribe(
      res => {
        this.data = res.slice();
        this.dataSource = new MatTableDataSource<Customer>(this.data);
        this.isLoadingResults = false;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      err => {
        this.cleanDataGrid();
        this.isLoadingResults = false;
        this.openSnackBar(err.error, 'Error');
      }
    );
  }

  onFileChange(files: FileList) {
    this.uploadForm.get('fileToSend').setValue(files.item(0));
  }

  openSnackBar(message: any, action: string) {
    this.snackBar.open(message, action, {
      duration: 5000,
      verticalPosition: 'top',
      horizontalPosition: 'end',
    });
  }
}
