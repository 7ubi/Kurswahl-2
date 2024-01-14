import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AssignChoiceComponent} from './assign-choice.component';

describe('AssignChoiceComponent', () => {
  let component: AssignChoiceComponent;
  let fixture: ComponentFixture<AssignChoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignChoiceComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AssignChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
