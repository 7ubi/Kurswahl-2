import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EditStudentClassComponent} from './edit-student-class.component';

describe('EditStudentClassComponent', () => {
  let component: EditStudentClassComponent;
  let fixture: ComponentFixture<EditStudentClassComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditStudentClassComponent]
    });
    fixture = TestBed.createComponent(EditStudentClassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
