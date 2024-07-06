package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.UploadFile;
import upc.backend.mapper.FileUploadMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class FileUploadService {
    @Resource
    private FileUploadMapper fileUploadMapper;
    public UploadFile getFileById(Integer id){
        return fileUploadMapper.selectByID(id);
    }

    //更新文献信息
    public Boolean updateFileInfo(UploadFile uploadFile){
        return fileUploadMapper.updateByPrimaryKeySelective(uploadFile) > 0;
    }

    public PageResult geFilesPage(PageQueryUtil pageUtil){
        List<UploadFile> uploadFiles = fileUploadMapper.findAllFilesList(pageUtil);
        int total = fileUploadMapper.getNumOfTotalFiles(pageUtil);
        PageResult pageResult = new PageResult(uploadFiles, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public Boolean add_file(UploadFile uploadFile){
        return fileUploadMapper.insertSelective(uploadFile) > 0;
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return fileUploadMapper.deleteBatch(ids) > 0;
    }
}
