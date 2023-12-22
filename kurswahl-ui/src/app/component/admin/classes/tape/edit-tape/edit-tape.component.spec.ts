import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EditTapeComponent} from './edit-tape.component';

describe('EditTapeComponent', () => {
  let component: EditTapeComponent;
  let fixture: ComponentFixture<EditTapeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditTapeComponent]
    });
    fixture = TestBed.createComponent(EditTapeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
