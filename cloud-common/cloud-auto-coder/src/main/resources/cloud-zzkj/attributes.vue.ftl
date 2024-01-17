<template>
  <a-modal
    title="新增${comment}"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
  >
    <template slot="footer">
        <a-button key="back" @click="handleCancel">取消</a-button>
        <a-button key="submit" type="primary" @click="handleSubmit">添加</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <#if column?? && (column?size > 0) >
        <#list column as col>
          <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
          && col.nameClass != "createDate" && col.nameClass != "updateDate"
          && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
          && col.nameClass != "createBy" && col.nameClass != "updateBy"
          && col.nameClass != "version" && col.nameClass != "id"
          && col.nameClass != "createTime" && col.nameClass != "updateTime">
            <#if col.enums?? && (col.enums?size > 0) >
      <a-row :gutter="24">
        <a-col :md="24" :sm="24">
          <a-form :form="form">
            <a-form-item label="${col.webComment}" :labelCol="labelCol" :wrapperCol="wrapperCol" has-feedback>
              <a-select style="width: 100%" placeholder="请选择${col.webComment}" v-model="${col.nameClass? uncap_first}">
                <#list col.enums as enum>
                <a-select-option :value="${enum.key}">${enum.value}</a-select-option>
                </#list>
              </a-select>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
            <#else>
            <#if col.type == 'String'>
      <a-row :gutter="24">
        <a-col :md="24" :sm="24">
          <a-form :form="form">
            <a-form-item label="${col.comment}" :labelCol="labelCol" :wrapperCol="wrapperCol" has-feedback>
              <a-input placeholder="请输入${col.comment}" :maxLength="${col.length?c}" v-model="${col.nameClass? uncap_first}"/>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
              </#if>
              <#if col.type == 'Date' ||  col.type == 'java.util.Date'>
      <a-row :gutter="24">
        <a-col :md="24" :sm="24">
          <a-form :form="form">
            <a-form-item label="${col.comment}" :labelCol="labelCol" :wrapperCol="wrapperCol" has-feedback>
              <a-date-picker @change="onChange" v-model="${col.nameClass? uncap_first}" placeholder="请选择${col.comment}" />
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
              </#if>
              <#if col.type == 'Double' || col.type == 'Long' || col.type == 'Integer'>
      <a-row :gutter="24">
        <a-col :md="24" :sm="24">
          <a-form :form="form">
            <a-form-item label="${col.comment}" :labelCol="labelCol" :wrapperCol="wrapperCol" has-feedback>
              <a-input-number v-model="${col.nameClass? uncap_first}" :min="1" :precision="0" :step="1" placeholder="${col.comment}"  style="width: 100%"/>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
              </#if>
            </#if>
          </#if>
        </#list>
      </#if>
    </a-spin>
  </a-modal>
</template>

<script>
import moment from 'moment';
export default {
  components: {
  },
  data() {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      visible: false,
      confirmLoading: false,
      form: this.$form.createForm(this),
      key: '',
    <#if column?? && (column?size > 0) >
    <#list column as col>
    <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
    && col.nameClass != "createDate" && col.nameClass != "updateDate"
    && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
    && col.nameClass != "createBy" && col.nameClass != "updateBy"
    && col.nameClass != "version"
    && col.nameClass != "createTime" && col.nameClass != "updateTime">
      ${col.nameClass? uncap_first}: null,
    </#if>
    </#list>
    </#if>
    };
  },
  methods: {
    // 初始化方法
    initData(record) {
      this.visible = true;
      if (record && record.key) {
        this.key = record.key;
        this.$nextTick(() => {
<#if column?? && (column?size > 0) >
<#list column as col>
<#if col.nameClass != "createUser" && col.nameClass != "updateUser"
&& col.nameClass != "createDate" && col.nameClass != "updateDate"
&& col.nameClass != "isDelete" && col.nameClass != "isDeleted"
&& col.nameClass != "createBy" && col.nameClass != "updateBy"
&& col.nameClass != "version"
&& col.nameClass != "createTime" && col.nameClass != "updateTime">
          this.${col.nameClass? uncap_first} = record.${col.nameClass? uncap_first};
</#if>
</#list>
</#if>
        });
      }
    },
    // 时间
    onChange(value) {
      this.formFields.forEach((item) => {
        if (item.key === 'extensionTime') {
          this.formData.extensionTime = moment(value).format('YYYY-MM-DD');
        }
      });
    },
    handleSubmit() {
      const {
        form: { validateFields },
      } = this;
      validateFields((errors, values) => {
        if (!errors) {
          this.confirmLoading = true;
          if (!this.key || this.key === '') {
            values.key = Math.floor(Math.random() * (9999999999 - 1111111111)) + 1111111111;
          } else {
            values.key = this.key;
          }
          <#if column?? && (column?size > 0) >
          <#list column as col>
          <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
          && col.nameClass != "createDate" && col.nameClass != "updateDate"
          && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
          && col.nameClass != "createBy" && col.nameClass != "updateBy"
          && col.nameClass != "version"
          && col.nameClass != "createTime" && col.nameClass != "updateTime">
          values.${col.nameClass? uncap_first} = this.${col.nameClass? uncap_first};
          </#if>
          </#list>
          </#if>
          this.$emit('confirm', values);
          this.$emit('handleCancel');
          this.handleCancel();
        }
      });
    },
    handleCancel() {
      this.key = '';
      this.form.resetFields();
      this.confirmLoading = false;
      this.visible = false;
      <#if column?? && (column?size > 0) >
      <#list column as col>
      <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
      && col.nameClass != "createDate" && col.nameClass != "updateDate"
      && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
      && col.nameClass != "createBy" && col.nameClass != "updateBy"
      && col.nameClass != "version"
      && col.nameClass != "createTime" && col.nameClass != "updateTime">
      this.${col.nameClass? uncap_first} = null;
      </#if>
      </#list>
      </#if>
    },
  },
};
</script>
