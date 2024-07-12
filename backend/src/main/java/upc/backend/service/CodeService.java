package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.common.ServiceResultEnum;
import upc.backend.controller.user.param.UserUpdateParam;
import upc.backend.entity.Code;
import upc.backend.entity.Datasets;
import upc.backend.entity.UserToken;
import upc.backend.mapper.CodeMapper;
import upc.backend.mapper.UserTokenMapper;
import upc.backend.util.NumberUtil;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;
import upc.backend.util.SystemUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CodeService {
    private final CodeMapper codeMapper;

    public CodeService(CodeMapper codeMapper) {
        this.codeMapper = codeMapper;
    }


    public Code getCodeByCodeId(Integer CodeId){
        return codeMapper.selectByCodeId(CodeId);
    }

    public PageResult getCodePage(PageQueryUtil pageUtil){
        List<Code> code = codeMapper.findAllCodeList(pageUtil);
        int total = codeMapper.getNumOfTotalCode(pageUtil);
        PageResult pageResult = new PageResult(code, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return codeMapper.deleteBatch(ids) > 0;
    }

}
