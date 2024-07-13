package upc.backend.mapper;

import org.apache.ibatis.annotations.Param;
import upc.backend.entity.Code;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import upc.backend.entity.Datasets;
import upc.backend.util.PageQueryUtil;

import java.util.List;

@Mapper
public interface CodeMapper {

    Code selectByCodeId(Integer codeId);
    List<Code> selectByTeamIds(@Param("pageUtil") PageQueryUtil pageUtil, @Param("teamIdsArray") Integer[] teamIdsArray);
    List<Code> findAllCodeListByUserIdOrderByTime(PageQueryUtil pageUtil);
    int getNumOfTotalCodeByUserId(PageQueryUtil pageUtil);
    List<Code> findAllCodeListByUserIdOrderByTeamId(PageQueryUtil pageUtil);
    int getNumOfTotalCode(PageQueryUtil pageUtil);
    int getNumOfUserCode(Integer[] teamIdsArray);

    List<Code> findAllCodeList(PageQueryUtil pageUtil);

    int deleteBatch(Integer[] ids);



}
