<template>
  <!-- 纠纷处理详情弹窗 -->
  <a-modal :title="title" :width="1200" :visible="visible" :confirm-loading="confirmLoading" @cancel="handleEditCancel"
           destroyOnClose :footer="null" :maskClosable="false">
    <a-spin :spinning="loading">
      <div>
        <a-descriptions bordered :column="4" size="small">
          <h3 slot="title" class="title">基础信息</h3>
<#if column?? && (column?size > 0) >
<#list column as col>
              <#if col.nameClass != "createUser" && col.nameClass != "updateUser"
              && col.nameClass != "createDate" && col.nameClass != "updateDate"
              && col.nameClass != "isDelete" && col.nameClass != "isDeleted"
              && col.nameClass != "createBy" && col.nameClass != "updateBy"
              && col.nameClass != "version" && col.nameClass != "id"
              && col.nameClass != "createTime" && col.nameClass != "updateTime">
          <a-descriptions-item label="${col.webComment}" :span="2">
            {{ detailParams.${col.nameClass? uncap_first} }}
          </a-descriptions-item>
              </#if>
            </#list>
          </#if>
          <#if foreignKeys?? && (foreignKeys?size > 0) >
            <#list foreignKeys as foreignKey>
              <#if foreignKey.joinTableNameClass != nameClass>
          <a-descriptions-item label="${foreignKey.comment}列表" :span="5">
            <a-table :columns="${foreignKey.joinTableNameClass? uncap_first}Columns" :data-source="${foreignKey.joinTableNameClass? uncap_first}Data" size="small" :pagination="false" rowKey="id">
              <a slot="name" slot-scope="text">{{ text }}</a>
            </a-table>
          </a-descriptions-item>
              </#if>
            </#list>
          </#if>
          <#if mergeTables?? && (mergeTables?size > 0) >
            <#list mergeTables as mergeTable>
              <#if mergeTable.leftTable == mergeTable.maintain>
          <a-descriptions-item label="${mergeTable.mysqlTable.comment}列表" :span="5">
            <a-table :columns="${mergeTable.mysqlTable.nameClass? uncap_first}Columns" :data-source="${mergeTable.mysqlTable.nameClass? uncap_first}Data" size="small" :pagination="false" rowKey="id">
              <a slot="name" slot-scope="text">{{ text }}</a>
            </a-table>
          </a-descriptions-item>
              </#if>
            </#list>
          </#if>
          <a-descriptions-item label="附件信息" :span="3">
            <div v-if="detailParams.appealFileVoList && detailParams.appealFileVoList.length">
              <div v-for="(item, index) in detailParams.appealFileVoList" :key="index" style="margin: 8px">
                <img v-if="item.type === 'jpeg' ||
                  item.type === 'png' ||
                  item.type === 'jpg' ||
                  item.type === 'gif' ||
                  item.type === 'bmp' ||
                  item.type === 'svg'
                  " :src="item.filePath" style="width: 60px; height: 60px; object-fit: cover"
                     @click="previewImg(item.filePath)"/>
                <a v-else :href="item.filePath" target="_blank" :download="item.name">
                  {{ item.name }}
                </a>
              </div>
            </div>
          </a-descriptions-item>
        </a-descriptions>
      </div>
    </a-spin>
    <imgPreview v-if="showPreview" ref="previewRef"></imgPreview>
  </a-modal>
</template>

<script>
// 接口
import {
  ${nameClass? uncap_first}InfoFull,
} from '@/${web}/api${webExpandPackage}/${nameClass? uncap_first}/${nameClass? uncap_first}Api';
import imgPreview from '@/views/system/opinion/imgPreview.vue';

<#if foreignKeys?? && (foreignKeys?size > 0) >
<#list foreignKeys as foreignKey>
<#if foreignKey.joinTableNameClass != nameClass>
const ${foreignKey.joinTableNameClass? uncap_first}Columns = [
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
  },
  </#if>
  </#list>
];
</#if>
</#list>
</#if>
<#if mergeTables?? && (mergeTables?size > 0) >
<#list mergeTables as mergeTable>
<#if mergeTable.leftTable == mergeTable.maintain>
const ${mergeTable.mysqlTable.nameClass? uncap_first}Columns = [
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
  },
  </#if>
  </#list>
];
</#if>
</#list>
</#if>
export default {
  components: {
    imgPreview,
  },
  props: {
    // 弹窗是否显示
    visible: {
      type: Boolean,
      default: false,
    },
    // 弹窗确认按钮loading状态
    confirmLoading: {
      type: Boolean,
      default: false,
    },
    // 弹窗标题
    title: {
      type: String,
      default: '',
    },
    // detail info id/纠纷ID
    detailId: {
      type: String,
      default: '',
    },
    // 步骤id
    stepId: {
      type: String,
      default: '',
    },
    // 列表数据
    listdata: {
      type: Object,
      default: () => {
      },
    },
  },
  data() {
    return {
      <#if foreignKeys?? && (foreignKeys?size > 0) >
      <#list foreignKeys as foreignKey>
      <#if foreignKey.joinTableNameClass != nameClass>
      // ${foreignKey.comment}
      ${foreignKey.joinTableNameClass? uncap_first}Columns,
      ${foreignKey.joinTableNameClass? uncap_first}Data: [],
      </#if>
      </#list>
      </#if>
      <#if mergeTables?? && (mergeTables?size > 0) >
      <#list mergeTables as mergeTable>
      <#if mergeTable.leftTable == mergeTable.maintain>
      // ${mergeTable.mysqlTable.comment}
      ${mergeTable.mysqlTable.nameClass? uncap_first}Columns,
      ${mergeTable.mysqlTable.nameClass? uncap_first}Data: [],
      </#if>
      </#list>
      </#if>
      operateType: '',
      detailParams: {}, // 展示信息数据
      showPreview: false, // 附件预览
      loading: false,
    };
  },
  methods: {
    init(type) {
      this.operateType = type;
    },
    // 编辑弹窗取消
    handleEditCancel(e) {
      this.$emit('editCancel', e);
    },
    // 获取展示信息
    getDetailInfo() {
      if (this.detailId) {
        this.loading = true;
        ${nameClass? uncap_first}InfoFull(this.detailId)
          .then((res) => {
            if (res.success) {
              this.detailParams = res.data;
              <#if foreignKeys?? && (foreignKeys?size > 0) >
              <#list foreignKeys as foreignKey>
              <#if foreignKey.joinTableNameClass != nameClass>
              // ${foreignKey.comment}
              this.${foreignKey.joinTableNameClass? uncap_first}Data = res.data.${foreignKey.joinTableNameClass? uncap_first}VoList;
              </#if>
              </#list>
              </#if>
              <#if mergeTables?? && (mergeTables?size > 0) >
              <#list mergeTables as mergeTable>
              <#if mergeTable.leftTable == mergeTable.maintain>
              // ${mergeTable.mysqlTable.comment}
              this.${mergeTable.mysqlTable.nameClass? uncap_first}Data = res.data.${mergeTable.mysqlTable.nameClass? uncap_first}VoList;
              </#if>
              </#list>
              </#if>
            } else {
              this.$message.error(res.msg);
            }
          })
          .finally(() => {
            this.loading = false;
          });
      }
    },
    previewImg(path) {
      this.showPreview = true;
      this.$nextTick().then(() => {
        this.$refs.previewRef.init(path);
      });
    },
    handleCancel() {
      this.detailParams = {};
      <#if foreignKeys?? && (foreignKeys?size > 0) >
      <#list foreignKeys as foreignKey>
      <#if foreignKey.joinTableNameClass != nameClass>
      // ${foreignKey.comment}
      this.${foreignKey.joinTableNameClass? uncap_first}Data = [];
      </#if>
      </#list>
      </#if>
      <#if mergeTables?? && (mergeTables?size > 0) >
      <#list mergeTables as mergeTable>
      <#if mergeTable.leftTable == mergeTable.maintain>
      // ${mergeTable.mysqlTable.comment}
      this.${mergeTable.mysqlTable.nameClass? uncap_first}Data = [];
      </#if>
      </#list>
      </#if>
    },
  },
  mounted() {
    this.getDetailInfo();
  },
};
</script>

<style lang="less" scoped>
:deep(.ant-descriptions-item-label) {
  white-space: nowrap;
}

.modal-box {
  height: 600px;
  overflow-y: scroll;
}

.btn-box {
  margin-left: 50px;

  .btn-margin {
    margin-right: 10px;
  }
}

.status-text {
  font-weight: bold;
  font-size: 20px;
  margin-right: 10px;
}

.title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  text-align: center;
}

.msg-title {
  font-size: 18px;
  margin-bottom: 10px;
  text-align: center;
}

:deep(.ant-descriptions) {
  padding-bottom: 20px;
}

.handle-btn {
  margin: 20px 0 30px;

  .log-btn {
    margin: 5px 10px;
  }
}

.margin-b-20 {
  margin-bottom: 20px;
}

.isActive {
  color: #ffffff !important;
  background-color: #4496f6;
}
</style>
