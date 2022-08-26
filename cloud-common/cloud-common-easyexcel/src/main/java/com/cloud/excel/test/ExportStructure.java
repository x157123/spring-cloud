package com.cloud.excel.test;

import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.util.db.bo.TableEntity;
import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.MySQLDb;
import com.cloud.excel.util.ExportUtil;

import java.util.List;

/**
 * @author liulei
 *
 * 导出表结构
 */
public class ExportStructure {

    public static void main(String[] args) {
        //

        getTableAll();

    }


    /**
     * 读取所有表结构
     */
    public static void getTableAll(){
        DbComponent dbComponent = MySQLDb.createDb("192.168.10.113", "3306", "sync-test", "sync-test", "root", "tianquekeji");
        List<TableEntity> tables = dbComponent.getTable("sync_gis_dlayers","sync_uc_sys_dict","sync_uc_sys_organization","sync_sg_company","sync_sg_cp_type_school","sync_sg_grid_extend_info","sync_sg_grid_responsible_building","sync_sg_grid_team_building_chief","sync_sg_grid_team_grid_member","sync_sg_grid_team_red_sleeve","sync_sg_new_org_economy","sync_sg_new_org_social","sync_sg_org_manage_center","sync_sg_org_person","sync_sg_addr_building","sync_sg_addr_info","sync_sg_house_pop_rel","sync_sg_house_rent_info","sync_sg_pop_addr_rel","sync_sg_pop_base_info","sync_sg_pop_business_rel","sync_sg_pop_drug_visit","sync_sg_pop_druggy","sync_sg_pop_float","sync_sg_pop_house_reg_info","sync_sg_pop_juveniles","sync_sg_pop_ment_visit","sync_sg_pop_mental_patient","sync_sg_pop_posi_visit","sync_sg_pop_positive","sync_sg_pop_rect","sync_sg_pop_rect_visit","sync_sg_pop_visit_base","sync_sg_com_pub_edu","sync_sg_et_check_issue","sync_sg_et_issue","sync_sg_security_case","sync_sg_security_case_person");
        tables.forEach(tableEntity -> dbComponent.setTableColumn(tableEntity));
        ExportUtil.exportListData(tables);
        tables.forEach(System.out::println);
    }

}
