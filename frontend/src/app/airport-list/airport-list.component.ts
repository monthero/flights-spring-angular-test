import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort} from '@angular/material/sort';
import { merge, Observable, of as observableOf } from 'rxjs';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { DataService } from '../data.service';
import { Airport, Country, Coordinates } from '../airport-form/models';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-airport-list',
  templateUrl: './airport-list.component.html',
  styleUrls: ['./airport-list.component.styl']
})
export class AirportListComponent implements AfterViewInit {

  	displayedColumns: string[] = ['code', 'name', 'city', 'country', 'latitude', 'longitude'];
  	data: Airport[] = [];

  	resultsLength = 0;
  	isLoadingResults = true;
  	dataSource;

  	@ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  	@ViewChild(MatSort, {static: false}) sort: MatSort;

	constructor(private http : DataService) { }

	ngAfterViewInit() {
		// If the user changes the sort order, reset back to the first page.
    	this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    	merge(this.sort.sortChange, this.paginator.page).pipe(
    			startWith({}),
    			switchMap(() => {
					this.isLoadingResults = true;
					return this.http.getAirports();
    			}),
    			map(data => {
					this.isLoadingResults = false;
					this.resultsLength = data.length;
					return data;
    			}),
    			catchError(() => {
					this.isLoadingResults = false;
					return observableOf([]);
    			})
    		).subscribe(data => { 
    			this.data = data;
    			this.dataSource = new MatTableDataSource(data);
    			this.dataSource.paginator = this.paginator;
    			this.dataSource.sort = this.sort;
    		});
		
	}

}
