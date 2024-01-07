import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ChoiceSurveillanceComponent} from './choice-surveillance.component';

describe('ChoiceSurveillanceComponent', () => {
  let component: ChoiceSurveillanceComponent;
  let fixture: ComponentFixture<ChoiceSurveillanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChoiceSurveillanceComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ChoiceSurveillanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
