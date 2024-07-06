package upc.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import upc.backend.entity.ChartData;
import upc.backend.mapper.ChartDataMapper;

import java.util.List;
import java.util.Map;

@Service
public class ChartDataService {
    @Resource
    private ChartDataMapper chartDataMapper;
    public List<String> getNameOptions(){
        return chartDataMapper.getNameOptions();
    }

    public List<ChartData> getChartData(String name){
        List<ChartData> chartDataList = chartDataMapper.getChartData(name);
        return chartDataList;
    }
}
