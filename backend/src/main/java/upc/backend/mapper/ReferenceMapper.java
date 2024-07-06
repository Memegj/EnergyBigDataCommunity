package upc.backend.mapper;
import upc.backend.entity.Reference;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface ReferenceMapper {
    int insert(Reference reference);

    int insertSelective(Reference reference);

    List<Reference> selectByTitle(String title);
    List<Reference> selectByAuthor(String author);
    Reference selectByID(Integer id);

    int updateByPrimaryKeySelective(Reference reference);

    int updateByPrimaryKey(Reference reference);

    List<Reference> findAllReferenceList(PageQueryUtil pageUtil);
    int getNumOfTotalReferences(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);

}
