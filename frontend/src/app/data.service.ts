import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';

import { Observable } from 'rxjs';
import { Airport, Coordinates, Country } from './airport-form/models';
import { ApiRequest, ApiRequestInfo } from './backend-request-dashboard/backend-request-dashboard.component';
import { map } from 'rxjs/operators';

@Injectable({
	providedIn: 'root',
})

export class DataService {

	constructor(private http: HttpClient) { }

	api_server_url = 'http://localhost:5050';

	async getAirports(term?) {
		let headers = new HttpHeaders();
		headers.append('Content-Type', 'application/json');

		let url = `${this.api_server_url}/airports`;
		if (term) {
			url += `?search_term=${term}`;
		}
		return await this.http.get<Airport[]>(url, {headers: headers})
			.toPromise().then(
				(res) => { return res.map(item => { 
					return new Airport(
						item.code,
						item.name,
						item.city,
						new Country(item.country.iso2_code, item.country.name),
						new Coordinates(item.coordinates.latitude, item.coordinates.longitude)
					);
				});
			},
			(error) => {
				console.error(error);
				return [];
			}
		)
	}

	getApiRequests() {
		let headers = new HttpHeaders();
		headers.append('Content-Type', 'application/json');
		return this.http.get<ApiRequestInfo>(`${this.api_server_url}/api-requests`, {headers: headers});
	}

	calculateFare(form) : any {
		let headers = new HttpHeaders();
		headers.set('Content-Type', 'application/json');
		/*
		let params = new HttpParams();
		params.append("o", form.originAirport);
		params.append("d", form.destinationAirport);
		params.append("p", form.passengers);
		params.append("tt", form.tripType);
		params.append("c", form.currency);
		*/

		return this.http.get<any>(`${this.api_server_url}/calculate-fare?o=${form.originAirport.code}&d=${form.destinationAirport.code}&p=${form.passengers}&c=${form.currency}&tt=${form.tripType}`, { headers: headers });
	}
}