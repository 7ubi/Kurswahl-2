import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowAdminsComponent} from './show-admins.component';

describe('ShowAdminsComponent', () => {
  let component: ShowAdminsComponent;
  let fixture: ComponentFixture<ShowAdminsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShowAdminsComponent]
    });
    fixture = TestBed.createComponent(ShowAdminsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
