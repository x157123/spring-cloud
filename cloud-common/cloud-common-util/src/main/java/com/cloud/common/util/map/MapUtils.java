package com.cloud.common.util.map;

import java.text.DecimalFormat;
import java.util.*;

public class MapUtils {

    /**
     * 将Map 大写key转换为小写key
     *
     * @param orgMap
     * @return
     */
    public static Map<String, Object> transformLowerCase(Map<String, Object> orgMap) {
        Map<String, Object> resultMap = new HashMap<>();
        if (orgMap == null || orgMap.isEmpty()) {
            return resultMap;
        }
        Set<String> keySet = orgMap.keySet();
        for (String key : keySet) {
            String newKey = spliceName(key);
            resultMap.put(newKey, orgMap.get(key));
        }
        return resultMap;
    }

    /**
     * 将mapKey转换为驼峰命名
     *
     * @param orgMaps
     * @return
     */
    public static List<Map<String, Object>> transformLowerCase(List<Map<String, Object>> orgMaps) {
        List<Map<String, Object>> newMap = new ArrayList<>();
        for (Map<String, Object> map : orgMaps) {
            newMap.add(transformLowerCase(map));
        }
        return newMap;
    }

    /**
     * 合计 将data中的数据合并到result中
     *
     * @param result
     * @param data
     * @param dataValueKey
     * @return
     */
    public static void total(List<Map<String, Object>> result, List<Map<String, Object>> data, String join, String... dataValueKey) {
        Map<String, Map<String, Object>> resultMap = new HashMap<>();
        for (Map<String, Object> map : result) {
            resultMap.put(map.get(join).toString(), map);
        }
        for (Map<String, Object> map : data) {
            Map<String, Object> res = resultMap.get(map.get(join).toString());
            if (res != null) {
                for (String key : dataValueKey) {
                    res.put(key, getSum(res, map, key));
                }
            }
        }
    }

    /**
     * 合计  将data中的数据合并到result中
     *
     * @param result
     * @param data
     * @param dataValueKey
     * @return
     */
    public static void total(List<Map<String, Object>> result, String resultValueKey, String join, List<Map<String, Object>> data, String dataValueKey) {
        total(result, join, resultValueKey, data, join, dataValueKey);
    }

    /**
     * 合计  将data中的数据合并到result中
     *
     * @param result
     * @param data
     * @param dataValueKey
     * @return
     */
    public static void total(List<Map<String, Object>> result, String resultJoinKey, String resultValueKey, List<Map<String, Object>> data, String dataJoinKey, String dataValueKey) {
        Map<String, Map<String, Object>> resultMap = new HashMap<>();
        for (Map<String, Object> map : result) {
            resultMap.put(map.get(resultJoinKey).toString(), map);
        }
        if(data!=null && data.size()>0) {
            for (Map<String, Object> map : data) {
                Map<String, Object> res = resultMap.get(map.get(dataJoinKey).toString());
                if (res != null) {
                    res.put(resultValueKey, getLong(res, resultValueKey) + getLong(map, dataValueKey));
                }
            }
        }
    }

    /**
     * 合计  将data中的数据合并到result中
     * 根据 类型动态表头合计
     *
     * @param result     结果
     * @param join       关联字段 一般orgCode
     * @param data       数据库查询结果集
     * @param compareKey 类型
     * @param prefix     前缀
     * @param keys       合计列
     */
    public static void total(List<Map<String, Object>> result, String join, List<Map<String, Object>> data, String compareKey,String prefix, List<String> keys) {
        Map<String, Map<String, Object>> resultMap = new HashMap<>(result.size());
        for (Map<String, Object> map : result) {
            resultMap.put(map.get(join).toString(), map);
        }
        for (Map<String, Object> map : data) {
            Map<String, Object> res = resultMap.get(map.get(join).toString());
            if (res != null) {
                String key = getKey(map, compareKey);
                if(prefix!=null){
                    key = prefix + key;
                }
                res.put(key, getLong(res, key) + getLong(map, keys));
            }
        }
    }


    /**
     * 重置name
     *
     * @param result
     * @param befKey
     * @param aftKey
     */
    public static void rename(List<Map<String, Object>> result, String befKey, String aftKey) {
        for (Map<String, Object> map : result) {
            map.put(aftKey, map.get(befKey));
            map.remove(befKey);
        }
    }

    /**
     * 删除部分属性
     *
     * @param result
     * @param removeKey
     */
    public static void remove(List<Map<String, Object>> result, String... removeKey) {
        for (Map<String, Object> map : result) {
            for (String key : removeKey) {
                map.remove(key);
            }
        }
    }

    /**
     * 设置比例
     *
     * @param results
     * @param writeKey
     * @param dividendKey
     * @param list
     */
    public static void setProportion(List<Map<String, Object>> results, String writeKey, String dividendKey, String... list) {
        for (Map<String, Object> map : results) {
            Long count = 0L;
            for (String key : list) {
                count += getLong(map, key);
            }
            Long dividend = getLong(map, dividendKey);
            map.put(writeKey, getProportion(dividend, count));
        }
    }

    /**
     * 计算比例
     *
     * @param divisor
     * @param dividend
     * @return
     */
    public static String getProportion(Long divisor, Long dividend) {
        if (dividend <= 0) {
            return "_";
        } else if (divisor <= 0 && dividend > 0) {
            return "0%";
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("##0.##");
            return decimalFormat.format(100.0 * divisor / dividend) + "%";
        }
    }

    /**
     * 合计 将totalKey列数据进行合计
     *
     * @param result
     * @param totalKey
     * @return
     */
    public static Long total(List<Map<String, Object>> result, String totalKey) {
        Long total = 0L;
        for (Map<String, Object> map : result) {
            total += getLong(map, totalKey);
        }
        return total;
    }

    /**
     * 合计  将多列数据合计到一个属性中
     *
     * @param results
     * @param writeKey
     * @param list
     */
    public static void setTotal(List<Map<String, Object>> results, String writeKey, String... list) {
        for (Map<String, Object> map : results) {
            Long count = 0L;
            for (String key : list) {
                count += getLong(map, key);
            }
            map.put(writeKey, count);
        }
    }

    /**
     * 为列表添加默认值
     *
     * @param results
     * @param data
     * @param keys
     */
    public static void setDefaults(List<Map<String, Object>> results, Object data, List<String> keys) {
        for (Map<String, Object> result : results) {
            for (String key : keys) {
                result.put(key, data);
            }
        }
    }

    /**
     * 拆分列表
     *
     * @param mapList
     * @param key
     * @param splitStr
     * @param mapKeys
     * @return
     */
    public static List<Map<String, Object>> splitKeyAndNewList(List<Map<String, Object>> mapList, String key, String splitStr, String... mapKeys) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            Object obj = map.get(key);
            if (obj != null) {
                String[] sirs = obj.toString().split(splitStr);
                for (String str : sirs) {
                    if (str == null || str == "") {
                        continue;
                    }
                    Map<String, Object> newMap = new HashMap<>();
                    newMap.put(key, str);
                    for (String mapKey : mapKeys) {
                        newMap.put(mapKey, map.get(mapKey));
                    }
                    list.add(newMap);
                }
            }
        }
        return list;
    }

    /**
     * 将多个map合并
     *
     * @param maps
     * @return
     */
    public static Map<String, Object> mergeMap(Map<String, Object>... maps) {
        Map<String, Object> newMap = new HashMap<>();
        Set<String> keys = new HashSet<>();
        for (Map<String, Object> map : maps) {
            keys.addAll(map.keySet());
        }
        for (String key : keys) {
            newMap.put(key, 0);
        }
        for (Map<String, Object> map : maps) {
            for (String key : keys) {
                newMap.put(key, getLong(map, key) + getLong(newMap, key));
            }
        }
        return newMap;
    }

    /**
     * 将两列map值合计
     *
     * @param bef
     * @param aft
     * @param key
     * @return
     */
    private static Long getSum(Map<String, Object> bef, Map<String, Object> aft, String key) {
        return getLong(bef, key) + getLong(aft, key);
    }

    /**
     * 获取map中的统计数字
     *
     * @param map
     * @param key
     * @return
     */
    public static Long getLong(Map<String, Object> map, String key) {
        return getLong(map, Arrays.asList(key));
    }

    /**
     * 获取map中的统计数字
     *
     * @param map
     * @param keys
     * @return
     */
    public static Long getLong(Map<String, Object> map, List<String> keys) {
        Long count = 0L;
        try {
            if (keys != null && keys.size() > 0) {
                for (String key : keys) {
                    if (map.get(key) != null) {
                        count += Long.parseLong(map.get(key).toString());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("错误map：" + map);
            System.out.println("错误key：" + keys);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 修改为驼峰命令 手字符小写
     *
     * @param name
     * @return
     */
    private static String spliceName(final String name) {
        String[] names = name.split("_");
        String newName = "";
        for (String str : names) {
            str = str.trim().toLowerCase();
            newName += str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        if (Character.isLowerCase(newName.charAt(0))) {
            return newName;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(newName.charAt(0))).append(newName.substring(1)).toString();
        }
    }

    /**
     * 将key转换为String 并已Key开头
     *
     * @param obj
     * @return
     */
    public static String getKey(Object obj) {
        if (obj != null) {
            return "key_" + obj.toString();
        }
        return "kye_";
    }

    /**
     * 将key转换为String 并已Key开头
     *
     * @param map
     * @param key
     * @return
     */
    private static String getKey(Map<String, Object> map, String key) {
        try {
            if (map.get(key) != null) {
                return getKey(map.get(key).toString());
            }
        } catch (Exception e) {
            System.out.println("错误map：" + map);
            System.out.println("错误key：" + key);
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 添加合计行
     *
     * @param list
     * @param name
     * @param keys
     */
    public static void addTotalRow(List<Map<String, Object>> list, String name, List<String> keys) {
        Map<String, Object> total = new HashMap<>();
        total.put(name, "合计");
        for(String key : keys){
            total.put(key, 0);
        }
        for (Map<String, Object> map : list) {
            for(String key : keys){
                total.put(key,getLong(total,key) + getLong(map,key));
            }
        }
        list.add(total);
    }
}
