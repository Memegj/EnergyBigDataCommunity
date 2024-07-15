package upc.backend.mapper;
import upc.backend.entity.Datasets;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface DatasetsteamJointMapper {
    int insert(Datasets datasets);

    int insertSelective(Datasets datasets);

    List<Datasets> selectByDataName(String DataName);
    List<Datasets> selectByisPublic(Integer isPublic);
    Datasets selectByDataId(Integer DataId);

    int updateByPrimaryKeySelective(Datasets datasets);

    int updateByPrimaryKey(Datasets datasets);

    List<Datasets> findAllDatasetsList(PageQueryUtil pageUtil);
    List<Datasets> findAllDatasets_teamListByUserId(PageQueryUtil pageUtil);
    int getNumOfTotalDatasets(PageQueryUtil pageUtil);
    int getNumOfTotalDatasets_teamByUserId(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);

}
