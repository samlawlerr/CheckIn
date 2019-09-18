package android.bignerdbranch.com.CrimeDbSchema;

import android.bignerdbranch.com.CheckIn;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class CheckInCursorWrapper extends CursorWrapper {
    public CheckInCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public CheckIn getCheckIn() {
        String uuidString = getString(getColumnIndex(CheckInDbSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CheckInDbSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CheckInDbSchema.CrimeTable.Cols.DATE));
        String location = getString(getColumnIndex(CheckInDbSchema.CrimeTable.Cols.LOCATION));


        CheckIn check = new CheckIn(UUID.fromString(uuidString));
        check.setTitle(title);
        check.setDate(new Date(date));
        check.getLocation();
        return check;
    }
}

