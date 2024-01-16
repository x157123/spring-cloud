<template>
  <a-form-model :model="formData" :rules="formRules" ref="formRef" :layout="layout">
    <!-- isShowLevel: 待办-调解情况-判断是否展示定级
         isShowUpload: 待办-调解情况-判断是否展示协议或口头协议字段 -->
    <a-form-model-item v-for="field in formFields" :key="field.key" :label="(field.selectType === 'preliminaryLevel' && !isShowLevel) ||
      (field.uploadType === 'oralUpload' && !isShowUpload)
      ? ''
      : (field.key === 'otherFunction' && !isShowOtherFunction) ? '' : field.label
      " :prop="(field.selectType === 'preliminaryLevel' && !isShowLevel) ||
    (field.uploadType === 'oralUpload' && !isShowUpload)
    ? ''
    : field.key" :labelCol="field.labelCol" :wrapperCol="field.wrapperCol" :class="{ 'is-require': field.isRequired }">
      <template v-if="field.type === 'divTime'">
        <div>{{ reportTime }}</div>
      </template>
      <template v-if="field.type === 'input'">
        <!--        <a-input :style="field.style" v-if="field.key === 'player'"-->
        <!--          v-model="formData[field.key]" :placeholder="field.placeholder" :maxLength="field.maxLength" @input="inputData"/>-->
        <a-input :style="field.style" v-model="formData[field.key]" :placeholder="field.placeholder"
                 :maxLength="field.maxLength" @input="inputData" :readOnly="handleType===99"/>
      </template>
      <template v-if="field.type === 'textarea'">
        <a-textarea :style="field.style" v-model="formData[field.key]" :placeholder="field.placeholder"
                    :auto-size="field.autoSize || { minRows: 3, maxRows: 5 }" :maxLength="field.maxLength"/>
      </template>
      <template v-if="field.type === 'select'">
        <template v-if="handleType === 12 && field.selectType === 'disputeOrgId'" >
          <span class="tips-icon">*</span>
        </template>
        <!-- 所属区域 -->
        <OrganizationCascader v-else-if="field.selectType === 'reportToOrgId'" ref="showReportToOrgIdCascaderRef"
                              :width="285" :minLevel="5" :multiple="multiple" :disabled="field.isDisabled"/>
        <OrganizationCascader v-else-if="field.selectType === 'disputeOrgId'" ref="showOrgCascaderRef"
                              @orgChange="orgChange" :width="285" :minLevel="5" :multiple="multiple"
                              :disabled="field.isDisabled"
                              :isValue="isValue"/>
        <OrganizationCascader v-else-if="field.selectType === 'occurOrgId'" ref="showOrgCascaderRef"
                              @orgChange="orgChange" :width="960" :minLevel="5" :multiple="true"
                              :disabled="field.isDisabled"
                              :isValue="isValue" :defaultType="1"/>
        <!-- 调解、协办单位，多选-->
        <OrganizationMulSelect v-else-if="field.selectType === 'slaveOrgIds' ||
          field.selectType === 'joinOrgId'
          " treeCheckable multiple :isSetDefault="false" ref="showOrganizationMulSelectRef" @orgChange="slaveOrgChange"
                               :width="null"/>
        <a-select v-else v-model="formData[field.key]" :placeholder="field.placeholder" style="min-width: 180px">
          <a-select-option v-for="option in field.options" :key="option.value" :value="option.value">
            {{ option.label }}
          </a-select-option>
        </a-select>
        <template v-if="isMaterOrgId && field.selectType === 'disputeOrgId'">
          <span style="color: red">请选择主办单位</span>
        </template>
      </template>
      <template v-if="field.type === 'checkbox'">
        <a-checkbox-group v-if="field.key === 'otherFunction' && isShowOtherFunction" v-model="formData[field.key]">
          <a-checkbox v-for="option in field.options" :key="option.value" :value="option.value">
            {{ option.label }}
          </a-checkbox>
        </a-checkbox-group>
        <a-checkbox-group v-if="field.key !== 'otherFunction'" style="display: grid;" v-model="formData[field.key]">
          <a-checkbox v-for="option in field.options" :key="option.value" :value="option.value"
                      style="margin-left: 8px;margin-top: 6px;">
            {{ option.label }}
          </a-checkbox>
        </a-checkbox-group>
      </template>
      <template v-if="field.type === 'time'">
        <a-date-picker @change="onChange" v-model="formData[field.key]" :placeholder="field.placeholder"
                       :style="field.style" :disabledDate="field.isDisable ? disabledDate : null"/>
      </template>
      <template v-if="field.type === 'timeRange'">
        <a-range-picker show-time separator="-" :placeholder="['开始时间', '结束时间']" v-model="formData[field.key]"
                        valueFormat="YYYY-MM-DD HH:mm:ss">
          <a-icon slot="suffixIcon" type="calendar" />
        </a-range-picker>
      </template>
      <template v-if="field.type === 'rangeTime'">
        <a-range-picker v-model="formData[field.key]" :show-time="{ format: 'HH:mm:ss' }" format="YYYY-MM-DD HH:mm:ss"
                        :placeholder="field.placeholder" @change="onRangeChange" :style="field.style"/>
      </template>
      <template v-if="field.type === 'number'">
        <a-input-number v-model="formData[field.key]" :min="1" :precision="0" :step="1" :placeholder="field.placeholder"
                        :style="field.style"/>
      </template>
      <template v-if="field.uploadType === 'oralUploadSimple'">
        <fileList v-model="formData[field.key]" :uoloadMultiple="field.uoloadMultiple"
                  @fileList="getFileListSimple"
                  :btnText="btnText" :accept="'.xml,.xlsx,.jpeg,.png,.jpg,.gif,.bmp,.svg'"/>
      </template>
      <template v-if="field.type === 'upload'">
        <fileList v-if="!field.uploadType" v-model="formData[field.key]" :uoloadMultiple="field.uoloadMultiple"
                  @fileList="getFileList"
                  :btnText="btnText" :accept="'.xml,.xlsx,.jpeg,.png,.jpg,.gif,.bmp,.svg'"/>
      </template>
      <template v-if="field.type === 'oralUpload'">
        <fileList v-if="!field.uploadType ||
          (field.uploadType === 'oralUpload' && isShowUpload)
          " v-model="formData[field.key]" :uoloadMultiple="field.uoloadMultiple" @fileList="getOralFileList"
                  :btnText="btnText" :accept="'.xml,.xlsx,.jpeg,.png,.jpg,.gif,.bmp,.svg'"/>
      </template>
      <a-form-model-rule v-for="rule in field.rules" :key="rule.field" :field="rule.field" :required="rule.required"
                         :message="rule.message" :type="rule.type"></a-form-model-rule>
    </a-form-model-item>
    <slot></slot>
  </a-form-model>
</template>

<script>
// 事发区域下拉组件
import OrganizationCascader from '@/components/User/OrganizationCascader.vue';
import OrganizationMulSelect from '@/components/User/OrganizationMulSelect.vue';
// 上传组件
import fileList from '@/components/mediatiionCenter/fileList.vue';
import { sysDictTypeDropDown } from '@/api/modular/system/dictManage';
import moment from 'moment';
// 接口
import { getParentByOrgId } from '@/api/modular/system/organization';

/**
 * @description 动态表单组件
 * @param {Array} formFields 表单字段配置 [{key: 'name', label: '姓名', type: 'input', placeholder: '请输入姓名'}]
 * @param {Object} formRules 表单校验规则 {name: [{ required: true, message: '请输入姓名', trigger: 'blur' }]}
 * @param {String} layout 表单布局 horizontal | vertical | inline
 * @method validateForm 表单校验方法，返回一个 Promise 对象
 * @method resetForm 表单重置方法
 */
export default {
  components: {
    OrganizationCascader,
    OrganizationMulSelect,
    fileList,
  },
  props: {
    layout: {
      type: String,
      default: 'horizontal',
    },
    formFields: {
      type: Array,
      required: true,
    },
    formRules: {
      type: Object,
      required: false,
    },
    /**
     * 模块类型
     * 目前这里用于判断是否执行下拉请求
     */
    moduleType: {
      type: String,
      default: '',
    },
    // 操作类型 1:新增 2:编辑 99:查看卷宗
    handleType: {
      type: Number,
      default: 1,
    },
    // 编辑回显数据
    echoData: {
      type: Object,
      default: () => {
      },
    },
    listdata: {
      type: Object,
      default: () => {
      },
    },
    // 编辑id
    detailId: {
      type: String,
      default: '',
    },
    // 区域是否多选
    multiple: {
      type: Boolean,
      default: false,
    },
    // uoload btn text
    btnText: {
      type: String,
      default: '',
    },
    // 录入部门是否有默认值
    isValue: {
      type: Boolean,
      default: true,
    },
    // 添加当事人-查询回填name
    identityName: {
      type: String,
      default: '',
    },
    // 添加当事人-查询回填身份
    identity: {
      type: Number,
    },
    identityPhone: {
      type: String,
      default: '',
    },
    identityIdCardNo: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      formData: {
        files: [],
        result: 0,
      },
      disputeTypeList: [], // 纠纷类型
      orgId: this.$store.getters.orgId,
      fileList: [],
      availableRoomList: [], //
      preliminaryLevelList: [], // 定级List
      reportTime: '', // 上报时间展示
      isShowLevel: true, // 调解情况-是否展示定级
      isShowUpload: true, // 调解情况-调解结果为成功才需要上传调解协议书或口头协议书
      isShowOtherFunction: false, // 其他途径是否展示，只有调解结果选择其他途径才显示
      isMaterOrgId: false, // 是否展示交办-主办单位必填语
    };
  },
  methods: {
    validateForm() {
      return new Promise((resolve, reject) => {
        this.$refs.formRef.validate((valid) => {
          console.log(this.formData, 'fffffffffffffffffffff');
          if (valid) {
            resolve(this.formData);
          } else {
            reject(new Error('表单校验不通过'));
          }
        });
      });
    },
    resetForm() {
      if (this.$refs.showOrgCascaderRef) {
        this.$nextTick().then(() => {
          this.isValue ? this.$refs.showOrgCascaderRef[0].init([String(this.orgId)]) : this.$refs.showOrgCascaderRef[0].init(null, null, [String(this.orgId)]);
        });
      }
      this.$refs.formRef.resetFields();
    },
    // 时间
    onChange(value) {
      this.formFields.forEach((item) => {
        if (item.key === 'extensionTime') {
          this.formData.extensionTime = moment(value).format('YYYY-MM-DD');
        }
      });
    },
    // 时间范围
    onRangeChange(value) {
      /* if (value && value.length) {
        this.formData.startTime = moment(value[0]).format(
          'YYYY-MM-DD HH:ss:mm',
        );
        this.formData.endTime = moment(value[1]).format('YYYY-MM-DD HH:ss:mm');
      } */
    },
    // 禁选时间
    disabledDate(current) {
      return current && current > moment().endOf('day');
    },
    setInitValue() {
      this.formFields.forEach((item) => {
        if (item.key === 'identity') {
          this.formData[item.key] = item.defaultValue;
          this.inputData();
        }
      });
    },
    slaveOrgChange(value) {
      this.formData.slaveOrgIds = value;
      this.formData.joinOrgId = value;
    },
    // change所属区域赋值
    orgChange(orgId) {
      this.formData.orgId = orgId;
      this.formData.materOrgId = orgId;
      this.formData.occurOrgId = orgId;
      if (this.handleType === 12) {
        // 交办-主办单位-判断是否展示必填提示
        if ((Object.prototype.toString.call(orgId) === '[object Number]' && orgId) || (Object.prototype.toString.call(orgId) === '[object String]' && orgId) || (Object.prototype.toString.call(orgId) === '[object Array]' && orgId.length)) {
          this.isMaterOrgId = false;
        } else {
          this.isMaterOrgId = true;
        }
      }
    },
    // 所属区域初始化方法
    initOrgData() {
      if (this.moduleType === 'mediationOrg' || this.moduleType === 'mediationDisputeType') {
        if (this.isValue) {
          this.orgChange(this.orgId);
        }
        if (this.$refs.showOrgCascaderRef) {
          this.$nextTick().then(() => {
            this.isValue ? this.$refs.showOrgCascaderRef[0].init([String(this.orgId)]) : this.$refs.showOrgCascaderRef[0].init(null, null, [String(this.orgId)]);
          });
        }
      }
      if (this.$refs.showReportToOrgIdCascaderRef) {
        // 先查询获取上级机构ID
        getParentByOrgId(this.orgId).then(res => {
          if (res.success) {
            this.orgChange(res.data.id);
            this.$nextTick().then(() => {
              this.$refs.showReportToOrgIdCascaderRef[0].init([String(res.data.id)]);
            });
          }
        });
      }
      if (this.$refs.showOrganizationMulSelectRef) {
        this.$nextTick().then(() => {
          this.$refs.showOrganizationMulSelectRef[0].init(this.orgId);
        });
      }
    },
    // 获取附件数据
    getFileList(data) {
      this.formFields.forEach((item) => {
        if (item.key === 'otherFiles') {
          // let that = this;
          let arr = data.map((item) => {
            let i = {
              name: item.response.data.fileName,
              filePath: item.response.data.relativePath,
              originalName: item.response.data.originalName,
              type: item.response.data.type,
              // id: that.detailId,
            };
            return i;
          });
          this.formData.otherFiles = arr;
        }
      });
    },
    inputData() {
      this.$forceUpdate();
    },
    initDefaultData() {
      this.formFields.forEach((item) => {
        if (item.key === 'player') {
          this.formData[item.key] = item.defaultData;
          this.inputData();
        }
      });
    },
  },
  watch: {
    // 添加当事人回填name
    identityName(newVal, oldVal) {
      if (newVal && newVal !== '') {
        this.formFields.forEach((item) => {
          if (item.key === 'name') {
            this.formData[item.key] = newVal;
            this.inputData();
          }
        });
      }
    },
    // 添加当事人回填身份
    identity(newVal, oldVal) {
      if (newVal && newVal !== '') {
        this.formFields.forEach((item) => {
          if (item.key === 'identity') {
            this.formData[item.key] = newVal;
            this.inputData();
          }
        });
      }
    },
    identityPhone(newVal, oldVal) {
      if (newVal && newVal !== '') {
        this.formFields.forEach((item) => {
          if (item.key === 'phone') {
            this.formData[item.key] = newVal;
            this.inputData();
          }
        });
      }
    },
    identityIdCardNo(newVal, oldVal) {
      if (newVal && newVal !== '') {
        this.formFields.forEach((item) => {
          if (item.key === 'idCardNo') {
            this.formData[item.key] = newVal;
            this.inputData();
          }
        });
      }
    },
    // 数据回显
    echoData(newVal) {
      this.formData = { ...newVal };
    },
  },
  mounted() {
    this.setInitValue();
    this.initDefaultData();
    if (this.moduleType === 'mediationDisputeType' || this.moduleType === 'mediationOrg') {
      this.initOrgData();
    }
    if (this.handleType === 1 || this.handleType === 2 || this.handleType === 3 || this.handleType === 10) {
      // 菜单类型
      sysDictTypeDropDown({ code: 'preliminaryLevel' }).then((res) => {
        this.preliminaryLevelList = res.data;
      });
    }
    this.$nextTick(() => {
      // 待办-调解情况-调解结果默认0:成功
      this.formFields.forEach((item) => {
        if (item.key === 'result' && item.radioType === 'mediationResult') {
          this.formData[item.key] = 0;
        }
        // 我的审核-审批状态
        if (item.key === 'approve' && item.selectType === 'checkStatus') {
          this.formData.approve = [0];
        }
      });
    });
    // 数据回显
    if (this.echoData) {
      this.formData = { ...this.echoData };
    }
  },
};
</script>
<style lang="less" scoped>
.is-require {
  :deep(.ant-form-item-label) {
    label::before {
      display: inline-block;
      margin-right: 4px;
      color: #f5222d;
      font-size: 14px;
      font-family: SimSun, sans-serif;
      line-height: 1;
      content: '*';
    }
  }
}
.form-item {
  position: relative;
}
.tips-icon {
  color: red;
  position: absolute;
  top: 0;
  left: -82px;
  z-index: 1;
}
</style>
