import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowSubjectAreasComponent } from './show-subject-areas.component';

describe('ShowSubjectAreasComponent', () => {
  let component: ShowSubjectAreasComponent;
  let fixture: ComponentFixture<ShowSubjectAreasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShowSubjectAreasComponent]
    });
    fixture = TestBed.createComponent(ShowSubjectAreasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
