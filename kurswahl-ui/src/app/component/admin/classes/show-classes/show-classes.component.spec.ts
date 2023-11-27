import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowClassesComponent} from './show-classes.component';

describe('ShowClassesComponent', () => {
    let component: ShowClassesComponent;
    let fixture: ComponentFixture<ShowClassesComponent>;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [ShowClassesComponent]
        });
        fixture = TestBed.createComponent(ShowClassesComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
