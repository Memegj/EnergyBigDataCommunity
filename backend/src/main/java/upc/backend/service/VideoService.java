package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.backend.entity.Datasets;
import upc.backend.entity.Userteam;
import upc.backend.entity.Video;
import upc.backend.entity.Videocontent;
import upc.backend.mapper.DatasetsMapper;
import upc.backend.mapper.VideoContentMapper;
import upc.backend.mapper.VideoMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.ArrayList;
import java.util.List;


@Service
public class VideoService {
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private TeamService teamService;
    @Resource
    private UserteamService userteamService;
    @Autowired
    @Resource
   private VideoContentMapper videoContentMapper;
    //页数
    //获取视频信息
    public Video getVideoByVideoId(Integer VideoId){
        return videoMapper.selectByVideoId(VideoId);
    }
    public Video getVideoById(Integer VideoId){
        return videoMapper.getVideoByID(VideoId);
    }
    public PageResult getVideosPage(PageQueryUtil pageUtil){
        List<Video> videos =videoMapper.findAllVideoList(pageUtil);
        int total = videoMapper.getNumOfTotalVideos(pageUtil);

        PageResult pageResult = new PageResult(videos, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
    public List<Videocontent> getVideoContentsByVideoId(Integer videoId) {
        return videoContentMapper.selectByVideoId(videoId);
    }
    //更新文献信息
    public Boolean updateVideoInfo(Video video){
        int radd = videoMapper.updateByPrimaryKeySelective(video);
        if (radd > 0){return true;}
        else {return false;}
    }

    public PageResult getVideoPageByUserId(PageQueryUtil pageUtil){
        List<Video> videos = videoMapper.findAllVideoListByUserId(pageUtil);
        int total = videoMapper.getNumOfTotalVideoByUserId(pageUtil);
        for (Video video : videos) {
            Integer teamId = video.getTeamId();
            if (teamId == null) {
                video.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                video.setTeamName(teamName); // 假设 Datasets 类有 setTeamName 方法用于设置团队名称
            }
        }
        PageResult pageResult = new PageResult(videos, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public List<Video> getVideoByTeamId(PageQueryUtil pageUtil, Integer[] teamIdsArray){
        return videoMapper.selectVideoByTeamIds(pageUtil,teamIdsArray);
    }


    public PageResult getVideoPage(PageQueryUtil pageUtil){
        List<Userteam> userteams = userteamService.getTeamByPageUtil(pageUtil);
        List<Integer> teamIds = new ArrayList<>();

        for (Userteam userteam : userteams) {
            Integer teamId = userteam.getTeamId();
            teamIds.add(teamId); // 将 teamId 存入 teamIds 列表
        }
        Integer[] teamIdsArray = teamIds.toArray(new Integer[0]);
        List<Video> videos = getVideoByTeamId(pageUtil,teamIdsArray);
        int total = videoMapper.getNumOfUserVideo(pageUtil,teamIdsArray);
        for (Video video : videos) {
            Integer teamId = video.getTeamId();
            if (teamId == null) {
                video.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                video.setTeamName(teamName);
            }
        }
        PageResult pageResult = new PageResult(videos, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }


    public PageResult getVideoPageByUserIdOrderByTime(PageQueryUtil pageUtil){
        List<Video> videos = videoMapper.findAllVideoListByUserIdOrderByTime(pageUtil);
        int total = videoMapper.getNumOfTotalVideoByUserId(pageUtil);
        for (Video video : videos) {
            Integer teamId = video.getTeamId();
            if (teamId == null) {
                video.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                video.setTeamName(teamName);
            }
        }
        PageResult pageResult = new PageResult(videos, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public PageResult getVideoPageByUserIdOrderByTeamId(PageQueryUtil pageUtil){
        List<Video> videos = videoMapper.findAllVideoListByUserIdOrderByTeamId(pageUtil);
        int total = videoMapper.getNumOfTotalVideoByUserId(pageUtil);
        for (Video video : videos) {
            Integer teamId = video.getTeamId();
            if (teamId == null) {
                video.setTeamName("公开");
            } else {
                String teamName = teamService.getTeamNameByTeamId(teamId);
                video.setTeamName(teamName);
            }
        }
        PageResult pageResult = new PageResult(videos, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public Boolean add_video(Video video){
        int radd = videoMapper.insertSelective(video);
        if (radd > 0){return true;}
        else {return false;}
    }

    public Boolean add_file(Video video){
        return videoMapper.insertSelective(video) > 0;
    }
    public Boolean add_videocontentfile(Videocontent videocontent){
        return videoContentMapper.insertvideocontentSelective(videocontent) > 0;
    }

    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        Integer aa= videoMapper.deleteBatchCollect(ids);
        Integer bb= videoMapper.deleteBatchVideo(ids);
        return aa+bb > 0;
    }
    public Boolean deleteBatchVideocontent(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return videoContentMapper.deleteBatchVideocontent(ids) > 0;
    }

}
