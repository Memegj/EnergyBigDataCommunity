package upc.backend.mapper;

import jakarta.annotation.Resource;
import upc.backend.entity.User;

public interface UserAMapper {
    User selectByID(Integer userid);
}
