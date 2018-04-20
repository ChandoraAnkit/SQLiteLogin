package androidy.chandora.ankit.sqlitelogin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "UserManager.db";

    private static String TABLE_USER = "user";

    private static String COLUMN_ID = "user_id";
    private static String COLUMN_NAME = "user_name";
    private static String COLUMN_EMAIL = "user_email";
    private static String COLUMN_PASSWORD = "user_password";

    private static String CREATE_TABLE = " CREATE TABLE " + TABLE_USER + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_EMAIL + " TEXT, "
            + COLUMN_PASSWORD + " TEXT ) ";

    private static String DROP_TABLE = " DROP TABLE IF EXISTS " + TABLE_USER;

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);

        onCreate(db);
    }

    //Adding user

    public void addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user.getId());
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    //Fetch list of all users
    public List<User> getAllUsers() {

        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_PASSWORD};
        String sortOrder = COLUMN_NAME + " ASC ";

        List<User> users = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, columns, null, null, null, null, sortOrder);
        if (cursor.isFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));

                users.add(user);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return users;
    }

    //Update users

    public void updateUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user.getId());
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());

        db.update(TABLE_USER, values, COLUMN_ID + " =? ", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //Delete user
    public void deleteUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USER, COLUMN_ID + " =? ", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //Check user
    public boolean checkUser(String email) {

        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " =? ";
        String[] selectionArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    //Check user is login or not
    public boolean checkUser(String email, String password) {

        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL +" =? "+" AND "+COLUMN_PASSWORD +" =? ";
        String[] selectionArgs = {email,password};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,columns,selection,selectionArgs,null,null,null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count >0){
            Toast.makeText(context, "User found", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            Toast.makeText(context, "User Not found", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
