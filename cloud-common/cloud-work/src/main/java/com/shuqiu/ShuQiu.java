package com.shuqiu;

import com.cloud.common.util.db.connection.DbComponent;
import com.cloud.common.util.db.connection.MySQLDb;
import com.cloud.common.util.db.connection.PostgreSQLDb;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ShuQiu {

    static Random r = new Random();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        List<Orgs> orgs = getDborg();
        List<Datas> datas = getData();
        List<String> sql = new ArrayList<>();
        for(Datas ds : datas){
            Orgs org = orgs.get(r.nextInt(orgs.size()-1));
            String time = getData(ds.getCreateDate());
            String detail_address = ds.getDetailAddress();
            String detail_content = ds.getDetailContent();
            sql.add("UPDATE public.accept_appeal_info SET id="+ds.getId().substring(0,17)+",org_id="+org.getOrgId()+",org_code='"+org.getOrgCode()+"',current_target_org_id="+org.getOrgId()+",current_target_org_code='"+org.getOrgCode()+"',occur_date='"+time+"',appeal_number='"+ds.getId()+"', detail_address='"+getStr(ds.getDetailAddress())+"',detail_content='"+getStr(ds.getDetailContent())+"',create_date='"+time+"',lon='',lat='' WHERE id="+ds.getId()+";");
        }

        String fileName = "D:\\example.txt";

        try {
            FileWriter writer = new FileWriter(fileName);

            for (String line : sql) {
                writer.write(line + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStr(String str){
        String newStr ;
        newStr = str.replace("自贡市","甘孜藏族自治州");
        newStr = newStr.replace("自贡","甘孜藏族自治州");
        newStr = newStr.replace("自流井区","");
        newStr = newStr.replace("贡井区","");
        newStr = newStr.replace("大安区","");
        newStr = newStr.replace("沿滩区","");
        newStr = newStr.replace("荣县","");
        newStr = newStr.replace("富顺县","");
        newStr = newStr.replace("自贡高新区","");
        return newStr;
    }

    public static String getData(String data){
        Date dat = null;
        try {
            dat = sdf.parse(data);
            Long time = dat.getTime();
            if(time<= 1672502426000L){
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, 2020 + r.nextInt(3));
                calendar.set(Calendar.MONTH, r.nextInt(12));
                dat = calendar.getTime();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sdfs.format(dat);
    }

    public static List<DataFile> getDataFile(){
        Long start = 0L;
        Long page = 2000L;
        List<DataFile> all = new ArrayList<>();
        DbComponent dbComponent = PostgreSQLDb.createDb("192.168.10.67", "5432", "postgres", "postgres", "postgres", "Tianquekeji@123");
        try {
            String queryTable = "SELECT id, business_id FROM public.accept_appeal_file limit  2000  offset %s";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<DataFile> list = dbComponent.readJdbcData(String.format(queryTable, ".1.3.%", start), DataFile::new, null);
                all.addAll(list);
                start += page;
                if (list.size() < page) {
                    bool = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        String fileName = "D:\\exampleFile.txt";

        try {
            FileWriter writer = new FileWriter(fileName);

            for (DataFile dt : all) {
                String sql = "UPDATE public.accept_appeal_file SET id="+dt.getId().substring(0,17)+", business_id="+dt.getBusinessId().substring(0,17)+" where id="+ dt.getId().substring(0,17);
                writer.write(sql + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return all;
    }

    public static List<Datas> getData(){
        Long start = 0L;
        Long page = 2000L;
        List<Datas> all = new ArrayList<>();
        DbComponent dbComponent = PostgreSQLDb.createDb("192.168.10.67", "5432", "postgres", "postgres", "postgres", "Tianquekeji@123");
        try {
            String queryTable = "SELECT id,org_id,org_code,current_target_org_id,current_target_org_code,occur_date,appeal_number, detail_address,detail_content,create_date,update_date FROM public.accept_appeal_info where org_code like '%s' limit  2000  offset %s";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<Datas> list = dbComponent.readJdbcData(String.format(queryTable, ".1.3.%", start), Datas::new, null);
                all.addAll(list);
                start += page;
                if (list.size() < page) {
                    bool = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all;
    }


    public static List<Orgs> getDborg(){
        Long start = 0L;
        Long page = 2000L;
        List<Orgs> all = new ArrayList<>();
        DbComponent dbComponent = MySQLDb.createDb("192.168.10.95", "3306", "doraemon_system", "doraemon_system", "sichuan", "Tianquekeji@123");
//        DbComponent dbComponent = MySQLDb.createDb("192.168.10.113", "3306", "doraemon_system_v5", "doraemon_system_v5", "root", "tianquekeji");
        try {
            String queryTable = "SELECT id as org_id,parent_id ,org_internal_code as org_code,org_name, org_full_name,seq FROM `doraemon_system`.uc_sys_organization where org_level= '90170001' and org_type='90180001' and is_deleted =0 and org_internal_code like '%s' LIMIT %s ,2000";
            boolean bool = Boolean.TRUE;
            while (bool) {
                List<Orgs> list = dbComponent.readJdbcData(String.format(queryTable, ".1.20.%", start), Orgs::new, null);
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
