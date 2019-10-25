import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AirportFormComponent } from './airport-form/airport-form.component';
import { AirportListComponent } from './airport-list/airport-list.component';
import { BackendRequestDashboardComponent } from './backend-request-dashboard/backend-request-dashboard.component';


const routes: Routes = [
	{ path: '', component: AirportFormComponent},
	{ path: 'airports', component: AirportListComponent},
	{ path: 'dashboard', component: BackendRequestDashboardComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
