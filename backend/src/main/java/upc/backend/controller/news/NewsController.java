package upc.backend.controller.news;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upc.backend.common.BatchIdParam;
import upc.backend.controller.news.Param.NewsP;
import upc.backend.controller.news.Param.NewsUpDataParam;
import upc.backend.entity.News;
import upc.backend.service.NewsService;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.Result;
import upc.backend.util.ResultGenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2024-07-10
 */
@RestController
@RequestMapping("/api")
public class NewsController {
    @Autowired
    private NewsService newsService;
    //保存

    //修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody News news){
        return newsService.updateById(news);
    }
    //新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody News news){
        return newsService.saveOrUpdate(news);
    }
    @GetMapping("/delete")
    public boolean delete(Integer id){
        return newsService.removeById(id);
    }
    @GetMapping( "/news/search")
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize,@RequestParam(required = false) String title) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 5) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (title == null) {
            return ResultGenerator.genFailResult("请输入标题");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newsService.getNewsPage2(pageUtil,title));
    }
    //这个也是模糊查询，但是没有实现分页，不需要了
    @GetMapping("/news/getnews")
    public Result getnews(){
        List<News> list=newsService.getnews();
        return ResultGenerator.genSuccessResult(list);
    }
    @GetMapping( "/news/getnew")
    public Result list(@RequestParam(required = false) Integer pageNumber,
                       @RequestParam(required = false) Integer pageSize) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 5) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newsService.getNewsPage(pageUtil));
    }
        @RequestMapping(value = "/news/delete", method = RequestMethod.DELETE)
    //@ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam) {

        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (newsService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

        @PostMapping("/news/NewsAdd")
        public Result<String> addNews(@RequestBody NewsP newsaddparam) {
             //进行参数校验
//            if (newsaddparam.getTitle() == null || newsaddparam.getReleaseAuthor() == null || newsaddparam.getReleaseTime()==null
//                    || newsaddparam.getUrl() == null || newsaddparam.getContent()==null){
//                return ResultGenerator.genFailResult("请输入完整信息");
//            }

            // 将前端传来的数据转换为News实体类
            News news = new News();
            news.setTitle(newsaddparam.getTitle());
            news.setReleaseauthor(newsaddparam.getRelease());
            news.setReleasetime(String.valueOf(newsaddparam.getTime()));
            news.setUrl(newsaddparam.getUrl());
            news.setContent(newsaddparam.getContent());

            long timestamp = 1657526400000L; // 2024-07-10 的时间戳
            Date date = new Date(timestamp);
            news.setUploadtime(date);

                newsService.addNews(news);



            return ResultGenerator.genSuccessResult("success");
        }


    @PostMapping("/news/upDataNews/{id}")
    public Result<String> upDataNews(@RequestBody NewsUpDataParam newsaddparam, @PathVariable("id") Integer id) {
        //进行参数校验
//            if (newsaddparam.getTitle() == null || newsaddparam.getReleaseAuthor() == null || newsaddparam.getReleaseTime()==null
//                    || newsaddparam.getUrl() == null || newsaddparam.getContent()==null){
//                return ResultGenerator.genFailResult("请输入完整信息");
//            }

        // 将前端传来的数据转换为News实体类
        News news = new News();
        news.setId(id);
        news.setTitle(newsaddparam.getTitle());
        news.setReleaseauthor(newsaddparam.getRelease());
        news.setReleasetime(String.valueOf(newsaddparam.getTime()));
        news.setUrl(newsaddparam.getUrl());
        news.setContent(newsaddparam.getContent());
        long timestamp = 1657526400000L; // 2024-07-10 的时间戳
        Date date = new Date(timestamp);
        news.setUploadtime(date);
        newsService.upData(news);
        return ResultGenerator.genSuccessResult("success");
    }





}

