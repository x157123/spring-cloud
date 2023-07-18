package com.cloud.sync.storage;

import io.debezium.config.Configuration;
import io.debezium.relational.Tables;
import io.debezium.relational.ddl.DdlParser;
import io.debezium.relational.history.*;

import java.time.Instant;
import java.util.Map;

public class FileSchemaHistoryStore implements SchemaHistory {

    @Override
    public void configure(Configuration configuration, HistoryRecordComparator historyRecordComparator, SchemaHistoryListener schemaHistoryListener, boolean b) {

    }

    @Override
    public void start() {

    }

    @Override
    public void record(Map<String, ?> map, Map<String, ?> map1, String s, String s1) throws SchemaHistoryException {

    }

    @Override
    public void record(Map<String, ?> map, Map<String, ?> map1, String s, String s1, String s2, TableChanges tableChanges, Instant instant) throws SchemaHistoryException {

    }

    /**
     * @param map
     * @param tables
     * @param ddlParser
     * @deprecated
     */
    @Override
    public void recover(Map<Map<String, ?>, Map<String, ?>> map, Tables tables, DdlParser ddlParser) {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean storageExists() {
        return false;
    }

    @Override
    public void initializeStorage() {

    }
}
