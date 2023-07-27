package com.cloud.sync.entity;

import com.cloud.common.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
    import java.util.List;

/**
* @author liulei
* 表映射
*/
@Data
@TableName("sync_serve")
public class Serve extends BaseEntity {

    /**
    * 名称
    */
    private String name;

    /**
    * 采集数据Id
    */
    private Long readConnectId;

    /**
    * 写入数据库Id
    */
    private Long writeConnectId;
}
