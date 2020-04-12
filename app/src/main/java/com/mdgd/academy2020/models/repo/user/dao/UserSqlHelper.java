package com.mdgd.academy2020.models.repo.user.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.mdgd.academy2020.models.dao.sqlite.AppSqLiteHelper;

public class UserSqlHelper extends AppSqLiteHelper {
    static final String COLUMN_UID = "uid";
    static final String TABLE_USERS = "users";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NICKNAME = "nickname";
    static final String COLUMN_EMAIL = "email";
    static final String COLUMN_AVATAR_URL = "avatar_url";
    static final String COLUMN_AVATAR_PATH = "avatar_path";

    public UserSqlHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, factory);
    }

    public UserSqlHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, factory, errorHandler);
    }

    // @TargetApi(28)
    // public UserSqlHelper(@Nullable Context context, @NonNull SQLiteDatabase.OpenParams openParams) {
    //     super(context, openParams);
    // }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final StringBuilder sb = new StringBuilder("create table if not exists ");
        sb.append(TABLE_USERS);
        sb.append("( ");
        sb.append(COLUMN_ID);
        sb.append(" integer primary key autoincrement, ");
        sb.append(COLUMN_NICKNAME);
        sb.append(" text, ");
        sb.append(COLUMN_EMAIL);
        sb.append(" text, ");
        sb.append(COLUMN_AVATAR_URL);
        sb.append(" text, ");
        sb.append(COLUMN_AVATAR_PATH);
        sb.append(" text");

        sb.append(");");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    @Override
    public String getTableName() {
        return TABLE_USERS;
    }

    @Override
    public String getIdColumnName() {
        return COLUMN_ID;
    }
}
