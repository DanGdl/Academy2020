package com.mdgd.academy2020.models.repo.user.dao;

import com.mdgd.academy2020.models.dao.Dao;
import com.mdgd.academy2020.models.repo.user.User;

public interface UserDao extends Dao<User> {
    User getUserByUid(String uid);
}
