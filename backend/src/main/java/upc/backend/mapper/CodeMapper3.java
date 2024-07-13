package upc.backend.mapper;
import upc.backend.entity.Code;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface CodeMapper3 {
    int insert(Code code);

    int insertSelective(Code code);
    int insertCode(Code code);

    List<Code> selectByTitle(String codename);

    Code selectByID(Integer codeid);

    int updateByPrimaryKeySelective(Code code);

    int updateByPrimaryKey(Code code);
    List<Code> findAllReferenceList(PageQueryUtil pageUtil);
    int getNumOfTotalReferences(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);






}
