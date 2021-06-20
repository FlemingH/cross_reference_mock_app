package com.volvo.crm;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private MockTxtController mockTxtController = new MockTxtController();

    @Test
    public void test_get_mock_txt() {
        String mockTxt = mockTxtController.getMockTxt();
        System.out.println(mockTxt);
    }
}