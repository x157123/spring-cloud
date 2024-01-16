<template>
  <div>
    <!-- 矛盾纠纷编辑弹窗 -->
    <a-modal :title="title" :width="1200" :visible="visible" @cancel="handleEditCancel" destroyOnClose class="modal-box">
      <dynamic-form :formFields="formFields" :formRules="formRules" ref="formRef" layout="inline" :btnText="'上传附件'"
        :moduleType="'mediationDisputeType'" :echoData="detailInfoData" :handleType="handleType" :detailId="detailId">
      </dynamic-form>
      <a-row type="flex" justify="space-between" class="edit-tit">
        <a-col flex="auto">
          <h3>
            <span style="color: red">*</span>
            当事人列表
          </h3>
        </a-col>
        <a-col flex="30px">
          <a-icon type="plus-circle" theme="filled" style="color: #4d7bdf;font-size: 22px;" @click="handleAddPeople" />
        </a-col>
      </a-row>
      <a-table ref="tablePeopleRef" :columns="partiesColumns" :data-source="partiesList" :scroll="{ x: 'calc(600px + 50%)', y: 600 }" bordered :pagination="false">
        <a slot="name" slot-scope="text">{{ text }}</a>
        <a slot="actions" slot-scope="text,record,index">
          <a-popconfirm title="是否删除?" @confirm="() => deleteOne(index)">
            <a class="ml10">删除</a>
          </a-popconfirm>
          <a class="ml10" @click="handleEditPeople(record)">编辑</a>
        </a>
      </a-table>
      <slot></slot>
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
          add${foreignKey.joinTableNameClass},
    <!-- ${foreignKey.comment} -->
    <add${foreignKey.joinTableNameClass? uncap_first} ref="add${foreignKey.joinTableNameClass? uncap_first}Ref" v-if="add${foreignKey.joinTableNameClass? uncap_first}Visible" @confirm="handleAdd${foreignKey.joinTableNameClass? uncap_first}" @handleCancel="handleAdd${foreignKey.joinTableNameClass? uncap_first}Cancel" />
        </#if>
      </#list>
    </#if>
    <#if mergeTables?? && (mergeTables?size > 0) >
      <#list mergeTables as mergeTable>
        <#if mergeTable.leftTable == mergeTable.maintain>
    <!-- ${mergeTable.comment} -->
    <!-- <add${mergeTable.tableNameClass? uncap_first} ref="add${mergeTable.tableNameClass? uncap_first}Ref" v-if="add${mergeTable.tableNameClass? uncap_first}Visible" @confirm="handleAdd${mergeTable.tableNameClass? uncap_first}" @handleCancel="handleAdd${mergeTable.tableNameClass? uncap_first}Cancel" /> -->
        </#if>
      </#list>
    </#if>
    <!-- 添加当事人弹框 -->
    <addProtyPop ref="addPartyPopupRef" v-if="addPartyVisible" @confirm="handleAddParty" @handleCancel="handleAddPartyCancel" />
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
import add${foreignKey.joinTableNameClass} from '@/${web}/${foreignKey.joinTableNameClass}/${foreignKey.joinTableNameClass}Attributes.vue';
</#if>
</#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
<#list mergeTables as mergeTable>
<#if mergeTable.leftTable == mergeTable.maintain>
// ${mergeTable.comment}
import add${mergeTable.tableNameClass? uncap_first} from '@/${web}/${mergeTable.tableNameClass? uncap_first}/${mergeTable.tableNameClass? uncap_first}Attributes.vue';
</#if>
</#list>
</#if>
// 接口
import {
  appealInfoFull, appealInfoAdd, appealInfoUpdate,
} from '@/api/modular/${nameClass? uncap_first}/${nameClass? uncap_first}Api';
import moment from 'moment';
let that = null;
export default {
  name: 'contradictionEditPopup',
  components: {
    dynamicForm,
    <#if foreignKeys?? && (foreignKeys?size > 0) >
    <#list foreignKeys as foreignKey>
    <#if foreignKey.joinTableNameClass != nameClass>
    add${foreignKey.joinTableNameClass},
    </#if>
    </#list>
    </#if>
    <#if mergeTables?? && (mergeTables?size > 0) >
    <#list mergeTables as mergeTable>
    <#if mergeTable.leftTable == mergeTable.maintain>
    add${mergeTable.tableNameClass? uncap_first},
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
      mediation: {},
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
      fileDeletedIds: [],
      confirmLoading: false,
      // 当事人表头
      partiesColumns: [
        {
          key: 'name',
          title: '姓名',
          dataIndex: 'name',
          width: 120,
        },
        {
          key: 'idCardNo',
          title: '身份证号码',
          dataIndex: 'idCardNo',
          width: 200,
        },
        {
          key: 'phone',
          title: '联系电话',
          dataIndex: 'phone',
          width: 200,
        },
        {
          key: 'address',
          title: '户籍地址',
          ellipsis: true,
          dataIndex: 'address',
          width: 240,
        },
        {
          key: 'permanentAddress',
          title: '常住地址',
          ellipsis: true,
          dataIndex: 'permanentAddress',
          width: 240,
        },
        {
          key: 'actions',
          title: '操作',
          fixed: 'right',
          dataIndex: 'actions',
          width: 160,
          scopedSlots: { customRender: 'actions' },
        },
      ],
      // 当事人列表
      partiesList: [],
      addPartyVisible: false, // 添加当事人弹框
      btnLoading: false, // 确认按钮loading
    };
  },
  methods: {
    deleteOne(index) {
      this.partiesList.splice(index, 1);
    },
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
      this.fileDeletedIds.push(file.uid);
      if (i === -1) return;
      this.fileList.splice(i, 1);
    },
    // 新增/编辑弹窗确认
    handleEditOk() {
      this.$refs.formRef
        .validateForm()
        .then((formData) => {
          // 表单校验通过，可以进行提交操作
          if (!this.partiesList || !this.partiesList.length) {
            this.$message.warning({
              content: '请添加当事人列表！',
              key: 1,
            });
            return false;
          }
          this.btnLoading = true;
          // 新增
          if (this.handleType === 1) {
            let params = {
              ...formData,
              source: formData.source, // 数据来源（0：研判会商，1：新增）
              persons: this.partiesList,
              occurTime: moment(formData.occurTime).format('YYYY-MM-DD HH:mm:ss'),
              demands: this.demands,
              files: this.fileList,
            };
            appealInfoAdd(params).then((res) => {
              if (res.success) {
                this.$message.success(res.msg);
                this.fileList = [];
                this.partiesList = [];
                this.demands = [
                  {
                    demand: '',
                    id: Date.now(),
                  },
                ];
                this.handleEditCancel();
                this.$emit('editOk');
              } else {
                this.$message.error(res.msg);
              }
            }).finally(() => {
              this.btnLoading = false;
            });
          } else {
            // 编辑提交
            let params = {
              ...formData,
              id: this.detailId,
              persons: this.partiesList,
              occurTime: moment(formData.occurTime).format('YYYY-MM-DD HH:mm:ss'),
              demands: this.demands,
              files: this.fileList,
              demandDeletedIds: this.demandDeletedIds,
              fileDeletedIds: this.fileDeletedIds,
            };
            appealInfoUpdate(params).then((res) => {
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
          }
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
      this.partiesList = [];
      this.fileList = [];
      this.$refs.formRef.resetForm();
    },
    validateForm() {
      return that.$refs.formRef
        .validateForm();
    },
    // 添加主要诉求
    handleAddDemand() {
      this.demands.push({
        demand: '',
        id: Date.now(),
      });
    },
    // 移除主要诉求
    removeDomain(item) {
      let index = this.demands.indexOf(item);
      if (index !== -1) {
        this.demands.splice(index, 1);
      }
      if (this.detailId) {
        this.demandDeletedIds.push(item.id);
      }
    },
    // 打开添加当事人列表弹框
    handleAddPeople() {
      this.addPartyVisible = true;
      this.$nextTick().then(() => {
        this.$refs.addPartyPopupRef.initData();
      });
    },
    handleEditPeople(record) {
      this.addPartyVisible = true;
      this.$nextTick().then(() => {
        this.$refs.addPartyPopupRef.initData(record);
      });
    },
    // 添加
    handleAddParty(data) {
      let bool = 0;
      this.partiesList.forEach((item, index) => {
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
        this.partiesList.push(data);
      }
    },
    // 取消添加当事人
    handleAddPartyCancel() {
      this.addPartyVisible = false;
    },
    // 获取展示信息
    getDetailInfo() {
      appealInfoFull(this.detailId).then((res) => {
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
          // 当事人列表
          this.partiesList = res.data.appealPersonVoList;
          this.fileList = res.data?.appealFileVoList.map(p => {
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
