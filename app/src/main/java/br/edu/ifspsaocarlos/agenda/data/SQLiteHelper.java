package br.edu.ifspsaocarlos.agenda.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "agenda.db";
    public static final String DATABASE_TABLE = "contatos";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "nome";
    public static final String KEY_FONE = "fone";
    public static final String KEY_FONE2 = "fone2";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NIVER = "niver";
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, " +
            KEY_FONE2 + " TEXT, " +
            KEY_NIVER + " TEXT, " +
            KEY_EMAIL + " TEXT);";

    private static final String DATABASE_ALTER_ADD_FONE2 = "ALTER TABLE "
            + DATABASE_TABLE + " ADD COLUMN " + KEY_FONE2 + " TEXT;";

    private static final String DATABASE_ALTER_ADD_NIVER = "ALTER TABLE "
            + DATABASE_TABLE + " ADD COLUMN " + KEY_NIVER + " TEXT;";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            database.execSQL(DATABASE_ALTER_ADD_FONE2);
        }

        if (oldVersion < 3) {
            database.execSQL(DATABASE_ALTER_ADD_NIVER);
        }

    }
}