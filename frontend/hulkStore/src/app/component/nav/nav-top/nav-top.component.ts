import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-nav-top',
  templateUrl: './nav-top.component.html',
  styleUrls: ['./nav-top.component.css']
})
export class NavTopComponent implements OnInit {

  @Input() title: string = '';

  constructor() {}

  ngOnInit(): void {}

  onLogginButtonClick(event: any): void {
    console.log('onLogginButtonClick');
  }

}
