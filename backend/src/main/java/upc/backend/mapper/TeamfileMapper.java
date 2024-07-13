package upc.backend.mapper;
import upc.backend.entity.Teamfile;
import upc.backend.util.PageQueryUtil;

import java.util.List;

public interface TeamfileMapper {
    int insert(Teamfile teamfile);

    int insertSelective(Teamfile teamfile);


    List<Teamfile> selectByDataId(Integer DataId);
    Teamfile selectByCodeId(Integer CodeId);
    Teamfile selectByVideoId(Integer VideoId);
    Teamfile selectByLiterId(Integer LiterId);

    int updateByPrimaryKeySelective(Teamfile teamfile);

    int updateByPrimaryKey(Teamfile teamfile);

    List<Teamfile> findAllTeamfileList(PageQueryUtil pageUtil);
    int getNumOfTotalTeamfile(PageQueryUtil pageUtil);
    int deleteBatch(Integer[] ids);

}
