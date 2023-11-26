import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowTapesComponent} from './show-tapes.component';

describe('ShowTapesComponent', () => {
  let component: ShowTapesComponent;
  let fixture: ComponentFixture<ShowTapesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShowTapesComponent]
    });
    fixture = TestBed.createComponent(ShowTapesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
