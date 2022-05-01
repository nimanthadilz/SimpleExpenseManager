package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final DatabaseHelper databaseHelper;

    public PersistentTransactionDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_ACCOUNT_NO, accountNo);
        cv.put(DatabaseHelper.COLUMN_EXPENSE_TYPE, expenseType.name());
        cv.put(DatabaseHelper.COLUMN_AMOUNT, amount);

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = formatter.format(date);
        cv.put(DatabaseHelper.COLUMN_DATE, dateString);

        db.insert(DatabaseHelper.TRANSACTION_TABLE, null, cv);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionsList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TRANSACTION_TABLE,
                null,
                null,
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String accountNo = cursor.getString(0);
                String expenseType = cursor.getString(1);
                double amount = cursor.getDouble(2);
                String date = cursor.getString(3);

                try {
                    Transaction transaction = new Transaction(
                            new SimpleDateFormat("dd-MM-yyyy").parse(date),
                            accountNo,
                            ExpenseType.valueOf(expenseType),
                            amount);
                    transactionsList.add(transaction);
                } catch (Exception e) {
                }
            }
            ;
        }
        cursor.close();
        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TRANSACTION_TABLE,
                null,
                null,
                null,
                null,
                null,
                null,
                Integer.toString(limit));
        List<Transaction> transactionsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String accountNo = cursor.getString(0);
            String expenseType = cursor.getString(1);
            double amount = cursor.getDouble(2);
            String date = cursor.getString(3);

            try {
                Transaction transaction = new Transaction(
                        new SimpleDateFormat("dd-MM-yyyy").parse(date),
                        accountNo,
                        ExpenseType.valueOf(expenseType),
                        amount);
                transactionsList.add(transaction);
            } catch (Exception e) {
            }
        }
        cursor.close();
        return transactionsList;
    }
}
