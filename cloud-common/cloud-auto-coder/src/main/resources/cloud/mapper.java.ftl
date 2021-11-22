package ${package}.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${package}.entity.${table.className};
import ${package}.query.${table.className}Query;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface ${table.className}Mapper extends BaseMapper<${table.className}> {

    /**
     * 分页查询
     *
     * @param page
     * @param ${table.className ? uncap_first}Query
     * @return
     */
    IPage<${table.className}> queryPage(IPage<${table.className}> page, @Param("param") ${table.className}Query ${table.className ? uncap_first}Query);

}
