package com.cloud.excel.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liulei
 */
@Data
public class TitleVO {

    private String title;

    private String key;

    private List<TitleVO> children;

    private String align;

    /**
     * 是否允许点击 table-column-active-info
     */
    private String className;

    /**
     * 是否显示
     */
    private Boolean show;

    /**
     * 上级
     */
    private TitleVO fatherTitle;

    @Transient
    public Boolean isShow() {
        return this.show;
    }

    @Transient
    public TitleVO getFatherTitle() {
        return this.fatherTitle;
    }

    public static TitleVO createTitleVO(String title) {
        TitleVO titleVO = new TitleVO();
        titleVO.title = title;
        titleVO.setKey("o0xo0x1");
        titleVO.show = Boolean.TRUE;
        return titleVO;
    }

    private TitleVO() {
    }

    public TitleVO(String title, String key,boolean bool) {
        this.title = title;
        this.show = Boolean.TRUE;
        if(bool) {
            this.className = "table-column-active-info";
        }
        this.key = key;
    }

    public String getAlign() {
        if (this.align == null) {
            return "center";
        }
        return this.align;
    }

    public TitleVO append(TitleVO titles) {
        return append(null, titles);
    }

    public TitleVO append(String key, TitleVO titles) {
        List<TitleVO> tmp = new ArrayList<>();
        tmp.add(titles);
        return append(key, tmp);
    }


    public TitleVO append(List<TitleVO> titles) {
        return append(null, titles);
    }

    public TitleVO append(String key, List<TitleVO> titles) {
        if (key == null) {
            key = this.getKey();
        }
        return append(this, key, titles);
    }

    private TitleVO append(TitleVO title, String key, List<TitleVO> titles) {
        if (title.getKey().equals(key)) {
            for (TitleVO titleVO : titles) {
                titleVO.setFatherTitle(title);
            }
            if (title.getChildren() == null) {
                title.setChildren(titles);
            } else {
                title.getChildren().addAll(titles);
            }
            return title;
        } else {
            for (TitleVO tmp : title.getChildren()) {
                append(tmp, key, titles);
            }
        }
        return title;
    }

    @Transient
    public List<List<String>> getHead() {
        return this.getHead(null);
    }

    /**
     * @param
     * @return
     */
    @Transient
    public List<List<String>> getHead(List<String> exportList) {
        List<List<String>> list = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        if (exportList != null && exportList.size() > 0) {
            hideHead(this.getChildren());
            showHead(this.getChildren(), exportList);
        }
        titles.add(this.getTitle());
        getHead(list, this.getChildren(), titles);
        return list;
    }

    /**
     * 赛选列 并显示
     *
     * @param titles
     * @param exportList
     */
    private void showHead(List<TitleVO> titles, List<String> exportList) {
        for (TitleVO title : titles) {
            if (title.getChildren() == null || title.getChildren().size() <= 0) {
                if (exportList.contains(title.getTitle())) {
                    showFatherTitle(title);
                }
            } else {
                hideHead(title.getChildren());
            }
        }
    }

    /**
     * 将父级表头显示出来
     *
     * @param title
     */
    private void showFatherTitle(TitleVO title) {
        title.setShow(Boolean.TRUE);
        if (title.getFatherTitle() != null) {
            showFatherTitle(title.getFatherTitle());
        }
    }

    private void hideHead(List<TitleVO> titles) {
        for (TitleVO title : titles) {
            //先隐藏所有列
            title.setShow(Boolean.FALSE);
            if (title.getChildren() != null && title.getChildren().size() > 0) {
                hideHead(title.getChildren());
            }
        }
    }

    private void getHead(List<List<String>> list, List<TitleVO> titles, List<String> root) {
        for (TitleVO title : titles) {
            if (title.isShow()) {
                List<String> tmp = new ArrayList<>();
                if (root.size() > 0) {
                    tmp.addAll(root);
                }
                tmp.add(title.getTitle());
                if (title.getChildren() == null || title.getChildren().size() <= 0) {
                    list.add(tmp);
                } else {
                    getHead(list, title.getChildren(), tmp);
                }
            }
        }
    }

    /**
     * 获取表头最大层级
     *
     * @return
     */
    @Transient
    public Integer getHeadDepth() {
        Integer headDepth = 0;
        List<List<String>> list = getHead();
        for (List<String> head : list) {
            headDepth = headDepth < head.size() ? head.size() : headDepth;
        }
        return headDepth;
    }

    @Transient
    public List<String> getHeadKey() {
        List<String> list = new ArrayList<>();
        getHeadKey(list, this);
        return list;
    }

    private void getHeadKey(List<String> list, TitleVO title) {
        if (title.getChildren() == null || title.getChildren().size() <= 0) {
            if(title.isShow()) {
                list.add(title.getKey());
            }
        } else {
            for (TitleVO tmp : title.getChildren()) {
                getHeadKey(list, tmp);
            }
        }
    }


    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
