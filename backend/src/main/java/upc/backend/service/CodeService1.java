package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Code;
import upc.backend.mapper.CodeMapper3;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

@Service
public class CodeService1 {
    @Resource
    private CodeMapper3 codeMapper;
    public Code getReferenceById(Integer codeid){
        return codeMapper.selectByID(codeid);
    }

    // 获取文献信息

    public Boolean updateReferenceInfo(Code code){
        int radd = codeMapper.updateByPrimaryKeySelective(code);
        if (radd > 0){return true;}
        else {return false;}
    }

    public PageResult getReferencesPage(PageQueryUtil pageUtil){
        List<Code> excel = codeMapper.findAllReferenceList(pageUtil);
        int total = codeMapper.getNumOfTotalReferences(pageUtil);
        PageResult pageResult = new PageResult(excel, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public Boolean add_reference(Code code){
        int radd = codeMapper.insertCode(code);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return codeMapper.deleteBatch(ids) > 0;
    }
}
