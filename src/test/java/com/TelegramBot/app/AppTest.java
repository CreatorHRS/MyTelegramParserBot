package com.TelegramBot.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/*
    Unit test for simple App.
*/

public class AppTest{
    @Test
    public void dateCompareEqualTest(){
        Date date1 = new Date(2000, 9, 3, 20, 12);
        Date date2 = new Date(2000, 9, 3, 20, 12);

        int res = date1.compare(date2);
        assertTrue(res == 0);
    }

    @Test
    public void dateCompareMoreTest(){
        Date date1 = new Date(2000, 9, 3, 20, 12);
        Date date2 = new Date(2000, 12, 3, 20, 12);

        int res = date1.compare(date2);
        assertTrue(res < 0);
    }

    @Test
    public void dateCompareLessTest(){
        Date date1 = new Date(2000, 11, 3, 20, 12);
        Date date2 = new Date(2000, 9, 2, 20, 12);

        int res = date1.compare(date2);
        assertTrue(res > 0);
    }

    
}