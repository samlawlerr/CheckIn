package android.bignerdbranch.com;

import android.bignerdbranch.com.CrimeDbSchema.CheckInCursorWrapper;
import android.bignerdbranch.com.CrimeDbSchema.CheckInDbSchema;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.bignerdbranch.com.CrimeDbSchema.CheckInBaseHelper;

public class CheckInLab {
    private static CheckInLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CheckInLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CheckInLab(context);
        }
        return sCrimeLab;
    }
    private CheckInLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CheckInBaseHelper(mContext)
                .getWritableDatabase();

    }

    public void addCheckIn(CheckIn c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CheckInDbSchema.CrimeTable.NAME, null, values);

    }


    public List<CheckIn> getCheckIn() {
        List<CheckIn> crimes = new ArrayList<>();
        CheckInCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCheckIn());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public CheckIn getCheckIn(UUID id) {
        CheckInCursorWrapper cursor = queryCrimes(
                CheckInDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCheckIn();
        } finally {
            cursor.close();
        }

    }

    public File getPhotoFile(CheckIn crime) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }

    public void deleteCheckIn(CheckIn crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.delete(CheckInDbSchema.CrimeTable.NAME, "uuid =" + '"' + crime.getId() + '"', null);
    }

    public void updateCrime(CheckIn crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CheckInDbSchema.CrimeTable.NAME, values,
                CheckInDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private CheckInCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CheckInDbSchema.CrimeTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new CheckInCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(CheckIn crime) {
        ContentValues values = new ContentValues();
        values.put(CheckInDbSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CheckInDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CheckInDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CheckInDbSchema.CrimeTable.Cols.LOCATION, crime.getLocation());
        return values;
    }
}

