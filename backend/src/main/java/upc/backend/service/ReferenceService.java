package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.controller.references.param.ReferenceUpdateParam;
import upc.backend.entity.Reference;
import upc.backend.mapper.ReferenceMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class ReferenceService {
    @Resource
    private ReferenceMapper referenceMapper;


    // 获取文献信息
    public Reference getReferenceById(Integer id){
        return referenceMapper.selectByID(id);
    }

    //更新文献信息
    public Boolean updateReferenceInfo(Reference reference){
        return referenceMapper.updateByPrimaryKeySelective(reference) > 0;
    }

    public PageResult getReferencesPage(PageQueryUtil pageUtil){
        List<Reference> references = referenceMapper.findAllReferenceList(pageUtil);
        int total = referenceMapper.getNumOfTotalReferences(pageUtil);
        PageResult pageResult = new PageResult(references, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public Boolean add_reference(Reference reference){
        int radd = referenceMapper.insertSelective(reference);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return referenceMapper.deleteBatch(ids) > 0;
    }
}
