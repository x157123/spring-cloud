package com.cloud.auto.code.mysql;

import com.cloud.auto.code.util.StringUtil;
import lombok.Data;

import java.util.Iterator;
import java.util.Set;

@Data
public class Enums {
    private String key;
    private String value;
    private String name;

    public Enums() {
    }

    public Enums(String key, String value, String name) {
        this.key = key;
        this.value = value;
        this.name = name;
        Set<String> st = StringUtil.getAllPinyin(name, "_");
        Iterator<String> iterator = st.iterator();
        if (iterator.hasNext()) {
            String firstElement = iterator.next();
            this.name = firstElement;
        }
    }

}
