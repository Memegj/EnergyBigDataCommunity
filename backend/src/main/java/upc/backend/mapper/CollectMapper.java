package upc.backend.mapper;

import org.apache.ibatis.annotations.Param;
import upc.backend.entity.Team;
import upc.backend.util.PageQueryUtil;
import java.util.List;
import java.util.Map;
import upc.backend.entity.Collect;
import upc.backend.util.CollectQueryUtil;

public interface CollectMapper {
    int insert(Collect collect);
    List<String> getcollectTypes();

    List<Integer> gettypesCounts();
    int insertSelective(Collect collect);

    List<Collect> getCollectionByUserId(PageQueryUtil pageUtil);
    int getNumOfTotalCollect(PageQueryUtil pageUtil);
    void deleteCollectById(Map<String, Object> params);

    Collect selectByDataId(CollectQueryUtil collectUtil);
    Collect selectByCodeId(CollectQueryUtil collectUtil);
    Collect selectByVideoId(CollectQueryUtil collectUtil);
    Collect selectByLiterId(CollectQueryUtil collectUtil);
    List<Collect> selectByCollectType(String CollectType);

    int updateByPrimaryKeySelective(Collect collect);

    int updateByPrimaryKey(Collect collect);

    List<Collect> findAllCollectList(PageQueryUtil pageUtil);
    int deleteBatchByDataId(Integer[] ids);
    int deleteBatchByCodeId(Integer[] ids);
    int deleteBatchByLiterId(Integer[] ids);
    int deleteBatchByVideoId(Integer[] ids);
    int deleteBatch(Integer[] ids);

    void deleteCollectByVideoId(Map<String, Object> params);




}
