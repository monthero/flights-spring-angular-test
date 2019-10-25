import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, Validators, FormControl, FormGroup } from '@angular/forms';
import { Observable, from } from 'rxjs';
import { DataService } from '../data.service';
import { Airport } from './models';
import {
  debounceTime,
  map,
  startWith,
  distinctUntilChanged,
  filter,
  switchMap
} from "rxjs/operators";

@Component({
	selector: 'app-airport-form',
	templateUrl: './airport-form.component.html',
	styleUrls: ['./airport-form.component.styl']
})


export class AirportFormComponent implements OnInit {
	title = 'Airport form';

	constructor(private http : DataService) { }

	showResultsDiv: boolean = false;
	showLoading: boolean = false;
	showTrips: boolean = false;
	showReturnDiv: boolean = true;

	trip_price: number = 0.0;
	trip_price_per_passenger: number = 0.0;

	originAirportList: Airport[] = [];
	destinationAirportList: Airport[] = [];
	
	filteredOriginAirportOptions: Observable<Airport[]>;
	filteredDestinationAirportOptions: Observable<Airport[]>;

  	calculateForm = new FormGroup({
  		originAirport: new FormControl('', [Validators.required]),
  		destinationAirport: new FormControl('', [Validators.required]),
  		passengers: new FormControl(1, [Validators.required]),
  		tripType: new FormControl('rt'),
  		currency: new FormControl('EUR', Validators.required),
  	});

  	ngOnInit(){
		this.calculateForm.controls['originAirport'].valueChanges.pipe(
			// if character length greater then 0
			filter(res => res.length >= 1),
			// Time in milliseconds between key events
			debounceTime(1000),
			// If previous query is diffent from current   
			distinctUntilChanged(),
			map((value) => this.filterAirports("origin", value))
			).subscribe(res => { })

		this.calculateForm.controls['destinationAirport'].valueChanges.pipe(
			// if character length greater then 0
			filter(res => res.length >= 1),
			// Time in milliseconds between key events
			debounceTime(1000),
			// If previous query is diffent from current   
			distinctUntilChanged(),
			map((value) => this.filterAirports("destination", value))
			).subscribe(res => { })
	
    }

    displayFn(airport?: Airport): string | undefined {
    	return airport ? `[${airport.code}] ${airport.city}, ${airport.country.name}` : undefined;
    }

    changedOriginInput(value) {
    	this.filterAirports('origin', value);
    }

    filterAirports(flight_pos, value: string){
    	const filter_value = value.toLowerCase().trim();

    	if (filter_value === "" || (filter_value.includes("["))) {
    		return [];
    	}

    	let result = this.http.getAirports(value).then(data => {
    		if (flight_pos === 'origin') {
				this.originAirportList = <Airport[]>data;
			}
			else if (flight_pos === 'destination') {
				this.destinationAirportList = <Airport[]>data;
			}
    	});
    }


    integerArray(length: number): Array<number> {
    	if (length >= 0) {
    		return Array.from({length: length}, (v, k) => k+1);
    	}
    }

	swapAirports() {
		let origin = this.calculateForm.get('originAirport').value;
		let destination = this.calculateForm.get('destinationAirport').value;
		this.calculateForm.get('originAirport').setValue(destination);
		this.calculateForm.get('destinationAirport').setValue(origin);
	}

	onSubmit() {
		let form = this.calculateForm.value;
		console.log(form);
		this.showResultsDiv = true;
		this.showLoading = true;
		
		this.http.calculateFare(form).subscribe(
			(res) => { 
				if (res.hasOwnProperty('amount')) {
					this.trip_price = res.amount;
					this.trip_price_per_passenger = res.amount / form.passengers;
					this.showReturnDiv = form.tripType === 'rt';
					this.showLoading = false;
					this.showTrips = true;
				}
			},
			(error) => { 
				console.error(error);
			}
		);
	}
}