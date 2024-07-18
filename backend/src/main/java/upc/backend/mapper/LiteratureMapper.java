package upc.backend.mapper;
import org.apache.ibatis.annotations.Param;
import upc.backend.entity.Literature;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface LiteratureMapper {
    int insert(Literature literature);

    int insertSelective(Literature literature);
    List<Literature> getliteraturesdata();
    List<Literature> selectByLiterName(String Literature);
    Literature selectByLiterId(Integer LiterId);
    Literature selectByLiterIdAndUserId(Integer LiterId);

    int updateByPrimaryKeySelective(Literature literature);

    int updateByPrimaryKey(Literature literature);
    List<Literature> selectByTeamIdsByType(@Param("pageUtil") PageQueryUtil pageUtil, @Param("teamIdsArray") Integer[] teamIdsArray);
    int getNumOfUserLiterature(Integer[] teamIdsArray);
    int getNumOfUserLiteratureByType(@Param("pageUtil") PageQueryUtil pageUtil, @Param("teamIdsArray") Integer[] teamIdsArray);
    List<Literature> findAllLiteratureList(PageQueryUtil pageUtil);
    List<Literature> findAllLiteratureListByUserId(PageQueryUtil pageUtil);
    List<Literature> findAllLiteratureListByUserIdOrderByTime(PageQueryUtil pageUtil);
    List<Literature> findAllLiteratureListByUserIdOrderByTeamId(PageQueryUtil pageUtil);
    int getNumOfTotalLiterature(PageQueryUtil pageUtil);
    int getNumOfTotalLiteratureByUserId(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);

}
