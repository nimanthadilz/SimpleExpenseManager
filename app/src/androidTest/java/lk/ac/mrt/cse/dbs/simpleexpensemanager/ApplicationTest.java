/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentDemoExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing
 * Fundamentals</a>
 */
public class ApplicationTest {
    private static ExpenseManager expenseManager;

    @BeforeClass
    public static void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        expenseManager = new PersistentDemoExpenseManager(context);
    }

    @Test
    public void testAddNewAccount() {
        assertEquals(2, expenseManager.getAccountNumbersList().size());
        expenseManager.addAccount("190", "BOC", "Kamal", 5000);
        assertEquals(3, expenseManager.getAccountNumbersList().size());
    }

    @Test
    public void testAddNewTransaction() {
        assertEquals(0, expenseManager.getTransactionLogs().size());
        try {
            expenseManager.updateAccountBalance("12345A", 05, 05, 2022, ExpenseType.EXPENSE, "500");
        } catch (InvalidAccountException e) {
            fail();
        }
        assertEquals(1, expenseManager.getTransactionLogs().size());
    }
}