import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-button-loggin',
  templateUrl: './button-loggin.component.html',
  styleUrls: ['./button-loggin.component.css']
})
export class ButtonLogginComponent implements OnInit {

  @Output() onLogginClick = new EventEmitter();

  constructor() { }

  ngOnInit(): void {}

  funcLogginClick(): void {
    console.log("test");
    this.onLogginClick.emit();
  }

}
