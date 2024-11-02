import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LowStockComponent } from './low-stock.component';

describe('LowStockComponent', () => {
  let component: LowStockComponent;
  let fixture: ComponentFixture<LowStockComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LowStockComponent]
    });
    fixture = TestBed.createComponent(LowStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
