package com.android.bitglobal.tool;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 创建时间: 2017/5/3
 * 创建人: junhai
 * 功能描述:
 */
public class StringUtilsTest {
    StringUtils utils;
    @Before
    public void setUp() throws Exception {
        utils=new StringUtils();
    }

    @After
    public void tearDown() throws Exception {
        utils=null;
    }

    @Test
    public void formatDouble() throws Exception {
        assertEquals("1.12",utils.formatDouble(1.123456,2));
        assertEquals("1.123",utils.formatDouble(1.123456,3));
        assertEquals("-1.12",utils.formatDouble(-1.123456,2));
    }

}