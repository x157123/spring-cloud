package com.cloud.sync.param;

import lombok.Data;

@Data
public class AssociateColumn {

    private String readerColumn;

    private String writerTable;
}