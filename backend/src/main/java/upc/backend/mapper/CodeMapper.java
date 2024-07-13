package upc.backend.mapper;

import upc.backend.entity.Code;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import upc.backend.entity.Datasets;
import upc.backend.util.PageQueryUtil;

import java.util.List;

@Mapper
public interface CodeMapper {

    Code selectByCodeId(Integer codeId);

    int getNumOfTotalCode(PageQueryUtil pageUtil);

    List<Code> findAllCodeList(PageQueryUtil pageUtil);

    int deleteBatch(Integer[] ids);



}
