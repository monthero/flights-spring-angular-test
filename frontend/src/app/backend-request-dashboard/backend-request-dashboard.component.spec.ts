import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BackendRequestDashboardComponent } from './backend-request-dashboard.component';

describe('BackendRequestDashboardComponent', () => {
  let component: BackendRequestDashboardComponent;
  let fixture: ComponentFixture<BackendRequestDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BackendRequestDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BackendRequestDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
