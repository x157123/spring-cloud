import {Component, AfterViewInit, ViewChild, OnInit} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {HttpGlobalTool} from "@http/HttpGlobalTool";
import {PageEvent} from "@angular/material/paginator";
import {MatDrawer} from "@angular/material/sidenav";
import {${nameClass}EditComponent} from "./${nameClass? uncap_first}Edit.component";
import {AlertService} from "@alert/alert.service";


export interface Dict {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-${nameClass? uncap_first}',
  templateUrl: './${nameClass? uncap_first}.component.html',
  styleUrls: ['./${nameClass? uncap_first}.component.css'],
})
export class ${nameClass}Component implements AfterViewInit {

  pageEvent: PageEvent = new PageEvent();
  dataLength: number = 0;
  pageIndex: number = 0;
  pageSize: number = 10;
  displayedColumns: string[] = [<#list column as col><#if col.nameClass != "createUser" && col.nameClass != "updateUser" && col.nameClass != "createDate" && col.nameClass != "updateDate" && col.nameClass != "isDelete" && col.nameClass != "isDeleted" && col.nameClass != "version">'${col.nameClass? uncap_first}',</#if></#list>'operate'];
  dataSource = new MatTableDataSource<PeriodicElement>();

  visibilityListData = {'visibility': 'hidden'}

  @ViewChild('drawer', {static: false}) drawer!: MatDrawer;

  @ViewChild('app${nameClass}Edit', {static: false}) app${nameClass}Edit!: ${nameClass}EditComponent;

  constructor(private httpGlobalTool: HttpGlobalTool,
              private _alertService: AlertService) {
  }

  ngAfterViewInit(): void {
    this.queryData()
  }

  showProgressBar() {
    this.visibilityListData = {'visibility': 'visible'}
  }

  hideProgressBar() {
    this.visibilityListData = {'visibility': 'hidden'}
  }

  queryData() {
    let param = new URLSearchParams();
    param.set('page', String(this.pageIndex + 1));
    param.set('rows', String(this.pageSize));
    this.showProgressBar()
    this.httpGlobalTool.post("/api/${config.projectName}/${nameClass? uncap_first}/queryPage", param).subscribe({
      next: (res) => {
        this.dataSource = new MatTableDataSource<PeriodicElement>(res.data.records)
        this.dataLength = res.data.total
      },
      error: (e) => {
        this.hideProgressBar();
        console.log('error:', e.error)
      },
      complete: () => {
        this.hideProgressBar();
      }
    });
  }
  delById(id:number) {
    if(id != null && id>0){
      let param = new URLSearchParams();
      param.set('ids', String(id));
      this.httpGlobalTool.post("/api/${config.projectName}/${nameClass? uncap_first}/removeByIds",param).subscribe({
        next: (res) => {
          this._alertService.success("删除成功")
          this.queryData()
        },
        error: (e) => {
          this._alertService.error(e.error.error)
        },
        complete: () => {
        }
      });
    }
  }

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.pageSize = e.pageSize;
    this.pageIndex = e.pageIndex;
    this.queryData();
  }

  openEditSidenav(id:number,show?:boolean) {
    if (this.drawer) {
      this.app${nameClass}Edit.clearData()
      if(id != null && id>0){
        this.app${nameClass}Edit.findById(id,show);
      }
      this.drawer.open();
    }
  }

  closeEditSidenav() {
    if (this.drawer) {
      this.drawer.close();
    }
  }

}

export interface PeriodicElement {
  <#if column?? && (column?size > 0) >
    <#list column as col>
      <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
      && col.nameClass != "createDate" && col.nameClass != "updateDate"
      && col.nameClass != "isDelete" && col.nameClass != "isDeleted">
  ${col.nameClass? uncap_first}: string;
      </#if>
    </#list>
  </#if>
}