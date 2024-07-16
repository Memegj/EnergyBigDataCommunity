package upc.backend.mapper;

import org.apache.ibatis.annotations.Param;
import upc.backend.entity.Collect;
import upc.backend.util.PageQueryUtil;

import java.util.List;
import java.util.Map;

public interface CollectMapper {
    void deleteCollectByVideoId(Map<String, Object> params);
}
