package upc.backend.mapper;

import upc.backend.entity.InfantMessage;
import upc.backend.entity.User;
import upc.backend.util.PageQueryUtil;

import java.util.List;
public interface InfantMessageMapper {
    /**查询全部*/
    List<InfantMessage> listAllInfantMessage();
    List<InfantMessage> findAllInfantList(PageQueryUtil pageUtil);
    int getNumOfTotalInfants(PageQueryUtil pageUtil);

    int insert(InfantMessage infantMessage);

    int insertSelective(InfantMessage infantMessage);
}