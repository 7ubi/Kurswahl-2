import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowStudentClassesComponent} from './show-student-classes.component';

describe('ShowStudentClassesComponent', () => {
  let component: ShowStudentClassesComponent;
  let fixture: ComponentFixture<ShowStudentClassesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShowStudentClassesComponent]
    });
    fixture = TestBed.createComponent(ShowStudentClassesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
