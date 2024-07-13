package upc.backend.mapper;
import org.apache.ibatis.annotations.Param;
import upc.backend.entity.Code;
import upc.backend.entity.Datasets;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface DatasetsMapper {
    int insert(Datasets datasets);

    int insertSelective(Datasets datasets);

    List<Datasets> selectByDataName(String DataName);
    List<Datasets> selectByisPublic(Integer isPublic);
    Datasets selectByDataId(Integer DataId);
    Datasets selectByDataIdAndUserId(Integer DataId);

    int updateByPrimaryKeySelective(Datasets datasets);

    int updateByPrimaryKey(Datasets datasets);
    List<Datasets> selectByTeamIds(@Param("pageUtil") PageQueryUtil pageUtil, @Param("teamIdsArray") Integer[] teamIdsArray);
    int getNumOfUserDatasets(Integer[] teamIdsArray);
    List<Datasets> findAllDatasetsList(PageQueryUtil pageUtil);
    List<Datasets> findAllDatasetsListByUserId(PageQueryUtil pageUtil);
    List<Datasets> findAllDatasetsListByUserIdOrderByTime(PageQueryUtil pageUtil);
    List<Datasets> findAllDatasetsListByUserIdOrderByTeamId(PageQueryUtil pageUtil);
    int getNumOfTotalDatasets(PageQueryUtil pageUtil);
    int getNumOfTotalDatasetsByUserId(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);

}
