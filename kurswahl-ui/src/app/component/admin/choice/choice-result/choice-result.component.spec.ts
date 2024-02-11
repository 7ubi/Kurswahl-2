import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ChoiceResultComponent} from './choice-result.component';

describe('ChoiceResultComponent', () => {
  let component: ChoiceResultComponent;
  let fixture: ComponentFixture<ChoiceResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChoiceResultComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ChoiceResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
