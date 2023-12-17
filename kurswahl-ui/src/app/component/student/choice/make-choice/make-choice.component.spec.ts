import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MakeChoiceComponent} from './make-choice.component';

describe('MakeChoiceComponent', () => {
  let component: MakeChoiceComponent;
  let fixture: ComponentFixture<MakeChoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MakeChoiceComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MakeChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
