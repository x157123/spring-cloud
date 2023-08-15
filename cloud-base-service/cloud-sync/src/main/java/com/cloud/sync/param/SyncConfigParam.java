package com.cloud.sync.param;

import lombok.Data;

import java.util.List;

@Data
public class SyncConfigParam {

    private Long serveId;

    private String name;

    private Long readerDb;

    private Long writerDb;

    private List<AssociateTableParam> associateTable;
}
