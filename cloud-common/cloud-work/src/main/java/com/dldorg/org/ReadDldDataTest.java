package com.dldorg.org;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.MySQLDb;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
public class ReadDldDataTest {

    public static void main(String[] args) throws FileNotFoundException {
        List<Org> list = getDbOrg();
        Map<Long, Org> orgMap = new HashMap<>();
        for (Org org : list) {
            orgMap.put(org.getId(), org);
        }
        Map<Long, List<Org>> orgMaps = list.stream().collect(Collectors.groupingBy(Org::getParentId));

        lock(orgMaps, orgMap.get(1L));

    }


    private static void lock(Map<Long, List<Org>> orgMaps, Org org) {
        List<Org> list = orgMaps.get(org.getId());
        Long max = org.getDeptMax();
        Long tmpMzxx = 1L;
        if (list != null && list.size() > 0) {
            for (Org tmp : list) {
                String orgNew = tmp.getOrgCode().replace(org.getOrgCode(), "");
                orgNew = orgNew.replace(".", "");
                Long tmpMzx = Long.parseLong(orgNew);
                tmpMzxx = tmpMzx > tmpMzxx ? tmpMzx : tmpMzxx;
            }
            if (max < tmpMzxx) {
                System.out.println("update doraemon_system.uc_sys_organization set max_code  = "+(tmpMzxx)+" where id = "+org.getId()+" and max_code  = " + max + ";--" + org.getOrgFullName() );
            }
            for (Org tmp : list) {
                lock(orgMaps, tmp);
            }
        }
    }


    /**
     * 获取数据库组织机构信息
     */
    public static List<Org> getDbOrg() {
        Long start = 0L;
        Long page = 2000L;
        List<Org> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("192.168.10.95", "3306", "doraemon_system", "doraemon_system", "sichuan", "Tianquekeji@123");
//        DbComponent dbComponent = MySQLDb.createDb("192.168.10.113", "3306", "doraemon_system_v5", "doraemon_system_v5", "root", "tianquekeji");
        try {
            String queryTable = "SELECT id,parent_id ,org_name,seq,max_code as dept_max,org_internal_code as org_code,org_full_name FROM `doraemon_system`.uc_sys_organization where is_deleted =0 and org_internal_code like '%s' LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<Org> list = dbComponent.readJdbcData(String.format(queryTable, ".%", start), Org::new, (b, t) -> b.setSg(Boolean.TRUE));
                all.addAll(list);
                start += page;
                if (list.size() < page) {
                    bool = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbComponent.close();
        return all;
    }
}
