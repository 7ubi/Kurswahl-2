import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowSubjectsComponent } from './show-subjects.component';

describe('ShowSubjectsComponent', () => {
  let component: ShowSubjectsComponent;
  let fixture: ComponentFixture<ShowSubjectsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShowSubjectsComponent]
    });
    fixture = TestBed.createComponent(ShowSubjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
