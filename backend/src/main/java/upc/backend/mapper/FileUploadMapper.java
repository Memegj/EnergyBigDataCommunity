package upc.backend.mapper;

import upc.backend.entity.Reference;
import upc.backend.entity.UploadFile;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface FileUploadMapper {
    int insert(UploadFile uploadFile);

    int insertSelective(UploadFile uploadFile);

    List<UploadFile> selectByTitle(String filename);
    List<UploadFile> selectByAuthor(String upload_user);
    UploadFile selectByID(Integer id);

    int updateByPrimaryKeySelective(UploadFile uploadFile);

    int updateByPrimaryKey(UploadFile uploadFile);

    List<UploadFile> findAllFilesList(PageQueryUtil pageUtil);
    int getNumOfTotalFiles(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);
}
