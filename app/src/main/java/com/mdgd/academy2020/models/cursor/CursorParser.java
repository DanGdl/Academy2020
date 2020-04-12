package com.mdgd.academy2020.models.cursor;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

public interface CursorParser<T> {

    T fromCursor(@NonNull Cursor c);

    ContentValues toContentValues(@NonNull T item);

    ContentValues toContentValues(@NonNull T item, @NonNull ContentValues cv);
}
