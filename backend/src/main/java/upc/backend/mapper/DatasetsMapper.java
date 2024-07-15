package upc.backend.mapper;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface DatasetsMapper {
    int insert(Datasets datasets);

    int insertSelective(Datasets datasets);

    List<Datasets> selectByDataName(String DataName);
    List<Datasets> selectByisPublic(Integer isPublic);
    Datasets selectByDataId(Integer DataId);

    int updateByPrimaryKeySelective(Datasets datasets);

    int updateByPrimaryKey(Datasets datasets);

    List<Datasets> findAllDatasetsList(PageQueryUtil pageUtil);
    int getNumOfTotalDatasets(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);

}
