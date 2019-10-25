export class Airport {
	
	code: string
	name: string
	city: string 
	country: Country
	coordinates: Coordinates

	constructor (code: string, name: string, city: string, country: Country, coords: Coordinates) { 
		this.code = code
		this.name = name
		this.city = city
		this.country = country
		this.coordinates = coords
	}
}

export class Country {

	iso2_code: string
	name: string

	constructor (code: string, name: string) { 
		this.iso2_code = code
		this.name = name
	}
}

export class Coordinates {

	latitude: number
	longitude: number

	constructor (lat: number, lon: number) {
		this.latitude = lat
		this.longitude = lon
	}
}