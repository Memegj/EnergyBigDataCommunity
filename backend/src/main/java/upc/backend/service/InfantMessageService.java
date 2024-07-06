package upc.backend.service;

import jakarta.annotation.Resource;
import upc.backend.common.Constants;
import upc.backend.entity.User;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;
import upc.backend.util.Result;
import upc.backend.entity.InfantMessage;

import upc.backend.mapper.InfantMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.backend.util.ResultGenerator;

import java.util.ArrayList;
import java.util.List;

@Service
public class InfantMessageService {
    @Resource
    private InfantMessageMapper infantMessageMapper;
    /***
     * 查询全部
     */
    public Result listInfantMessage() {
        //从数据库查出所有的数据
        List<InfantMessage> list = infantMessageMapper.listAllInfantMessage();
        return ResultGenerator.genSuccessResult(list);
    }
    public PageResult getInfantPage(PageQueryUtil pageUtil){
        int total = infantMessageMapper.getNumOfTotalInfants(pageUtil);
        List<InfantMessage> infantMessages = infantMessageMapper.findAllInfantList(pageUtil);
        PageResult pageResult = new PageResult(infantMessages, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public int saveBatch(List<InfantMessage> infantMessages) {
        int counter = 0;
        for(InfantMessage item : infantMessages)
        if(infantMessageMapper.insertSelective(item) > 0)
            counter++;
        return counter;
    }
    public List getExcelExportData() {
        //从数据库查出所有的数据
        List<InfantMessage> list = infantMessageMapper.listAllInfantMessage();
        List list1 = new ArrayList<>();
        for(InfantMessage item : list)
        {
            List list_temp = new ArrayList<>();
            list_temp.add(item.getInfantname());
            list_temp.add(item.getNickname());
            list_temp.add(item.getSex());
            list_temp.add(item.getDateof());
            list_temp.add(item.getAge());
            list_temp.add(item.getNation());
            list_temp.add(item.getInfantidnumber());
            list_temp.add(item.getBirthplace());
            list_temp.add(item.getCurrentdate());
            list1.add(list_temp);
        }
        return list1;
    }
}
