import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowTeachersComponent} from './show-teachers.component';

describe('ShowTeachersComponent', () => {
  let component: ShowTeachersComponent;
  let fixture: ComponentFixture<ShowTeachersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShowTeachersComponent]
    });
    fixture = TestBed.createComponent(ShowTeachersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
