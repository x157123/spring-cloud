<template>
  <div class="propaganda">
    <div class="propagandaBtn">
      <Button
          type="primary"
          @click="addData">
        <span class="iconfont icon-icon_plus"> 新增</span>
      </Button>
      <Button
          @click="handleNewDelete"
          :disabled="selects.length>0 ? false:'disabled'">
        <span
            class="iconfont icon-icon_delete"
            @click="handleNewDelete"> 批量删除</span>
      </Button>
      <div>
        <Input
            style="width: 280px;"
            search
            v-model="formModelSearchParam.cname"
            @on-search="handlePageChange(1)"
            enter-button="查询"
            placeholder="请输入字段名称" />
      </div>
    </div>
    <div class="propagandaTable">
      <div class="listTable listTablehp" style="position:relative;min-height:300px;">
        <my-loading
                :isLoading="isLoading"
                :list="itemTableData"
                :isAllWin="true" />
        <tui-table
            :objHeight="objHeight"
            ref="table"
            :list="itemTableData"
            :column="tableColumns"
            count
            :total="total"
            :currentPage="page"
            :size="rows"
            :isLoading="isLoading"
            @on-page-size-change="size => handlePageChange(1, size)"
            @on-page-change="handlePageChange"
            @on-selection-change="handleSelectionChange" />
      </div>
    </div>
    <!-- 新增 -->
    <Drawer
        :title="itemTitle"
        v-model="addClosable"
        :mask-closable="false"
        @on-close="cancel"
        :styles="styles"
        width="720">
      <form${table.className}Edit
          ref="form${table.className}Edit"
          @closeModal="closeModal"
          @refreshList="handlePageChange" />
      <div class="demo-drawer-footer">
        <Button
            style="margin-right: 8px"
            @click="cancel">
          取消
        </Button>
        <Button
            type="primary"
            @click="submitPropagaForm"
            @refreshList="handlePageChange">
          确定
        </Button>
      </div>
    </Drawer>
  </div>
</template>
<script>
import form${table.className}Edit from './components/edit'
import { MyLoading } from '@commonSys/cpt'
// 当前页面使用的接口
import apis from './apis.js'

const screenW = window.screen.width
let taSz = 'default'
if (screenW < 1400) {
  taSz = 'small'
} else if (screenW < 1680) {
} else {
}
export default {
  name: 'Propaganda',
  components: {
    form${table.className}Edit,
    MyLoading,
  },
  data() {
    return {
      isLoading: true,
      objHeight: {
        height: 'calc(100vh - 334px)',
      },
      itemTitle: '新增修改',
      viewInfo: {},
      styles: {
        height: 'calc(100% - 55px)',
        overflow: 'auto',
        paddingBottom: '53px',
        position: 'static',
      },
      buttonSize: taSz,
      total: 0,
      rows: 12,
      formModelSearchParam: {
        cname: '',
      },
      indeterminate: false,
      checkAllGroup: [],
      addClosable: false,
      checkAll: false,
      msgClosable: false,
      itemTableData: [],
      selects: [],
      tableColumns: [
        {
          type: 'selection',
          width: 60,
        },
<#list table.column as col>
<#if col.nameClass != "Id" && col.nameClass != "CreateUser" && col.nameClass != "UpdateUser"&& col.nameClass != "CreateDate" && col.nameClass != "UpdateDate" && col.nameClass != "IsDeleted">
<#if col.type == 'varchar' && col.length gt 15>
        {
          title: '${col.comment}',
          key: '${col.nameClass? uncap_first}',
          ellipsis: true,
          tooltip: true,
          minWidth: 160,
          render: (h, { row }) => {
            let text = row.${col.nameClass? uncap_first}
            if (text.length > 15) {
              text = `${r"$"}{row.${col.nameClass? uncap_first}.substring(0, 15)}...`
            }
            return h('span', text)
          },
        },
<#elseif col.type == 'datetime'>
        {
          title: '${col.comment}',
          key: '${col.nameClass? uncap_first}',
          minWidth: 180,
        },
<#else>
        {
          title: '${col.comment}',
          key: '${col.nameClass? uncap_first}',
          ellipsis: true,
          tooltip: true,
          minWidth: 150,
        },
</#if>
</#if>
</#list>
        {
          title: '操作',
          key: 'option',
          zindex: 1000,
          minWidth: 160,
          render: (h, { row }) => {
            return h('div', [
              h('span', {
                style: {
                  color: '#3399FF',
                  cursor: 'pointer',
                },
                on: {
                  click: () => {
                    this.editData(row)
                  },
                },
              }, '修改'),
              h('span', {
                style: {
                  color: '#3399FF',
                  cursor: 'pointer',
                  margin: '0px 16px',
                },
                on: {
                  click: () => {
                    this.handleDel(row.id)
                  },
                },
              }, '删除'),
            ])
          },
        },
      ],
      page: 1,
    }
  },
  created() {
    this.conditions = this.formModelSearchParam
  },
  mounted() {
    this.handlePageChange()
  },
  methods: {
    handleEnterButton() {
      this.handlePageChange()
    },
    setPage(rows) {
      this.page = 1
      this.rows = rows
      this.handlePageChange(1, rows)
    },
    closeModal() {
      this.addClosable = false
    },
    handlePageChange(page = this.page, rows = this.rows) {
      this.checkAll = false
      this.selects = []
      this.itemTableData = []
      this.isLoading = true
      apis.getPage({ page: page, rows: rows }).then(res => {
        this.total = res.records
        this.page = res.page
        this.rows = res.pagesize
        this.itemTableData = res.rows
      }).catch(error => {
        this.$Message.error(error.message)
      })
      this.isLoading = false
    },
    handleSelectionChange(rows) {
      this.selects = rows
    },
    handleDel(id) {
      const ids = []
      ids.push(id)
      this.$Modal.confirm({
        title: '操作确认',
        content: '确定删除信息？',
        onOk: () => {
          apis.delById({ 'ids': ids }).then(res => {
            if (res) {
              this.$Message.success('操作成功')
              this.handlePageChange()
              this.selects = []
            }
          }).catch(error => {
            this.$Message.error(error.message)
          })
        },
      })
    },
    addData() {
      this.itemTitle = '新增字段信息'
      this.addClosable = true
      this.$refs.form${table.className}Edit.resetParam()
    },
    editData(row) {
      this.itemTitle = '编辑字段信息'
      this.addClosable = true
      this.$refs.form${table.className}Edit.getById(row.id)
    },
    handleSeeMsg(row) {
      this.msgClosable = true
      this.viewInfo = row
      this.propagandaArray.forEach(item => {
        if (item.uniqueCode === row.activityType) {
          this.viewInfo.typeVal = item.displayName
        }
      })
    },
    submitPropagaForm() {
      this.$nextTick(() => {
        this.$refs.form${table.className}Edit.ok()
      })
    },
    cancel() {
      this.addClosable = false
      this.$refs.form${table.className}Edit.cancel()
    },
    handleNewDelete() {
      let ids = []
      ids = this.selects.map(s => s.id)
      if (ids === null || ids.length < 0) {
        this.$Message.warning('请至少选择一条数据进行删除')
        return
      }
      this.$Modal.confirm({
        title: '操作确认',
        content: '确定删除选中字段信息？',
        onOk: () => {
          this.$store.dispatch('configure/formItem/itemDelete', { 'ids': ids })
            .then(rst => {
              if (rst) {
                this.$Message.success('操作成功')
                this.handlePageChange()
                this.selects = []
                this.checkAllGroup = []
                this.indeterminate = false
                this.checkAll = false
              }
            })
            .catch(error => {
              this.$Message.error(error.message)
            })
        },
      })
    },
  },
}
</script>
<style lang="scss">
.sayClass {
  display: grid;
  grid-template-columns: 70px auto;

  span {
    display: inline-block;
    line-height: 30px;
  }
}
</style>
<style scoped lang="scss">
.selectBorder {
  /deep/ .ivu-select-selection {
    border: 1px solid #dcdee2;
  }
}

.btnClor {
  /* background: #1492FF !important; */
  color: #1492ff;
  border: none;
}

.cardList {
  margin-top: 16px;
  min-height: calc(31vh);
  max-height: calc(31vh);
  overflow: auto;
}

@media screen and (min-width: 1680px) {
  .cardList {
    min-height: calc(46vh);
    max-height: calc(46vh);
  }
}

.cardListItem {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-gap: 24px 24px;
  grid-template-rows: auto auto;

  .cardListIteminfo {
    position: relative;
    border: 1px solid rgba(230, 230, 230, 1);
    border-radius: 4px;
    min-width: 200px;

    .titleName {
      padding: 16px 16px 0px 16px;

      .ivu-checkbox {
        font-size: $FONT-SIZE-14;
        color: rgba(153, 153, 153, 1);
      }
    }
  }
}

.eventpage {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

/deep/ .ivu-table-wrapper {
  width: 100% !important;
}

.demo-drawer-footer {
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: $COLOR-fff;
}

.propaganda {
  padding: 0px 24px 24px 24px;
  background: $COLOR-fff;

  .propagandaTools {
    display: flex;
    position: relative;
    justify-content: space-between;

    label {
      margin-right: 24px;
      display: flex;

      span.span {
        line-height: 30px;
        width: 60px;
      }
    }

    span {
      color: $COLOR-666;
      display: inline-block;
      margin-right: 12px;
    }
  }

  .propagandaBtn {
    padding-top: 20px;
    display: flex;
    align-items: center;

    Button {
      margin-right: 16px;
      // margin-top: 24px;
    }
  }

  .propagandaTable {
    margin-top: 12px;
  }
}

.nodeData /deep/ .ivu-table-tip table td {
  width: initial !important;
}

.cardDiv {
  padding: 0 24px;
  min-height: 16vh;
  display: flex;
  justify-content: space-between;
  position: relative;

  img {
    margin-top: 16px;
    width: 84px;
    height: 84px;
  }

  .cardTit {
    min-height: 70px;
  }

  .cardCon {
    width: calc(100% - 94px);
    padding-top: 16px;
  }

  .cardBottom {
    /* position: absolute;
            width:calc(100% - 130px); */
    width: 100%;
    bottom: 15px;
    padding-bottom: 10px;

    p {
      margin-bottom: 10px;
      /* word-break:break-all; */
      /* line-height:1; */
    }

    span.span {
      padding: 2px 5px;
      border-radius: 10px;
      border: 1px solid $COLOR-e6;
      color: $COLOR-666;
      font-size: $FONT-SIZE-12;
    }
  }
}

.typeNumber {
  display: flex;
  justify-content: flex-end;
  position: absolute;
  top: -30px;

  .icon-icon_unfinished {
    color: $COLOR-39f !important;
    font-size: 52px;

    .title {
      color: $COLOR-666;
      font-size: $FONT-SIZE-14;
    }
  }

  div:nth-child(1) {
    display: flex;
    // color:$COLOR-666;
    margin-right: 0;

    .blackCount {
      font-size: $FONT-SIZE-16 + 4px;
      color: black;
    }

    p:nth-child(1) {
      margin-top: 16px;
    }
  }

  .eventTypeCount {
    margin-right: 70px;

    p:nth-child(1) {
      margin-top: 16px;
      position: relative;
    }

    p:nth-child(1)::after {
      position: absolute;
      display: inline-block;
      content: '';
      width: 6px;
      height: 6px;
      border-radius: 3px;
      background: $COLOR-39f;
      top: 7px;
      left: -13px;
    }

    p:nth-child(2) {
      span {
        font-size: $FONT-SIZE-16 + 4px;
        color: black;
      }
    }
  }
}

.eventtools {
  padding: 0px 0px;
  display: grid;
  grid-template-columns: 200px 260px 260px 200px;
  grid-gap: 0px 24px;
  padding-top: 25px;
  background: $COLOR-fff;

  span {
    color: $COLOR-666;
  }
}

.eventtools2 {
  display: grid;
  grid-template-columns: 280px 80px;
  grid-gap: 0px 24px;
  margin-top: 24px;
}

.desItmeTxt {
  display: flex;
  width: 199%;
  margin-top: 8px;
  justify-content: flex-end;
}

.desItem {
  color: $COLOR-ccc !important;
}
</style>
