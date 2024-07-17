package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.Userteam;
import upc.backend.entity.Video;
import upc.backend.mapper.VideoMapper;
import upc.backend.util.PageQueryUtil;
import upc.backend.util.PageResult;

import java.util.ArrayList;
import java.util.List;


@Service
public class videoService {
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private UserteamService userteamService;

    @Resource
    private TeamService teamService;
    //获取视频信息
    public Video getVideoById(Integer VideoId){
        return videoMapper.getVideoByID(VideoId);
    }
   //更新视频信息
    public Boolean updateVideoInfo(Video video){
        return videoMapper.updateByPrimaryKeySelective(video) > 0;
    }
    //页数
    public PageResult getVideosPage(PageQueryUtil pageUtil){
        List<Video> videos =videoMapper.findAllVideoList(pageUtil);
        int total = videoMapper.getNumOfTotalVideos(pageUtil);
        PageResult pageResult = new PageResult(videos, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
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

    public List<Video> getVideoByTeamId(PageQueryUtil pageUtil, Integer[] teamIdsArray){
        return videoMapper.selectVideoByTeamIds(pageUtil,teamIdsArray);
    }



}
