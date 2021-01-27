package com.fei.demo;

import com.fei.demo.kotlin.Test1;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        Test1.newInstance().main();
        Test1.newInstance().line();

    }

    @Test
    public void test() throws Exception {
        Test1.newInstance().main();
        Test1.newInstance().star();
    }


}