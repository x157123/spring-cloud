package com.dict;


import java.util.HashMap;
import java.util.Map;

public class DictTest {
    public static void main(String[] args) {
        String dld = "01,精神分裂症;02,分裂情感性障碍;03,持久的妄想性障碍（偏执性精神病）;04,双相（情感）障碍;05,癫痫所致精神障碍;06,精神发育迟滞伴发精神障碍;07,重度抑郁发作;08,精神活性物质所致精神障碍;99,其他";
        String sg = "90590001,精神分裂症;90590002,分裂情感性障碍;90590003,持久的妄想性障碍(偏执性精神病）;90590004,双相（情感）障碍;90590005,癫痫所致精神障碍;90590006,精神发育迟滞伴发精神障碍;90590007,重度抑郁发作;90590008,精神活性物质所致精神障碍;90590009,其他";
        String[] sgs = sg.split(";");
        String[] dlds = dld.split(";");
        Map<String,String> sgMap = new HashMap<>();
        Map<String,String> dldMap = new HashMap<>();

        for(String st:dlds){
            String[] s = st.split(",");
            dldMap.put(s[1],s[0]);
        }

        System.out.println("private String set(String key){");
        System.out.println("if (key == null) {return \"\";}");
        System.out.println("        switch (key){");
        for(String st:sgs){
            String[] s = st.split(",");
            String key = dldMap.get(s[1]);
            if(key == null){
                key = "000";
            }
            System.out.println("case \""+ key +"\": return \""+s[0]+"\";         //"+s[1]);
//            System.out.println("case \""+ s[1] +"\": return \""+s[0]+"\";");
        }
        System.out.println("}");
        System.out.println("}");

    }
}
