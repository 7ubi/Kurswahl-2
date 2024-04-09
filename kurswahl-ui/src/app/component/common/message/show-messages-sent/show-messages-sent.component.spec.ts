import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowMessagesSentComponent} from './show-messages-sent.component';

describe('ShowMessagesSentComponent', () => {
  let component: ShowMessagesSentComponent;
  let fixture: ComponentFixture<ShowMessagesSentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowMessagesSentComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ShowMessagesSentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
