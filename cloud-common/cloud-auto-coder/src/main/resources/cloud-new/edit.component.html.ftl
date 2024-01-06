<div class="edit-content">
  <div class="edit-content-header">
    <ng-container *ngIf="show">
      查看
    </ng-container>
    <ng-container *ngIf="!show">
      新增
    </ng-container>
  </div>
  <hr>
  <ng-container>
    <div class="edit-content-info">
      <mat-grid-list cols="16" rowHeight="60px" gutterSize="5px">
        <#if column?? && (column?size > 0) >
          <#list column as col>
            <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
            && col.nameClass != "createDate" && col.nameClass != "updateDate"
            && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
            && col.nameClass != "id" && col.nameClass != "version">

        <mat-grid-tile colspan="4" class="title">${col.comment}:</mat-grid-tile>
        <mat-grid-tile colspan="12" class="content">
          <mat-form-field class="example-full-width">
            <mat-label>${col.comment}</mat-label>
            <input matInput [(ngModel)]="dataElement.${col.nameClass? uncap_first}" [readonly]="show">
          </mat-form-field>
        </mat-grid-tile>
            </#if>
          </#list>
        </#if>
      </mat-grid-list>
    </div>
    <hr>
    <div class="edit-content-bottom">
      <mat-card-actions align="start">
          <button mat-button (click)="doSomething()">关闭</button>
          <button mat-button (click)="doSave()" *ngIf="!show">保存</button>
      </mat-card-actions>
    </div>
    <mat-progress-bar [ngStyle]="visibilityEditData" mode="indeterminate"></mat-progress-bar>
  </ng-container>
</div>
