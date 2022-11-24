import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HomeComponent } from './component/page/home/home.component';
import { NavTopComponent } from './component/nav/nav-top/nav-top.component';
import { ButtonLogginComponent } from './component/button/button-loggin/button-loggin.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NavTopComponent,
    ButtonLogginComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
