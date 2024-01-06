import {ChangeDetectorRef, Component, ViewChild} from '@angular/core';
import {Dict, ${nameClass}Component} from "./${nameClass? uncap_first}.component";
import {HttpGlobalTool} from "@http/HttpGlobalTool";
import {AlertService} from "@alert/alert.service";



@Component({
  selector: 'app-${nameClass? uncap_first}-edit',
  templateUrl: './${nameClass? uncap_first}Edit.component.html',
  styleUrls: ['./${nameClass? uncap_first}Edit.component.css'],
})
export class ${nameClass}EditComponent {


  visibilityEditData = { 'visibility': 'hidden'}

  show: boolean = true;

  constructor(private parent: ${nameClass}Component,private httpGlobalTool: HttpGlobalTool,
              private _alertService: AlertService,private cd: ChangeDetectorRef) {
  }

  doSomething() {
    this.parent.closeEditSidenav();
    this.parent.queryData()
    this.dataElement = {... this.defDataElement}
  }

  doSave(){
    this.showProgressBar();
    this.httpGlobalTool.postBody("/api/${config.projectName}/${nameClass? uncap_first}/save", this.dataElement).subscribe({
      next: (res) => {
        this._alertService.success("保存成功")
        this.doSomething();
      },
      error: (e) => {
        this._alertService.error(e.error.error)
        this.hideProgressBar();
      },
      complete:()=>{
        this.hideProgressBar();
        this.clearData()
      }
    });
  }

  clearData(show?:boolean){
    if(show == null || !show){
      this.show = false;
    }else{
      this.show = true;
    }
    this.dataElement = this.defDataElement
  }

  findById(id:Number,show?:boolean){
    if(show == null || !show){
      this.show = false;
    }else{
      this.show = true;
    }
    this.httpGlobalTool.get("/api/${config.projectName}/${nameClass? uncap_first}/findById?id="+id).subscribe({
      next: (res) => {
        this.dataElement = res.data
      },
      error: (e) => {
        this._alertService.error(e.error.error)
        this.hideProgressBar();
      },
      complete:()=>{
        this.hideProgressBar();
      }
    });
  }

  showProgressBar(){
    this.visibilityEditData = { 'visibility': 'visible'}
  }
  hideProgressBar(){
    this.visibilityEditData = { 'visibility': 'hidden'}
  }

  defDataElement: DataElement = {
<#if column?? && (column?size > 0) >
  <#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
    && col.nameClass != "createDate" && col.nameClass != "updateDate"
    && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
    && col.nameClass != "id">
    ${col.nameClass? uncap_first}: '',
    </#if>
  </#list>
</#if>
  };

  dataElement: DataElement = JSON.parse(JSON.stringify(this.defDataElement));
}

export interface DataElement {

<#if column?? && (column?size > 0) >
  <#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
    && col.nameClass != "createDate" && col.nameClass != "updateDate"
    && col.nameClass != "isDelete" && col.nameClass != "isDeleted">
  ${col.nameClass? uncap_first}<#if col.nameClass=='id'>?</#if>: string;
    </#if>
  </#list>
</#if>
}

