import { axios } from '@/utils/request';
let baseUrl = '/${config.projectName}';

/**
 *
 * 根据id查询${comment}信息
 */
export function ${nameClass? uncap_first}InfoFull(id) {
  return axios({
    url: baseUrl + `/${nameClass? uncap_first}/findById/<#noparse>${id}</#noparse>`,
    method: 'get',
  });
}

/**
 * ${comment}新增
 */
export function ${nameClass? uncap_first}InfoAdd(data) {
  return axios({
    url: baseUrl + `/${nameClass? uncap_first}/save`,
    method: 'post',
    data,
  });
}

/***
 * ${comment}修改
 */
export function ${nameClass? uncap_first}InfoUpdate(parameter) {
  return axios({
    url: baseUrl + '/${nameClass? uncap_first}/save',
    method: 'post',
    data: parameter,
  });
}

/**
 * ${comment}列表
 */
export function ${nameClass? uncap_first}DisputePage(parameter) {
  return axios({
    url: baseUrl + '/${nameClass? uncap_first}/queryPage',
    method: 'post',
    params: parameter,
  });
}

/**
 * ${comment}删除
 */
export function ${nameClass? uncap_first}InfoDelete(id) {
  return axios({
    url: baseUrl + `/${nameClass? uncap_first}/delete/<#noparse>${id}</#noparse>`,
    method: 'delete',
  });
}
