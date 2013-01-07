package addres.book;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAddress
{

    public static final String KEY_NAMA = "nama";
    public static final String KEY_HP = "hp";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "DBaddress";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_CREATE =
            "create table friend (_id integer primary key autoincrement, " + "nama text not null, hp text not null);";

    private static final String DATABASE_NAME = "address";
    private static final String DATABASE_TABLE = "friend";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper
    {

        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS friend");
            onCreate(db);
        }
    }

    public DBAddress(Context ctx)
    {
        this.mCtx = ctx;
    }

    public DBAddress open() throws SQLException
    {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close()
    {
        mDbHelper.close();
    }


    public long createAddress(String nama, String hp)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAMA, nama);
        initialValues.put(KEY_HP, hp);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteAddress(long rowId)
    {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllAddress()
    {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAMA, KEY_HP}, null, null, null, null, null);
    }

    public Cursor fetchAddress(long rowId) throws SQLException
    {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAMA, KEY_HP}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public boolean updateAddress(long rowId, String nama, String hp)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAMA, nama);
        args.put(KEY_HP, hp);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}

