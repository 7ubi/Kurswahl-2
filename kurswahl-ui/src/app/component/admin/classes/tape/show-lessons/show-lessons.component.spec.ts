import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowLessonsComponent} from './show-lessons.component';

describe('ShowLessonsComponent', () => {
  let component: ShowLessonsComponent;
  let fixture: ComponentFixture<ShowLessonsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShowLessonsComponent]
    });
    fixture = TestBed.createComponent(ShowLessonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
