package com.mdgd.academy2020.models.repo.user.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mdgd.academy2020.models.dao.Entity;
import com.mdgd.academy2020.models.dao.sqlite.SqlDao;
import com.mdgd.academy2020.models.repo.user.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserDaoSql extends SqlDao<User> implements UserDao {

    public UserDaoSql(Context context) {
        super(new UserSqlHelper(context, null));
    }

    @Override
    public ContentValues toContentValues(@NotNull User entity, @NotNull ContentValues cv) {
        if (entity.getId() != Entity.DEFAULT_ID) {
            cv.put(UserSqlHelper.COLUMN_ID, entity.getId());
        }
        cv.put(UserSqlHelper.COLUMN_UID, entity.getUid());
        cv.put(UserSqlHelper.COLUMN_EMAIL, entity.getEmail());
        cv.put(UserSqlHelper.COLUMN_NICKNAME, entity.getNickname());
        cv.put(UserSqlHelper.COLUMN_AVATAR_URL, entity.getImageUrl());
        cv.put(UserSqlHelper.COLUMN_AVATAR_PATH, entity.getImagePath());
        return cv;
    }

    @Override
    public User fromCursor(@NotNull Cursor cursor) {
        final User user = new User(
                get(cursor, UserSqlHelper.COLUMN_EMAIL, ""),
                get(cursor, UserSqlHelper.COLUMN_NICKNAME, ""),
                get(cursor, UserSqlHelper.COLUMN_AVATAR_URL, ""),
                get(cursor, UserSqlHelper.COLUMN_AVATAR_PATH, ""),
                get(cursor, UserSqlHelper.COLUMN_UID, ""),
                get(cursor, UserSqlHelper.COLUMN_AVATAR_HASH, ""),
                get(cursor, UserSqlHelper.COLUMN_AVATAR_TYPE, "")
        );
        user.setId(get(cursor, UserSqlHelper.COLUMN_ID, Entity.DEFAULT_ID));
        return user;
    }

    @Override
    public User getUserByUid(String uid) {
        final List<User> users = new ArrayList<>();
        execRead(() -> {
            final Cursor cursor = db.query(sqLiteOpenHelper.getTableName(), null, UserSqlHelper.COLUMN_UID + "=?", new String[]{uid}, null, null, null);
            parseCursor(cursor, users);
        });
        return users.isEmpty() ? null : users.get(0);
    }
}
