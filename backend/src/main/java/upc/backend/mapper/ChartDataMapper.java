package upc.backend.mapper;

import upc.backend.entity.ChartData;
import upc.backend.entity.Reference;

import java.util.List;
import java.util.Map;

public interface ChartDataMapper {
    List<String> getNameOptions();
    List<ChartData> getChartData(String name);
}
