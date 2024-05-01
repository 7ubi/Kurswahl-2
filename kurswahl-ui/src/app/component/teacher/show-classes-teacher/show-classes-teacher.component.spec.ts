import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowClassesTeacherComponent} from './show-classes-teacher.component';

describe('ShowClassesTeacherComponent', () => {
  let component: ShowClassesTeacherComponent;
  let fixture: ComponentFixture<ShowClassesTeacherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowClassesTeacherComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ShowClassesTeacherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
