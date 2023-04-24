package ${javaPath}.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${javaPath}.entity.${nameClass};
import ${javaPath}.query.${nameClass}Query;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface ${nameClass}Mapper extends BaseMapper<${nameClass}> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param ${nameClass ? uncap_first}Query 查询条件
     * @return  分页数据
     */
    IPage<${nameClass}> queryPage(IPage<${nameClass}> page, @Param("param") ${nameClass}Query ${nameClass ? uncap_first}Query);

}
