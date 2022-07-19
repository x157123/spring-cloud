package com.test;

import java.util.Arrays;
import java.util.List;

/**
 * @author liulei
 */
public class CreateSql {
    public static void main(String[] args) {
        String strss = "jw_mental_metadata,sg_abutment_data,sg_base_file,sg_claim_data_refresh_log,sg_config_columnmeta,sg_config_confirm,sg_config_tablemeta,sg_dc_claim,sg_dc_log,sg_dc_step,sg_pop_addr_rel,sg_pop_base_info,sg_pop_business_rel,sg_pop_claim_reference_addr,sg_pop_druggy,sg_pop_mental_patient,sg_pop_positive,sg_pop_rect,sg_pop_remote_refresh_log,sg_pop_soldier,sg_trigonal_dict_mapping";
        String[] strs = strss.split(",");
        for(String str:strs){
            System.out.println("ALTER TABLE data_claim_test_v5."+str+" ALTER COLUMN is_deleted SET DEFAULT 0;");
            //System.out.println("ALTER TABLE common_biz_test_v5."+str+" ADD CONSTRAINT "+str+"_pk PRIMARY KEY (id);");
        }
    }
}
