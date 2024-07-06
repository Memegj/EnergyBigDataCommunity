package upc.backend.controller.EchartExample;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import upc.backend.entity.ChartData;
import upc.backend.service.ChartDataService;
import upc.backend.util.Result;
import upc.backend.util.ResultGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EchartShow {
    @Resource
    private ChartDataService chartDataService;

    @RequestMapping(value = "/chart_data/get_name_options", method = RequestMethod.GET)
    public Result list() {
        return ResultGenerator.genSuccessResult(chartDataService.getNameOptions());
    }

    @RequestMapping(value = "/chart_data/get_data_by_name", method = RequestMethod.GET)
    public Result get_data(@RequestParam String currentSelect) {
        List<ChartData> chartDataList = chartDataService.getChartData(currentSelect);
        List<String> weekList = new ArrayList<String>();
        List<Integer> valueList = new ArrayList<Integer>();
        for (int i = 0; i < chartDataList.size(); i++) {
            weekList.add(chartDataList.get(i).getWeek());
            valueList.add(chartDataList.get(i).getValue());
        }
        HashMap<String, List> map = new HashMap<>();
        map.put("weekData", weekList);
        map.put("valueData", valueList);
        return ResultGenerator.genSuccessResult(map);
    }
}
