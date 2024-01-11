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
          <a-descriptions-item label="当事人列表" :span="5">
            <a-table :columns="columns" :data-source="personData" size="small" :pagination="false" rowKey="id">
              <a slot="name" slot-scope="text">{{ text }}</a>
            </a-table>
          </a-descriptions-item>
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
    <a-divider/>
    <!-- 按钮列表 -->
    <div class="btn-box">
      <div>
        <a-button v-if="operateType == 'process'" :key="10" type="primary"
                  class="btn-margin" @click="handleClickBtn(10)">
          转纠纷化解
        </a-button>
        <a-button v-if="operateType == 'process'" :key="12" type="primary"
                  class="btn-margin" @click="handleClickBtn(12)">
          办结
        </a-button>
      </div>
    </div>
  </a-modal>
</template>

<script>
// 接口
import {
  appealInfoFull,
} from '@/api/modular/${nameClass? uncap_first}/${nameClass? uncap_first}Api';
import imgPreview from '@/views/dazhou/issue/all/imgPreview.vue';

const columns = [
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '联系方式',
    dataIndex: 'phone',
    key: 'phone',
  },
  {
    title: '身份证号码',
    dataIndex: 'idCardNo',
    key: 'idCardNo',
  },
  {
    title: '地址',
    dataIndex: 'address',
    key: 'address',
    ellipsis: true,
  },
];
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
      columns,
      operateType: '',
      partyNames: '',
      detailParams: {}, // 展示信息数据
      showPreview: false, // 附件预览
      loading: false,
      // 当事人列表
      personData: [],
    };
  },
  methods: {
    init(type) {
      this.operateType = type;
    },
    refrensh() {
      this.handleEditCancel();
    },
    // 编辑弹窗取消
    handleEditCancel(e) {
      this.$emit('editCancel', e);
    },
    // 获取展示信息
    getDetailInfo() {
      if (this.detailId) {
        this.loading = true;
        appealInfoFull(this.detailId)
          .then((res) => {
            if (res.success) {
              this.detailParams = res.data;
              // 当事人列表
              this.personData = res.data.appealPersonVoList;
              if (this.personData && this.personData.length > 0) {
                this.personData.forEach(item => {
                  if (item && item !== null) {
                    this.partyNames += item.name + ',';
                  }
                });
              }
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
    // 点击按钮展示对应操作弹框
    handleClickBtn(key) {
    },
    handleCancel() {
      this.detailParams = {};
      // 当事人列表
      this.data = [];
      this.partyNames = '';
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
