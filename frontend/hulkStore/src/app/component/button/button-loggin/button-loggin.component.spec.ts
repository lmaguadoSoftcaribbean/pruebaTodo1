import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ButtonLogginComponent } from './button-loggin.component';

describe('ButtonLogginComponent', () => {
  let component: ButtonLogginComponent;
  let fixture: ComponentFixture<ButtonLogginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ButtonLogginComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ButtonLogginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
