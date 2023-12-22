import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CreateTapeComponent} from './create-tape.component';

describe('CreateTapeComponent', () => {
  let component: CreateTapeComponent;
  let fixture: ComponentFixture<CreateTapeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateTapeComponent]
    });
    fixture = TestBed.createComponent(CreateTapeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
