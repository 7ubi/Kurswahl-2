import {ComponentFixture, TestBed} from '@angular/core/testing';

import {TapeTableComponent} from './tape-table.component';

describe('TapeTableComponent', () => {
  let component: TapeTableComponent;
  let fixture: ComponentFixture<TapeTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TapeTableComponent]
    });
    fixture = TestBed.createComponent(TapeTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
