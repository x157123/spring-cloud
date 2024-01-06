<mat-drawer-container autosize>
  <mat-drawer #drawer mode="over" position="end" class="edit-drawer">
    <app-${nameClass? uncap_first}-edit #app${nameClass}Edit></app-${nameClass? uncap_first}-edit>
  </mat-drawer>
  <mat-drawer-content>
    <mat-toolbar>
      <input matInput placeholder="查询条件">
      <button mat-raised-button color="primary" (click)="queryData()">查询</button>
      <button mat-icon-button color="primary" (click)="openEditSidenav(0)" aria-label="新增数据">
        <mat-icon>add</mat-icon>
      </button>
    </mat-toolbar>
    <mat-progress-bar [ngStyle]="visibilityListData" mode="indeterminate"></mat-progress-bar>
    <div class="table-container">
      <div class="tables-container">
        <div class="tables-container-content">
        <mat-table [dataSource]="dataSource">
          <#if column?? && (column?size > 0) >
            <#list column as col>
              <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
              && col.nameClass != "createDate" && col.nameClass != "updateDate"
              && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
              && col.nameClass != "version">
          <ng-container matColumnDef="${col.nameClass? uncap_first}" <#if col.nameClass=='id'>sticky</#if> >
            <mat-header-cell mat-header-cell *matHeaderCellDef> ${col.comment} </mat-header-cell>
            <mat-cell *matCellDef="let element" > {{element.${col.nameClass? uncap_first}}} </mat-cell>
          </ng-container>

              </#if>
            </#list>
          </#if>

          <ng-container matColumnDef="operate">
            <mat-header-cell mat-header-cell *matHeaderCellDef> 操作 </mat-header-cell>
            <mat-cell *matCellDef="let element">
              <a mat-icon-button color="primary" (click)="openEditSidenav(element.id, true)"  title="查看">
                <mat-icon>open_in_new</mat-icon>
              </a>
              <a mat-icon-button color="primary" (click)="openEditSidenav(element.id)"  title="修改">
                <mat-icon>edit</mat-icon>
              </a>
              <a mat-icon-button color="accent" (click)="delById(element.id)"  title="删除">
                <mat-icon>delete</mat-icon>
              </a>
            </mat-cell>
          </ng-container>
          <mat-header-row *matHeaderRowDef="displayedColumns; sticky:true"></mat-header-row>
          <mat-row *matRowDef="let row; columns: displayedColumns;"></mat-row>
        </mat-table>
        </div>
      </div>
      <mat-paginator [pageSizeOptions]="[10, 15, 20]" [length]="dataLength"
                     showFirstLastButtons
                     firstPageLabel="firstPageLabel"
                     itemsPerPageLabel="itemsPerPageLabel"
                     lastPageLabel="lastPageLabel"
                     nextPageLabel="nextPageLabel"
                     previousPageLabel="previousPageLabel"
                     [pageIndex]="pageIndex"
                     (page)="handlePageEvent($event)"
                     aria-label="Select page of periodic elements">
      </mat-paginator>
    </div>
  </mat-drawer-content>
</mat-drawer-container>
