<template>
  <div class="borderPadding propageEdit">
    <!-- //编辑 -->
    <Form
      ref="form${table.className}Ref"
      :model="formValidate"
      :rules="ruleValidate"
      label-position="top">
<#assign x = 1>
<#list table.column as col>
<#if col.nameClass != "Id" && col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
          && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate" && col.nameClass != "IsDeleted">
<#if x % 2 != 0>
      <div class="fromRow">
</#if>
<#if col.type == 'varchar'>
        <FormItem
          label="${col.comment}"
          prop="${col.nameClass? uncap_first}">
          <Input
            v-model="formValidate.${col.nameClass? uncap_first}"
            placeholder="${col.comment}"
            maxlength="${col.length}" />
        </FormItem>
<#elseif col.type == 'datetime'>
        <Form-item
          label="${col.comment}"
          prop="${col.nameClass? uncap_first}">
          <DatePicker
            @on-change="value => formValidate.${col.nameClass? uncap_first} = value"
            type="datetime"
            placeholder="选择时间"
            style="width: 96.5%"
            format="yyyy-MM-dd HH:mm:ss"
            :value="formValidate.${col.nameClass? uncap_first}" />
        </Form-item>
<#else>
        <FormItem
          label="${col.comment}"
          prop="${col.nameClass? uncap_first}">
          <Input
            v-model="formValidate.${col.nameClass? uncap_first}"
            placeholder="${col.comment}" />
        </FormItem>
</#if>
<#if x % 2 == 0>
      </div>
</#if>
<#assign x = x + 1>
</#if>
</#list>
<#if x % 2 == 0>
      </div>
</#if>
    </Form>
  </div>
</template>
<script>
// 当前页面使用的接口
import apis from './../apis.js'

export default {
  name: 'FormEdit',
  data() {
    return {
      resetParam() {
        this.$refs.form${table.className}Ref.resetFields()
        this.formValidate.id = ''
      },
      formValidate: {
        id: '',
<#list table.column as col>
<#if col.nameClass != "Id" && col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate" && col.nameClass != "IsDeleted">
        ${col.nameClass? uncap_first}: '',
</#if>
</#list>
      },
      ruleValidate: {
<#list table.column as col>
<#if col.nameClass != "Id" && col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"
        && col.nameClass != "CreateDate" && col.nameClass != "UpdateDate" && col.nameClass != "IsDeleted">
<#if col.requiredType == 'true'>
        ${col.nameClass? uncap_first}: [
          { required: true, message: '${col.comment}必须填写', trigger: 'blur'<#if col.type == 'varchar'>, maxlength: '${col.length}'</#if> },
        ],
</#if>
</#if>
</#list>
      },
    }
  },
  methods: {
    async getById(id) {
      apis.getById({ id: id }).then(res => {
        this.formValidate = res
      })
    },
    cancel() {
      this.$refs.form${table.className}Ref.resetFields()
    },
    ok() {
      this.$refs.form${table.className}Ref.validate(v => {
        if (v) {
          const formValidate = Object.assign({}, this.formValidate)
          apis.addData(formValidate).then(res => {
            if (res) {
              this.$msg(this, 'success', '操作成功！')
              this.$emit('closeModal')
              this.$emit('refreshList')
              this.$refs.form${table.className}Ref.resetFields()
            }
          })
        } else {
          console.log('我是错误的')
        }
      })
    },
  },
}
</script>
<style lang="scss">
.placeholder {
  color: $COLOR-ccc;
}
.borderPadding {
  padding: 8px;
}
.fromRow {
  display: grid;
  grid-template-columns: 47.8% 47.8%;
  grid-gap: 32px;
  label {
    color: $COLOR-666;
  }
}
.fromRow1 {
  display: grid;
  grid-template-columns: 30% 30% 30%;
  grid-gap: 32px;
  label {
    color: $COLOR-666;
  }
}
.fromRow2 {
  display: grid;
  grid-template-columns: 100%;
  grid-gap: 32px;
  label {
    color: $COLOR-666;
  }
}
.propageEdit /deep/ .ivu-icon-ios-loading:before {
  display: none;
}
</style>
