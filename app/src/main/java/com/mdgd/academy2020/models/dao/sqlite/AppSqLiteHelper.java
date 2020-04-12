package com.mdgd.academy2020.models.dao.sqlite;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class AppSqLiteHelper extends SQLiteOpenHelper {
    protected static final String DATABASE_NAME = "academy2020.db";
    protected static final int DATABASE_VERSION = 2;

    public AppSqLiteHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public AppSqLiteHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION, errorHandler);
    }

    @TargetApi(28)
    public AppSqLiteHelper(@Nullable Context context, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, DATABASE_NAME, DATABASE_VERSION, openParams);
    }

    public abstract String getTableName();

    public abstract String getIdColumnName();
}
