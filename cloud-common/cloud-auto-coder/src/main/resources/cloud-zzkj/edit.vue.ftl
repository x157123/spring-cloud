<template>
  <div>
    <!-- ${comment}编辑弹窗 -->
    <a-modal :title="title" :width="1200" :visible="visible" @cancel="handleEditCancel" destroyOnClose class="modal-box">
      <dynamic-form :formFields="formFields" :formRules="formRules" ref="formRef" layout="inline" :btnText="'上传附件'"
        :moduleType="'mediationDisputeType'" :echoData="detailInfoData" :handleType="handleType" :detailId="detailId">
      </dynamic-form>
<#if foreignKeys?? && (foreignKeys?size > 0) >
<#list foreignKeys as foreignKey>
<#if foreignKey.joinTableNameClass != nameClass>
      <a-row type="flex" justify="space-between" class="edit-tit">
        <a-col flex="auto">
          <h3>
            ${foreignKey.comment}
          </h3>
        </a-col>
        <a-col flex="30px">
          <a-icon type="plus-circle" theme="filled" style="color: #4d7bdf;font-size: 22px;" @click="handleAdd${foreignKey.joinTableNameClass? cap_first}" />
        </a-col>
      </a-row>
      <a-table ref="table${foreignKey.joinTableNameClass? cap_first}Ref" :columns="${foreignKey.joinTableNameClass? uncap_first}Columns" :data-source="${foreignKey.joinTableNameClass? uncap_first}List" :scroll="{ x: 'calc(600px + 50%)', y: 600 }" bordered :pagination="false">
        <a slot="name" slot-scope="text">{{ text }}</a>
        <a slot="actions" slot-scope="text,record,index">
          <a-popconfirm title="是否删除?" @confirm="() => delete${foreignKey.joinTableNameClass? cap_first}(index)">
            <a class="ml10">删除</a>
          </a-popconfirm>
          <a class="ml10" @click="handleEdit${foreignKey.joinTableNameClass? cap_first}(record)">编辑</a>
        </a>
      </a-table>
</#if>
</#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
<#list mergeTables as mergeTable>
<#if mergeTable.leftTable == mergeTable.maintain>
      <a-row type="flex" justify="space-between" class="edit-tit">
        <a-col flex="auto">
          <h3>
            ${mergeTable.mysqlTable.comment}
          </h3>
        </a-col>
        <a-col flex="30px">
          <a-icon type="plus-circle" theme="filled" style="color: #4d7bdf;font-size: 22px;" @click="handleAdd${mergeTable.mysqlTable.nameClass? cap_first}" />
        </a-col>
      </a-row>
      <a-table ref="table${mergeTable.mysqlTable.nameClass? cap_first}Ref" :columns="${mergeTable.mysqlTable.nameClass? uncap_first}Columns" :data-source="${mergeTable.mysqlTable.nameClass? uncap_first}List" :scroll="{ x: 'calc(600px + 50%)', y: 600 }" bordered :pagination="false">
        <a slot="name" slot-scope="text">{{ text }}</a>
        <a slot="actions" slot-scope="text,record,index">
          <a-popconfirm title="是否删除?" @confirm="() => delete${mergeTable.mysqlTable.nameClass? cap_first}(index)">
            <a class="ml10">删除</a>
          </a-popconfirm>
          <a class="ml10" @click="handleEdit${mergeTable.mysqlTable.nameClass? cap_first}(record)">编辑</a>
        </a>
      </a-table>
</#if>
</#list>
</#if>
      <slot/>
      <template slot="footer">
        <a-form-model-item label="附件信息" :label-col="{ span: 3 }" :wrapper-col="{ span: 16 }" class="upload-box">
          <a-upload name="file" :multiple="true" :action="uploadUrl" :headers="headers" @change="changeFile"
            :fileList="fileList" :remove="removeFile">
            <a-button type="primary">附件上传</a-button>
          </a-upload>
        </a-form-model-item>
        <div style="display: flex;">
          <a-button key="back" @click="handleEditCancel">
            取消
          </a-button>
          <a-button key="submit" type="primary" :loading="btnLoading" @click="handleEditOk">
            确定
          </a-button>
        </div>
      </template>
    </a-modal>
    <#if foreignKeys?? && (foreignKeys?size > 0) >
      <#list foreignKeys as foreignKey>
        <#if foreignKey.joinTableNameClass != nameClass>
    <!-- ${foreignKey.comment} -->
    <add${foreignKey.joinTableNameClass? cap_first} ref="add${foreignKey.joinTableNameClass? cap_first}Ref" v-if="add${foreignKey.joinTableNameClass? cap_first}Visible" @confirm="handleAdd${foreignKey.joinTableNameClass? cap_first}Confirm" @handleCancel="handleAdd${foreignKey.joinTableNameClass? cap_first}Cancel" />
        </#if>
      </#list>
    </#if>
    <#if mergeTables?? && (mergeTables?size > 0) >
      <#list mergeTables as mergeTable>
        <#if mergeTable.leftTable == mergeTable.maintain>
    <!-- ${mergeTable.mysqlTable.comment} -->
    <add${mergeTable.mysqlTable.nameClass? cap_first} ref="add${mergeTable.mysqlTable.nameClass? cap_first}Ref" v-if="add${mergeTable.mysqlTable.nameClass? cap_first}Visible" @confirm="handleAdd${mergeTable.mysqlTable.nameClass? cap_first}Confirm" @handleCancel="handleAdd${mergeTable.mysqlTable.nameClass? cap_first}Cancel" />
        </#if>
      </#list>
    </#if>
  </div>
</template>

<script>
import dynamicForm from '@/${web}/form/dynamicForm';
import { ACCESS_TOKEN } from '@/store/mutation-types';
import Vue from 'vue';
import { baseURL } from '@/config/net.config';
<#if foreignKeys?? && (foreignKeys?size > 0) >
<#list foreignKeys as foreignKey>
<#if foreignKey.joinTableNameClass != nameClass>
// ${foreignKey.comment}
import add${foreignKey.joinTableNameClass? cap_first} from '@/${web}${foreignKey.mysqlTable.webExpandPackage}/${foreignKey.joinTableNameClass? uncap_first}/${foreignKey.joinTableNameClass? uncap_first}Attributes';
</#if>
</#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
<#list mergeTables as mergeTable>
<#if mergeTable.leftTable == mergeTable.maintain>
// ${mergeTable.mysqlTable.comment}
import add${mergeTable.mysqlTable.nameClass? cap_first} from '@/${web}${mergeTable.mysqlTable.webExpandPackage}/${mergeTable.mysqlTable.nameClass? cap_first? uncap_first}/${mergeTable.mysqlTable.nameClass? cap_first? uncap_first}Attributes';
</#if>
</#list>
</#if>

// 接口
import {
  ${nameClass? uncap_first}InfoFull, ${nameClass? uncap_first}InfoAdd, ${nameClass? uncap_first}InfoUpdate,
} from '@/${web}/api${webExpandPackage}/${nameClass? uncap_first}/${nameClass? uncap_first}Api';
import moment from 'moment';
let that = null;
export default {
  name: 'contradictionEditPopup',
  components: {
    dynamicForm,
    <#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
    <#if foreignKey.joinTableNameClass != nameClass>
    add${foreignKey.joinTableNameClass? cap_first},
    </#if>
    </#list>
    </#if>
    <#if mergeTables?? && (mergeTables?size > 0) >
    <#list mergeTables as mergeTable>
    <#if mergeTable.leftTable == mergeTable.maintain>
    add${mergeTable.mysqlTable.nameClass? cap_first},
    </#if>
    </#list>
    </#if>
  },
  props: {
    // 编辑弹窗是否显示
    visible: {
      type: Boolean,
      default: false,
    },
    // 编辑弹窗标题
    title: {
      type: String,
      default: '',
    },
    // 编辑弹窗表单字段
    formFields: {
      type: Array,
      default: () => [],
    },
    // 编辑弹窗表单校验规则
    formRules: {
      type: Object,
      default: () => { },
    },
    // 操作类型 1:新增 2:编辑
    handleType: {
      type: Number,
      default: 1,
    },
    // 编辑id
    detailId: {
      type: String,
      default: '',
    },
  },
  data() {
    that = this;
    const token = Vue.ls.get(ACCESS_TOKEN);
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      demands: [
        {
          demand: '',
          id: Date.now(),
        },
      ],
      detailInfoData: {}, // 查看详情数据
      demandDeletedIds: [], // 删除主要诉求id
      headers: {
        'ZC-Auth': 'Bearer ' + token,
      },
      uploadUrl: `<#noparse>${baseURL}</#noparse>/system/oss/upload`,
      fileList: [],
      btnLoading: false, // 确认按钮loading
      <#if foreignKeys?? && (foreignKeys?size > 0) >
      <#list foreignKeys as foreignKey>
      <#if foreignKey.joinTableNameClass != nameClass>
      add${foreignKey.joinTableNameClass? cap_first},
      // ${foreignKey.comment}列表
      ${foreignKey.joinTableNameClass? uncap_first}List: [],
      // 添加${foreignKey.comment}弹框
      add${foreignKey.joinTableNameClass? cap_first}Visible: false,
      // ${foreignKey.comment}表头
      ${foreignKey.joinTableNameClass? uncap_first}Columns: [
        <#list foreignKey.mysqlTable.column as col>
        <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
    && col.nameClass != "createDate" && col.nameClass != "updateDate"
    && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
    && col.nameClass != "createBy" && col.nameClass != "updateBy"
    && col.nameClass != "version" && col.nameClass != "id"
    && col.nameClass != "createTime" && col.nameClass != "updateTime">
        {
          key: '${col.nameClass}',
          title: '${col.webComment}',
          dataIndex: '${col.nameClass}',
          width: 120,
        },
        </#if>
        </#list>
        {
          key: 'actions',
          title: '操作',
          fixed: 'right',
          dataIndex: 'actions',
          width: 160,
          scopedSlots: { customRender: 'actions' },
        },
      ],
      </#if>
      </#list>
      </#if>
      <#if mergeTables?? && (mergeTables?size > 0) >
      <#list mergeTables as mergeTable>
      <#if mergeTable.leftTable == mergeTable.maintain>
      // ${mergeTable.mysqlTable.comment}列表
      ${mergeTable.mysqlTable.nameClass? uncap_first}List: [],
      // 添加${mergeTable.mysqlTable.comment}弹框
      add${mergeTable.mysqlTable.nameClass? cap_first}Visible: false,
      // ${mergeTable.mysqlTable.comment}表头
      ${mergeTable.mysqlTable.nameClass? uncap_first}Columns: [
        <#list mergeTable.mysqlTable.column as col>
        <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
    && col.nameClass != "createDate" && col.nameClass != "updateDate"
    && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
    && col.nameClass != "createBy" && col.nameClass != "updateBy"
    && col.nameClass != "version" && col.nameClass != "id"
    && col.nameClass != "createTime" && col.nameClass != "updateTime">
        {
          key: '${col.nameClass}',
          title: '${col.webComment}',
          dataIndex: '${col.nameClass}',
          width: 120,
        },
              </#if>
        </#list>
        {
          key: 'actions',
          title: '操作',
          fixed: 'right',
          dataIndex: 'actions',
          width: 160,
          scopedSlots: { customRender: 'actions' },
        },
      ],
      </#if>
      </#list>
      </#if>
    };
  },
  methods: {
    // 附件信息
    changeFile(info) {
      this.fileList = info.fileList;
      if (info.file.status === 'done') {
        const result = info.file.response;
        if (result.success) {
          this.fileList.map(item => {
            if (item.response) {
              const res = item.response?.data;
              item.type = res.type;
              item.name = res.originalName;
              item.originalName = res.originalName;
              item.filePath = res.relativePath;
              item.url = res.fullUrl;
            }
          });
        } else {
          this.fileList.pop();
          this.$message.error(`图片上传失败`);
        }
      } else if (info.file.status === 'error') {
        this.$message.error(`图片上传失败`);
      }
    },
    removeFile(file) {
      let i = this.fileList.findIndex(it => it.uid === file.uid);
      // 删除附件id数组
      if (i === -1) return;
      this.fileList.splice(i, 1);
    },
    // 新增/编辑弹窗确认
    handleEditOk() {
      this.$refs.formRef
        .validateForm()
        .then((formData) => {
          this.btnLoading = true;
          let params = {
            ...formData,
            <#if foreignKeys?? && (foreignKeys?size > 0) >
            <#list foreignKeys as foreignKey>
            <#if foreignKey.joinTableNameClass != nameClass>
            ${foreignKey.joinTableNameClass? uncap_first}Params: this.${foreignKey.joinTableNameClass? uncap_first}List,
            </#if>
            </#list>
            </#if>
            <#if mergeTables?? && (mergeTables?size > 0) >
            <#list mergeTables as mergeTable>
            <#if mergeTable.leftTable == mergeTable.maintain>
            ${mergeTable.mysqlTable.nameClass? uncap_first}Params: this.${mergeTable.mysqlTable.nameClass? uncap_first}List,
            </#if>
            </#list>
            </#if>
            files: this.fileList,
          };
          params.id = this.detailId;
          // 新增修改
          ${nameClass? uncap_first}InfoUpdate(params).then((res) => {
            if (res.success) {
              this.$message.success(res.msg);
              this.handleEditCancel();
              this.$emit('editOk');
            } else {
              this.$message.error(res.msg);
            }
          }).finally(() => {
            this.btnLoading = false;
          });
        })
        .catch((error) => {
          this.btnLoading = false;
          // 表单校验不通过，做出相应的处理
          console.error(error);
        });
    },
    // 编辑弹窗取消
    handleEditCancel(e) {
      this.$emit('editCancel', e);
      this.demands = [
        {
          demand: '',
          id: Date.now(),
        },
      ];
      <#if foreignKeys?? && (foreignKeys?size > 0) >
      <#list foreignKeys as foreignKey>
      <#if foreignKey.joinTableNameClass != nameClass>
      // ${foreignKey.comment}列表
      this.${foreignKey.joinTableNameClass? uncap_first}List = [];
      </#if>
      </#list>
      </#if>
      <#if mergeTables?? && (mergeTables?size > 0) >
      <#list mergeTables as mergeTable>
      <#if mergeTable.leftTable == mergeTable.maintain>
      // ${mergeTable.mysqlTable.comment}列表
      this.${mergeTable.mysqlTable.nameClass? uncap_first}List = [];
      </#if>
      </#list>
      </#if>
      this.fileList = [];
      this.$refs.formRef.resetForm();
    },
    validateForm() {
      return that.$refs.formRef
        .validateForm();
    },
    <#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
    <#if foreignKey.joinTableNameClass != nameClass>
    // -------------------------------------------------------------添加${foreignKey.comment}开始------------------------------------------
    // 打开添加当事人列表弹框
    handleAdd${foreignKey.joinTableNameClass? cap_first}() {
      this.add${foreignKey.joinTableNameClass? cap_first}Visible = true;
      this.$nextTick().then(() => {
        this.$refs.add${foreignKey.joinTableNameClass? cap_first}Ref.initData();
      });
    },
    // 编辑
    handleEdit${foreignKey.joinTableNameClass? cap_first}(record) {
      this.add${foreignKey.joinTableNameClass? cap_first}Visible = true;
      this.$nextTick().then(() => {
        this.$refs.add${foreignKey.joinTableNameClass? cap_first}Ref.initData(record);
      });
    },
    // 回调添加数据
    handleAdd${foreignKey.joinTableNameClass? cap_first}Confirm(data) {
      let bool = 0;
      this.${foreignKey.joinTableNameClass? uncap_first}List.forEach((item, index) => {
        if (item.key === data.key) {
          item.name = data.name;
          item.idCardNo = data.idCardNo;
          item.phone = data.phone;
          item.address = data.address;
          item.permanentAddress = data.permanentAddress;
          bool = 1;
        }
      });
      if (bool === 0) {
        this.${foreignKey.joinTableNameClass? uncap_first}List.push(data);
      }
    },
    // 取消添加数据
    handleAdd${foreignKey.joinTableNameClass? cap_first}Cancel() {
      this.add${foreignKey.joinTableNameClass? cap_first}Visible = false;
    },
    delete${foreignKey.joinTableNameClass? cap_first}(index) {
      this.${foreignKey.joinTableNameClass? uncap_first}List.splice(index, 1);
    },
    // -------------------------------------------------------------添加${foreignKey.comment}结束------------------------------------------
    </#if>
    </#list>
    </#if>
    <#if mergeTables?? && (mergeTables?size > 0) >
    <#list mergeTables as mergeTable>
    <#if mergeTable.leftTable == mergeTable.maintain>
    // -------------------------------------------------------------添加${mergeTable.mysqlTable.comment}开始------------------------------------------
    // 打开添加当事人列表弹框
    handleAdd${mergeTable.mysqlTable.nameClass? cap_first}() {
      this.add${mergeTable.mysqlTable.nameClass? cap_first}Visible = true;
      this.$nextTick().then(() => {
        this.$refs.add${mergeTable.mysqlTable.nameClass? cap_first}Ref.initData();
      });
    },
    // 编辑
    handleEdit${mergeTable.mysqlTable.nameClass? cap_first}(record) {
      this.add${mergeTable.mysqlTable.nameClass? cap_first}Visible = true;
      this.$nextTick().then(() => {
        this.$refs.add${mergeTable.mysqlTable.nameClass? cap_first}Ref.initData(record);
      });
    },
    // 回调添加数据
    handleAdd${mergeTable.mysqlTable.nameClass? cap_first}Confirm(data) {
      let bool = 0;
      this.${mergeTable.mysqlTable.nameClass? uncap_first}List.forEach((item, index) => {
        if (item.key === data.key) {
          item.name = data.name;
          item.idCardNo = data.idCardNo;
          item.phone = data.phone;
          item.address = data.address;
          item.permanentAddress = data.permanentAddress;
          bool = 1;
        }
      });
      if (bool === 0) {
        this.${mergeTable.mysqlTable.nameClass? uncap_first}List.push(data);
      }
    },
    // 取消添加数据
    handleAdd${mergeTable.mysqlTable.nameClass? cap_first}Cancel() {
      this.add${mergeTable.mysqlTable.nameClass? cap_first}Visible = false;
    },
    delete${mergeTable.mysqlTable.nameClass? cap_first}(index) {
      this.${mergeTable.mysqlTable.nameClass? uncap_first}List.splice(index, 1);
    },
    // -------------------------------------------------------------添加${mergeTable.mysqlTable.comment}结束------------------------------------------
    </#if>
    </#list>
    </#if>
    // 获取展示信息
    getDetailInfo() {
      ${nameClass? uncap_first}InfoFull(this.detailId).then((res) => {
        if (res.success) {
          // this.detailInfoData = res.data;
          this.detailInfoData = {
          <#if column?? && (column?size > 0) >
          <#list column as col>
          <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
          && col.nameClass != "createDate" && col.nameClass != "updateDate"
          && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
          && col.nameClass != "createBy" && col.nameClass != "updateBy"
          && col.nameClass != "version"
          && col.nameClass != "createTime" && col.nameClass != "updateTime">
            ${col.nameClass? uncap_first}: res.data.${col.nameClass? uncap_first},
          </#if>
          </#list>
          </#if>
          };
          <#if foreignKeys?? && (foreignKeys?size > 0) >
          <#list foreignKeys as foreignKey>
          <#if foreignKey.joinTableNameClass != nameClass>
          // ${foreignKey.comment}列表
          this.${foreignKey.joinTableNameClass? uncap_first}List = res.data.${foreignKey.joinTableNameClass? uncap_first}VoList == null ? [] : res.data.${foreignKey.joinTableNameClass? uncap_first}VoList;
          </#if>
          </#list>
          </#if>
          <#if mergeTables?? && (mergeTables?size > 0) >
          <#list mergeTables as mergeTable>
          <#if mergeTable.leftTable == mergeTable.maintain>
          // ${mergeTable.mysqlTable.comment}列表
          this.${mergeTable.mysqlTable.nameClass? uncap_first}List = res.data.${mergeTable.mysqlTable.nameClass? uncap_first}VoList == null ? [] : res.data.${mergeTable.mysqlTable.nameClass? uncap_first}VoList;
          </#if>
          </#list>
          </#if>
          this.fileList = res.data?.${nameClass? uncap_first}FileVoList.map(p => {
            return {
              uid: p.id,
              id: p.id,
              name: p.originalName,
              status: 'done',
              url: p.filePath,
              filePath: p.fileUri,
              type: p.type,
            };
          });
          this.demands = res.data.demands;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
  },
  mounted() {
    if (this.detailId) {
      this.getDetailInfo();
    }
  },
};
</script>

<style lang="less" scoped>
.modal-box {
  :deep(.ant-modal-footer) {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.add-icon {
  color: #4d7bdf;
  font-size: 22px;
  margin-top: 10px;
}

.dynamic-delete-button {
  position: relative;
  top: 4px;
  font-size: 22px;
  color: #999;
  transition: all 0.3s;
  margin-left: 10px;
}

.upload-box {
  display: flex;
  flex: 1;
  text-align: left;
}
</style>
