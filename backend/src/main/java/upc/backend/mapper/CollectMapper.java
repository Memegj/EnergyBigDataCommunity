package upc.backend.mapper;
import upc.backend.entity.Collect;
import upc.backend.util.CollectQueryUtil;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface CollectMapper {
    int insert(Collect collect);

    int insertSelective(Collect collect);


    Collect selectByDataId(CollectQueryUtil collectUtil);
    Collect selectByCodeId(CollectQueryUtil collectUtil);
    Collect selectByVideoId(CollectQueryUtil collectUtil);
    Collect selectByLiterId(CollectQueryUtil collectUtil);
    List<Collect> selectByCollectType(String CollectType);

    int updateByPrimaryKeySelective(Collect collect);

    int updateByPrimaryKey(Collect collect);

    List<Collect> findAllCollectList(PageQueryUtil pageUtil);
    int getNumOfTotalCollect(PageQueryUtil pageUtil);
    int deleteBatchByDataId(Integer[] ids);
    int deleteBatchByCodeId(Integer[] ids);
    int deleteBatchByLiterId(Integer[] ids);
    int deleteBatchByVideoId(Integer[] ids);
    int deleteBatch(Integer[] ids);



}
