package com.cloud.sync.param;

import lombok.Data;

import java.util.List;

@Data
public class AssociateTable {

    private String readerTable;

    private Integer type;

    private String writerTable;

    private List<AssociateColumn> associateColumn;
}