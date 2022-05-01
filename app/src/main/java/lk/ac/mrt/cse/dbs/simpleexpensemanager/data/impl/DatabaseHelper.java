package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String ACCOUNT_TABLE = "ACCOUNT_TABLE";
    public static final String COLUMN_ACCOUNT_NO = "ACCOUNT_NO";
    public static final String COLUMN_BANK_NAME = "BANK_NAME";
    public static final String COLUMN_HOLDER_NAME = "HOLDER_NAME";
    public static final String COLUMN_BALANCE = "BALANCE";
    public static final String TRANSACTION_TABLE = "TRANSACTION_TABLE";
    public static final String COLUMN_EXPENSE_TYPE = "EXPENSE_TYPE";
    public static final String COLUMN_AMOUNT = "AMOUNT";
    public static final String COLUMN_DATE = "DATE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "190111B", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAccountTableStatement = "CREATE TABLE " + ACCOUNT_TABLE +
                " (" + COLUMN_ACCOUNT_NO + " " + "TEXT PRIMARY KEY," +
                COLUMN_BANK_NAME + " TEXT, " +
                COLUMN_HOLDER_NAME + " TEXT, " +
                COLUMN_BALANCE + " INT)";
        String createTransactionTableStatement = "CREATE TABLE " + TRANSACTION_TABLE + " " +
                "(" + COLUMN_ACCOUNT_NO + ", " +
                COLUMN_EXPENSE_TYPE + " TEXT," +
                COLUMN_AMOUNT + " REAL," +
                COLUMN_DATE + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_ACCOUNT_NO + ") REFERENCES " + ACCOUNT_TABLE +
                "(" + COLUMN_ACCOUNT_NO + "))";
        db.execSQL(createAccountTableStatement);
        db.execSQL(createTransactionTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
