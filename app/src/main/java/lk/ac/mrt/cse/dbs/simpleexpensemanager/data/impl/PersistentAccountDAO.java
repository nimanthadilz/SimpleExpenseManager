package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentAccountDAO implements AccountDAO {
    private final DatabaseHelper databaseHelper;

    public PersistentAccountDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.ACCOUNT_TABLE,
                new String[] { DatabaseHelper.COLUMN_ACCOUNT_NO },
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            String accountID = cursor.getString(0);
            accountNumbersList.add(accountID);
        }
        ;
        cursor.close();
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.ACCOUNT_TABLE,
                null,
                null,
                null,
                null,
                null,
                null);
        List<Account> accountsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String accountNo = cursor.getString(0);
            String bankName = cursor.getString(1);
            String accountHolder = cursor.getString(2);
            double balance = cursor.getDouble(3);
            accountsList.add(new Account(accountNo, bankName, accountHolder, balance));
        }
        cursor.close();
        return accountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account;
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.ACCOUNT_TABLE,
                null,
                DatabaseHelper.COLUMN_ACCOUNT_NO + "=?",
                new String[] { accountNo },
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3));
            cursor.close();
            db.close();
            return account;
        } else {
            throw new InvalidAccountException("Account no " + accountNo + " is invalid.");
        }

    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.COLUMN_ACCOUNT_NO, account.getAccountNo());
        cv.put(DatabaseHelper.COLUMN_BANK_NAME, account.getBankName());
        cv.put(DatabaseHelper.COLUMN_HOLDER_NAME, account.getAccountHolderName());
        cv.put(DatabaseHelper.COLUMN_BALANCE, account.getBalance());

        db.insert(DatabaseHelper.ACCOUNT_TABLE, null, cv);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        db.delete(
                DatabaseHelper.ACCOUNT_TABLE,
                DatabaseHelper.COLUMN_ACCOUNT_NO + "=?",
                new String[] { accountNo });
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        // Get account
        Account account = this.getAccount(accountNo);

        ContentValues cv = new ContentValues();
        int newBalance;
        switch (expenseType) {
            case EXPENSE:
                cv.put(DatabaseHelper.COLUMN_BALANCE, account.getBalance() - amount);
                break;
            case INCOME:
                cv.put(DatabaseHelper.COLUMN_BALANCE, account.getBalance() + amount);
                break;
        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.update(
                DatabaseHelper.ACCOUNT_TABLE,
                cv,
                DatabaseHelper.COLUMN_ACCOUNT_NO + "=?",
                new String[] { accountNo });
    }
}
