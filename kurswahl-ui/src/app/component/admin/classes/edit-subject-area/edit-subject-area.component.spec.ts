import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EditSubjectAreaComponent} from './edit-subject-area.component';

describe('EditSubjectAreaComponent', () => {
  let component: EditSubjectAreaComponent;
  let fixture: ComponentFixture<EditSubjectAreaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditSubjectAreaComponent]
    });
    fixture = TestBed.createComponent(EditSubjectAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
