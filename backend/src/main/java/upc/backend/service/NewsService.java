package upc.backend.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.backend.entity.News;
import upc.backend.mapper.NewsMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wms
 * @since 2024-07-10
 */
@Service
public class NewsService extends ServiceImpl<NewsMapper, News> {

    @Autowired
    private NewsMapper newsMapper; // 注入NewsMapper实例
    public boolean list(News news) {
        return false;
    }
    public Boolean addNews(News news){
        int newss = newsMapper.insert(news); // 使用实例调用insert方法
        if (newss > 0) {
            return true;
        } else {
            return false;
        }
    }


    public PageResult getNewsPage(PageQueryUtil pageUtil){
        List<News> news2 = newsMapper.findAllNewsList(pageUtil);
        int total = newsMapper.getNumOfTotalNews(pageUtil);
        PageResult pageResult = new PageResult(news2, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
//模糊查询
    public PageResult getNewsPage2(PageQueryUtil pageUtil,String title){
        List<News> news2 = newsMapper.findSelectNewsList(pageUtil,title);
        int total = newsMapper.getNumOfSelectNews(pageUtil);
        PageResult pageResult = new PageResult(news2, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public List<News> getnews() {
        return newsMapper.getnews();
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return newsMapper.deleteBatch(ids) > 0;
    }

    public void upData( News news){
        newsMapper.updateById(news);
    }


}