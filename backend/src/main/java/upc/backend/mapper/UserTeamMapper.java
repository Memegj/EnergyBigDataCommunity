package upc.backend.mapper;


import upc.backend.entity.UserTeam;

public interface UserTeamMapper {
    int insert(UserTeam record);
    int deleteByTeamId(Integer[] teamIds);

}
