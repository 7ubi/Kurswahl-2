import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSubjectComponent } from './create-subject.component';

describe('CreateSubjectComponent', () => {
  let component: CreateSubjectComponent;
  let fixture: ComponentFixture<CreateSubjectComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateSubjectComponent]
    });
    fixture = TestBed.createComponent(CreateSubjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
