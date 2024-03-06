import {ComponentFixture, TestBed} from '@angular/core/testing';

import {TeacherCsvImportDialogComponent} from './teacher-csv-import-dialog.component';

describe('TeacherCsvImportDialogComponent', () => {
  let component: TeacherCsvImportDialogComponent;
  let fixture: ComponentFixture<TeacherCsvImportDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeacherCsvImportDialogComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TeacherCsvImportDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
