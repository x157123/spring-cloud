<template>
  <!-- 纠纷调解-我的待办-待处理 -->
  <div>
    <a-spin :spinning="loading">
      <div class="headersearch">
        <dynamic-form :formFields="formFields" ref="dynamicForm" layout="inline" :moduleType="'mediationDisputeType'"
                      :isValue="false">
          <a-form-model-item>
            <a-button style="margin: 0 10px" type="primary" @click="onquery">
              查询
            </a-button>
            <a-button @click="resetForm">重置</a-button>
          </a-form-model-item>
        </dynamic-form>
      </div>
      <!-- 按钮区域 -->
      <div class="btn-content">
        <a-button type="primary" @click="handleAdd">新增</a-button>
        <a-button type="danger" style="margin-left: 20px" @click="handleDelete()">删除</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table :columns="columns" :data-source="data" bordered :row-selection="{
        selectedRowKeys: selectedRowKeys,
        onChange: onSelectChange,
        columnTitle: ' ',
      }" :scroll="{ x: 'calc(600px + 50%)', y: 600 }" rowKey="id" :pagination="false">
        <a slot="name" slot-scope="text">{{ text }}</a>

<#if column?? && (column?size > 0) >
<#list column as col>
<#if col.enums?? && (col.enums?size > 0) >
        <a-tag slot="${col.nameClass? uncap_first}" slot-scope="${col.nameClass? uncap_first}" :color="${col.nameClass? uncap_first}Text[${col.nameClass? uncap_first}].color">
          {{ ${col.nameClass? uncap_first}Text[${col.nameClass? uncap_first}].label }}
        </a-tag>
</#if>
</#list>
        </#if>
        <span slot="action" slot-scope="text, record" class="table-tabs">
          <a-button @click="openDetail(record, 'view')">查看</a-button>
          <a-button @click="handleEdit(record)">编辑</a-button>
          <a-button @click="openDetail(record, 'process')">受理</a-button>
        </span>
      </a-table>
      <!-- 分页 -->
      <Pagination v-model="currentPage" :total="total" @change="handlePageChange" class="page-card"/>
      <!-- 新增/编辑 -->
      <${nameClass? uncap_first}Edit :title="handleType === 1 ? '新增' : '编辑'" :formFields="editFormFields"
                              v-if="editVisible" :visible="editVisible" :formRules="editFormRules" @editOk="submitForm"
                              @editCancel="handleEditCancel" ref="editFrom" :detailId="detailId"
                              :handleType="handleType">
      </${nameClass? uncap_first}Edit>
      <${nameClass? uncap_first}Detail ref="${nameClass? uncap_first}DetailRef" title="详情" :visible="detailVisible"
                                  @editCancel="closeDetail" v-if="detailVisible" :detailId="detailId"
                                  :listdata="listdata">
      </${nameClass? uncap_first}Detail>
    </a-spin>
  </div>
</template>

<script>
// 组件
import { Pagination } from 'ant-design-vue';
import dynamicForm from '@/${web}/form/dynamicForm';
import ${nameClass? uncap_first}Edit from './${nameClass? uncap_first}Edit.vue';
import ${nameClass? uncap_first}Detail from './${nameClass? uncap_first}Detail.vue';
// 接口
import {
  ${nameClass? uncap_first}DisputePage,
  ${nameClass? uncap_first}InfoDelete,
  ${nameClass? uncap_first}InfoAdd,
  ${nameClass? uncap_first}InfoUpdate,
} from '@/${web}/api${webExpandPackage}/${nameClass? uncap_first}/${nameClass? uncap_first}Api';
// 表单信息
const columns = [
  <#if column?? && (column?size > 0) >
  <#list column as col>
  <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
  && col.nameClass != "createDate" && col.nameClass != "updateDate"
  && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
  && col.nameClass != "createBy" && col.nameClass != "updateBy"
  && col.nameClass != "version" && col.nameClass != "id"
  && col.nameClass != "createTime" && col.nameClass != "updateTime">
  {
    title: '${col.webComment}',
    key: '${col.nameClass? uncap_first}',
    dataIndex: '${col.nameClass? uncap_first}',
    scopedSlots: { customRender: '${col.nameClass? uncap_first}' },
    width: '200px',
  },
</#if>
</#list>
</#if>
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: '340px',
    scopedSlots: { customRender: 'action' },
  },
];

export default {
  components: {
    dynamicForm,
    ${nameClass? uncap_first}Edit,
    ${nameClass? uncap_first}Detail,
    Pagination,
  },
  data() {
    return {
      editVisible: false, // 编辑弹窗
      operateType: 'edit', // 编辑弹窗
      confirmLoading: false, // 编辑弹窗确认按钮是否加载中
      data: [],
      columns,
      <#if column?? && (column?size > 0) >
      <#list column as col>
      <#if col.enums?? && (col.enums?size > 0) >
      ${col.nameClass? uncap_first}Text: {
        <#list col.enums as enum>
        ${enum.key}: {
          label: '${enum.value}',
          value: ${enum.key},
          color: '#f50',
        },
        </#list>
      },
      </#if>
      </#list>
      </#if>
      formFields: [
        <#if column?? && (column?size > 0) >
        <#list column as col>
        <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "createDate" && col.nameClass != "updateDate"
        && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
        && col.nameClass != "createBy" && col.nameClass != "updateBy"
        && col.nameClass != "version"
        && col.nameClass != "createTime" && col.nameClass != "updateTime">
        {
          label: '${col.webComment}',
          key: '${col.nameClass? uncap_first}',
          type: 'input',
          placeholder: '请输入${col.webComment}',
        },
        </#if>
        </#list>
        </#if>
      ], // 查询表单
      selectedRowKeys: [], // 切换分页需要清空, 不然切换分页选中的数据是会连上的
      editFormFields: [
        <#if column?? && (column?size > 0) >
        <#list column as col>
        <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
        && col.nameClass != "createDate" && col.nameClass != "updateDate"
        && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
        && col.nameClass != "createBy" && col.nameClass != "updateBy"
        && col.nameClass != "version"
        && col.nameClass != "createTime" && col.nameClass != "updateTime">
        <#if col.enums?? && (col.enums?size > 0) >
        {
          key: '${col.nameClass? uncap_first}',
          label: '${col.webComment}',
          type: 'select',
          placeholder: '请选择${col.webComment}',
          selectType: 'source',
          selectMode: 'multiple',
          options: [
            <#list col.enums as enum>
            { label: '${enum.value}', value: ${enum.key} },
            </#list>
          ],
        },
        <#else>
          <#if col.type == 'String'>
        {
          label: '${col.comment}',
          key: '${col.nameClass? uncap_first}',
          type: 'input',
          placeholder: '请输入${col.nameClass? uncap_first}',
        },
          </#if>
          <#if col.type == 'Date' ||  col.type == 'java.util.Date'>
        {
          key: '${col.nameClass? uncap_first}',
          label: '${col.comment}',
          type: 'time',
          timeType: 0,
          placeholder: '请选择发现时间',
          isDisable: true,
        },
          </#if>
          <#if col.type == 'Double' || col.type == 'Long' || col.type == 'Integer'>
        {
          key: '${col.nameClass? uncap_first}',
          label: '${col.comment}',
          type: 'number',
          placeholder: '请输入${col.comment}',
          style: 'width:140px',
          maxLength: 5,
        },
          </#if>
        </#if>
        </#if>
        </#list>
        </#if>
      ], // 编辑表单
      editFormRules: {
    <#if column?? && (column?size > 0) >
    <#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
    && col.nameClass != "createDate" && col.nameClass != "updateDate"
    && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
    && col.nameClass != "createBy" && col.nameClass != "updateBy"
    && col.nameClass != "version"
    && col.nameClass != "createTime" && col.nameClass != "updateTime">
        <#if (col.enums?? && (col.enums?size > 0)) || col.type == 'Date' || col.type == 'java.util.Date'>
        ${col.nameClass? uncap_first}: [
          {
            required: true,
            message: '${col.webComment}为必填项',
            trigger: 'change',
          },
        ],
        <#else>
        ${col.nameClass? uncap_first}: [
          {
            required: true,
            message: '${col.webComment}为必填项',
            trigger: 'blur',
          },
        ],
        </#if>
    </#if>
    </#list>
    </#if>
      }, // 校验规则
      detailVisible: false,
      // 分页
      currentPage: 1,
      total: 0,
      detailId: '', // 查看详情ID
      loading: false,
      handleType: null,
      detailInfoData: {}, // 获取编辑回显数据
      listdata: {}, // 列表数据
    };
  },
  methods: {
    // 提交表单
    submitForm() {
      this.$refs.dynamicForm
        .validateForm()
        .then((formData) => {
          // 表单校验通过，可以进行提交操作
          this.loading = true;
          delete formData.materOrgId;
          delete formData.occurOrgId;
          let params = {
            ...formData,
            stepStatus: 0,
            current: this.currentPage,
          };
          ${nameClass? uncap_first}DisputePage(params).then((res) => {
            this.loading = false;
            if (res.success) {
              this.data = res.data.records;
              this.total = res.data.total;
            } else {
              this.$message.error(res.message);
            }
          });
        })
        .catch((error) => {
          // 表单校验不通过，做出相应的处理
          console.error(error);
        });
    },
    // 分页change
    handlePageChange(current) {
      this.currentPage = current;
      this.submitForm();
    },
    onquery() {
      this.currentPage = 1;
      this.submitForm();
    },
    // 重置表单
    resetForm() {
      this.$refs.dynamicForm.resetForm();
      this.submitForm();
    },
    // 删除
    handleDelete() {
      if (!this.selectedRowKeys || !this.selectedRowKeys.length) {
        this.$message.warning({
          content: '请选择一条删除数据！',
          key: 1,
        });
        return false;
      }
      this.$confirm({
        title: '删除',
        content: '是否确认删除？',
        okText: '确认',
        cancelText: '取消',
        onOk: () => {
          ${nameClass? uncap_first}InfoDelete(this.selectedRowKeys[0]).then((res) => {
            if (res.success) {
              this.$message.success(res.msg);
              this.submitForm();
              this.selectedRowKeys = [];
            } else {
              this.$message.error(res.msg);
            }
          });
        },
      });
    },
    // 查看
    openDetail(record, operateType, isMaster) {
      this.detailId = record.id;
      this.listdata = record;
      this.detailVisible = true;
      this.$nextTick().then(() => {
        this.$refs.${nameClass? uncap_first}DetailRef.init(operateType, isMaster);
      });
    },
    // 办理
    handleTransact(item) {
      this.listdata = item;
      this.openDetail(item, 'deal', item.isMaster);
    },
    // 选择
    onSelectChange(selectedRowKeys) {
      if (selectedRowKeys.length > 1) {
        this.$message.warning('删除只能单个选择数据！');
        return false;
      }
      this.selectedRowKeys = selectedRowKeys;
    },
    // 新增
    handleAdd() {
      this.editVisible = true;
      this.handleType = 1;
      this.detailId = '';
    },
    // 编辑
    handleEdit(value) {
      this.editVisible = true;
      this.handleType = 2;
      this.detailId = value.id;
    },
    // 新增/编辑弹窗确认--目前遗弃使用
    handleEditOk(data) {
      this.confirmLoading = true;
      this.$refs.editFrom
        .validateForm()
        .then(() => {
          // 表单校验通过，可以进行提交操作
          if (!this.partiesList || !this.partiesList.length) {
            this.$message.warning({
              content: '请添加当事人列表！',
              key: 1,
            });
            return false;
          }
          // data.typeIds = data.typeIds.split(',');
          // 新增
          if (this.handleType === 1) {
            let params = {
              ...data,
              source: 1, // 数据来源（0：研判会商，1：新增）
              persons: this.partiesList,
            };
            ${nameClass? uncap_first}InfoAdd(params).then((res) => {
              this.loading = false;
              if (res.success) {
                this.$message.success(res.msg);
                this.editVisible = false;
                this.selectedRowKeys = [];
                this.submitForm();
              } else {
                this.$message.error(res.msg);
              }
            }).finally(() => {
              this.confirmLoading = false;
            });
          } else {
            // 编辑提交
            let params = {
              ...data,
              id: this.detailId,
              persons: this.partiesList,
            };
            ${nameClass? uncap_first}InfoUpdate(params).then((res) => {
              this.loading = false;
              if (res.success) {
                this.$message.success(res.msg);
                this.editVisible = false;
                this.selectedRowKeys = [];
                this.submitForm();
              } else {
                this.$message.error(res.msg);
              }
            }).finally(() => {
              this.confirmLoading = false;
            });
          }
        })
        .catch((error) => {
          this.confirmLoading = false;
          // 表单校验不通过，做出相应的处理
          console.error(error);
        });
    },
    // 编辑弹窗取消
    handleEditCancel(e) {
      this.editVisible = false;
      this.submitForm();
    },
    // 关闭
    closeDetail() {
      this.detailVisible = false;
      this.submitForm();
    },
  },
  mounted() {
    this.submitForm();
    if (Object.keys(this.$route.params).length) {
      this.submitForm();
    }
  },
};
</script>

<style lang="less" scoped>
.btn-content {
  margin: 10px 0;
  padding: 10px 0;
  border-top: 1px dashed #ccc;
}

.table-tabs {
  :deep(button + button) {
    margin-left: 10px;
  }
}

.edit-tit {
  margin: 20px 0 10px;

  h3 {
    margin: 0;
  }
}

// 分页
.page-card {
  display: flex;
  justify-content: flex-end;
  margin: 15px 0;
}

.headersearch {
  height: 20%;
  padding: 10px 10px;
  border: 1px dashed #ccc;
  margin-top: 10px;
}
</style>
