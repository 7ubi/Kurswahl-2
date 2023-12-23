import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowChoicesComponent} from './show-choices.component';

describe('ShowChoicesComponent', () => {
  let component: ShowChoicesComponent;
  let fixture: ComponentFixture<ShowChoicesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowChoicesComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ShowChoicesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
