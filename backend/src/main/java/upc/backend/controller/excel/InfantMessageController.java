package upc.backend.controller.excel;

import jakarta.annotation.Resource;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.Result;
import upc.backend.entity.InfantMessage;
import upc.backend.mapper.InfantMessageMapper;
import upc.backend.service.InfantMessageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import upc.backend.util.ResultGenerator;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/api")
public class InfantMessageController {

    @Resource
    private InfantMessageService infantMessageService;

    @RequestMapping(value = "/excel/list", method = RequestMethod.GET)
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize) {
        //, @RequestHeader("token") String token
        // logger.info("this test tokon:{}", token);
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        Result rr = ResultGenerator.genSuccessResult(infantMessageService.getInfantPage(pageUtil));
        return rr;
    }
    /**
     * 导出接口
     */
    @RequestMapping(value = "/excel/export", method = RequestMethod.GET)
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查出所有的数据
        //List<InfantMessage> list = infantMessageMapper.listAllInfantMessage();
        List list = infantMessageService.getExcelExportData();
        //在内存操作写到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.merge(8, "        年    月信息统计表");
        //自定义标题别名
        List ll = new ArrayList<>();
        ll.add("姓名");
        ll.add("昵称");
        ll.add("性别");
        ll.add("出生日期");
        ll.add("年龄");
        ll.add("民族");
        ll.add("身份证号");
        ll.add("出生地");
        ll.add("更新日期");
        list.addFirst(ll);

//        writer.addHeaderAlias("infantname", "幼儿姓名");
//        writer.addHeaderAlias("nickname", "昵称");
//        writer.addHeaderAlias("sex", "性别");
//        writer.addHeaderAlias("age", "年龄");
//        writer.addHeaderAlias("dateof", "出生日期");
//        writer.addHeaderAlias("infantidnumber", "身份证号");
//        writer.addHeaderAlias("nation", "民族");
//        writer.addHeaderAlias("birthplace", "出生地");
//        writer.addHeaderAlias("currentdate", "更新日期");
//

        //一次性写出list对象到Excel使用默认样式，强制输出标题
        writer.write(list, true);
        //设置行高列宽

        writer.getSheet().setDefaultRowHeightInPoints(22);
        writer.getSheet().setColumnWidth(6,5000);
        writer.getSheet().setColumnWidth(8,4000);
        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officeedocument.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("infantInfo", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }


    /**
     * 导入接口
     */
    @PostMapping("/excel/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        //直接读数据表的内容
        List<List<Object>> list = reader.read(1);
        //使用hutool里面的工具类
        List<InfantMessage> infantMessages = CollUtil.newArrayList();
        for (List<Object> row : list) {
            InfantMessage infantMessage = new InfantMessage();
            infantMessage.setInfantname(row.get(0).toString());
            infantMessage.setNickname(row.get(1).toString());
            infantMessage.setSex(row.get(2).toString());
            infantMessage.setAge(row.get(3).toString());
            infantMessage.setDateof(row.get(4).toString());
            infantMessage.setInfantidnumber(row.get(5).toString());
            infantMessage.setNation(row.get(6).toString());
            infantMessage.setBirthplace(row.get(7).toString());
            infantMessage.setCurrentdate(new Date());
            infantMessages.add(infantMessage);
        }
        return ResultGenerator.genSuccessResult(infantMessageService.saveBatch(infantMessages));
    }
}
