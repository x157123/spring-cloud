import requstFactory from '@/lib/http/requstFactory'
const requstData = requstFactory()

export default {
<#list autoCodeConfig.tables as tab>
  //${tab.comment}列表
  ${tab.className? uncap_first}QueryPage: async (payload) => {
    return await requstData.post(
      '/${autoCodeConfig.serveName}/${tab.className? uncap_first}/queryPage',
      payload,
      false,
    )
  },
  //${tab.comment}详情
  ${tab.className? uncap_first}GetById: async (payload) => {
    return await requstData.post(
      '/${autoCodeConfig.serveName}/${tab.className? uncap_first}/findById',
      payload,
      false,
    )
  },
</#list>
}
