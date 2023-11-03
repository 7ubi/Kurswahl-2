import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CreateStudentClassComponent} from './create-student-class.component';

describe('CreateStudentClassComponent', () => {
  let component: CreateStudentClassComponent;
  let fixture: ComponentFixture<CreateStudentClassComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateStudentClassComponent]
    });
    fixture = TestBed.createComponent(CreateStudentClassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
