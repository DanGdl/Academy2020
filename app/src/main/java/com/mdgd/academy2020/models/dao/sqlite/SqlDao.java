package com.mdgd.academy2020.models.dao.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.common.collect.FluentIterable;
import com.mdgd.academy2020.models.cursor.DefaultCursorParser;
import com.mdgd.academy2020.models.dao.Dao;
import com.mdgd.academy2020.models.dao.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class SqlDao<T extends Entity> extends DefaultCursorParser<T> implements Dao<T> {
    protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final AppSqLiteHelper sqLiteOpenHelper;
    protected SQLiteDatabase db;

    public SqlDao(AppSqLiteHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public void close() {
        sqLiteOpenHelper.close();
        db = null;
    }

    @Override
    public void open() {
        if (db == null) {
            db = sqLiteOpenHelper.getWritableDatabase();
        }
    }

    protected void execTransaction(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        open();
        lock.writeLock().lock();
        try {
            db.beginTransactionNonExclusive();
            try {
                runnable.run();
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected void execRead(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        open();
        lock.readLock().lock();
        try {
            runnable.run();
        } finally {
            lock.readLock().unlock();
        }
    }


    @Override
    public void save(T entity) {
        if (entity != null) {
            save(Collections.singletonList(entity));
        }
    }

    @Override
    public void save(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        execTransaction(() -> {
            final ContentValues cv = new ContentValues();
            for (T entity : entities) {
                saveInner(entity, cv);
            }
        });
    }

    protected void saveInner(T entity, ContentValues cv) {
        cv = toContentValues(entity, cv);
        final int result = db.update(sqLiteOpenHelper.getTableName(), cv,
                sqLiteOpenHelper.getIdColumnName() + " = ?", new String[]{String.valueOf(entity.getId())});
        if (result == 0) {
            final long insert = db.insert(sqLiteOpenHelper.getTableName(), null, cv);
            entity.setId(insert);
        }
    }

    @Override
    public T get(long id) {
        final List<T> ts = get(Collections.singletonList(id));
        return ts.isEmpty() ? null : ts.get(0);
    }

    @Override
    public List<T> get(List<Long> ids) {
        final List<T> items = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return items;
        }
        final StringBuilder sb = new StringBuilder("(");
        for (Long id : ids) {
            if (id != null) {
                if (sb.length() != 1) {
                    sb.append(", ");
                }
                sb.append(id);
            }
        }
        sb.append(")");
        final String s = sb.toString();
        if (s.equals("()")) {
            return items;
        } else {
            execRead(() -> {
                final Cursor cursor = db.rawQuery(String.format("select * from %1$s where %2$s in %3$s", sqLiteOpenHelper.getTableName(), sqLiteOpenHelper.getIdColumnName(), s), null);
                parseCursor(cursor, items);
            });
        }
        return items;
    }


    private void parseCursor(Cursor cursor, List<T> items) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    items.add(fromCursor(cursor));
                } while (cursor.moveToNext());
                items.removeAll(Collections.singletonList(null));
            }
            cursor.close();
        }
    }

    @Override
    public void delete(long id) {
        deleteById(Collections.singletonList(id));
    }

    @Override
    public void deleteById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        final StringBuilder sb = new StringBuilder("(");
        for (Long id : ids) {
            if (id != null) {
                if (sb.length() != 1) {
                    sb.append(", ");
                }
                sb.append(id);
            }
        }
        sb.append(")");
        final String s = sb.toString();
        if (!s.equals("()")) {
            execTransaction(() -> {
                final int delete = db.delete(sqLiteOpenHelper.getTableName(), sqLiteOpenHelper.getIdColumnName() + " in ?", new String[]{s});
                Log.d("SqlDao", "Deleted " + delete);
            });
        }
    }

    @Override
    public void delete(T entity) {
        if (entity != null) {
            deleteById(Collections.singletonList(entity.getId()));
        }
    }

    @Override
    public void delete(List<T> entities) {
        if (entities != null && !entities.isEmpty()) {
            deleteById(FluentIterable.from(entities)
                    .filter(e -> e != null)
                    .transform(e -> e.getId())
                    .toList());
        }
    }

    @Override
    public void deleteAll() {
        execTransaction(() -> {
            final int delete = db.delete(sqLiteOpenHelper.getTableName(), null, null);
            Log.d("SqlDao", "Deleted " + delete);
        });
    }
}
