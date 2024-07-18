package upc.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import upc.backend.controller.news.Param.NewsQueryParam;
import upc.backend.entity.News;
import upc.backend.util.PageQueryUtil;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wms
 * @since 2024-07-10
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {

    int insert(News news);
    News selectByID(Integer id);
    int updateById(News news);
   //删除
    int deleteBatch(Integer[] ids);

     List<News> findBySearch( NewsQueryParam params);

    List<News> findAllNewsList(PageQueryUtil pageUtil);
    int getNumOfTotalNews(PageQueryUtil pageUtil);

    List<News> getnews();

    List<News> findSelectNewsList(@Param("title") PageQueryUtil pageUtil,String title);

    int getNumOfSelectNews(PageQueryUtil pageUtil);


}
