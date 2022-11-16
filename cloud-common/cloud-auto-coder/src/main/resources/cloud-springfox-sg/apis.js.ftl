import requstFactory from '@/lib/http/requstFactory'
const requstData = requstFactory()

export default {
  //列表
  getPage: async (payload) => {
    return await requstData.post(
      '/${autoCodeConfig.serveName}/${table.className? uncap_first}/queryPage',
      payload,
      false,
    )
  },
  //详情
  getById: async (payload) => {
    return await requstData.get(
      '/${autoCodeConfig.serveName}/${table.className? uncap_first}/findById',
      payload,
      false,
    )
  },
  //新增修改
  addData: async (payload) => {
    return await requstData.post(
      '/${autoCodeConfig.serveName}/${table.className? uncap_first}/save',
      payload,
      false,
    )
  },
  //删除
  delById: async (payload) => {
    return await requstData.post(
      '/${autoCodeConfig.serveName}/${table.className? uncap_first}/removeByIds',
      payload,
      false,
    )
  },
}
