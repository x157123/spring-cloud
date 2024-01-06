import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {${nameClass}Component} from './${nameClass? uncap_first}.component';
import {RouterModule} from "@angular/router";
import {${nameClass}Routes} from "./${nameClass? uncap_first}.routing";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatTableModule} from "@angular/material/table";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from "@angular/material/icon";
import {MatSidenavModule} from "@angular/material/sidenav";
import {${nameClass}EditComponent} from "./${nameClass? uncap_first}Edit.component";
import {MatCardModule} from "@angular/material/card";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatGridListModule} from "@angular/material/grid-list";
import {FormsModule} from "@angular/forms";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSelectModule} from "@angular/material/select";
import {MatListModule} from "@angular/material/list";


@NgModule({
    declarations: [
        ${nameClass}Component,
        ${nameClass}EditComponent
    ],
  imports: [
    CommonModule,
    MatInputModule,
    MatButtonModule,
    MatPaginatorModule,
    MatTableModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatIconModule,
    MatSidenavModule,
    MatCardModule,
    MatProgressBarModule,
    MatGridListModule,
    FormsModule,
    MatTooltipModule,
    MatSelectModule,
    MatListModule,
    RouterModule.forChild(${nameClass}Routes),
  ]
})
export class ${nameClass}Module {

}
