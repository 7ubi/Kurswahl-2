import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSubjectAreaComponent } from './create-subject-area.component';

describe('CreateSubjectAreaComponent', () => {
  let component: CreateSubjectAreaComponent;
  let fixture: ComponentFixture<CreateSubjectAreaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateSubjectAreaComponent]
    });
    fixture = TestBed.createComponent(CreateSubjectAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
