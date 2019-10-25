import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort} from '@angular/material/sort';
import { merge, Observable, of as observableOf } from 'rxjs';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { DataService } from '../data.service';
import { MatTableDataSource } from '@angular/material/table';

@Component({
	selector: 'app-backend-request-dashboard',
	templateUrl: './backend-request-dashboard.component.html',
	styleUrls: ['./backend-request-dashboard.component.styl']
})
export class BackendRequestDashboardComponent implements AfterViewInit {

	displayedColumns: string[] = ['id', 'uri', 'method', 'status', 'responseTime'];
  	data: ApiRequest[] = [];

  	dataSource;
  	resultsLength = 0;
  	isLoadingResults = true;

  	res: ApiRequestInfo;

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
					return this.http.getApiRequests();
    			}),
    			map(data => {
					this.isLoadingResults = false;
					this.res = data;
					this.resultsLength = data.info['total'];
					return data.list;
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


export interface ApiRequest {
	id: number
	status: number
	responseTime: number
	method: string
	uri: string
}

export interface ApiRequestInfo {
	response_times: Object
	info: Object
	list: ApiRequest[]
}