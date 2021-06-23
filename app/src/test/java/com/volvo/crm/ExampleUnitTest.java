package com.volvo.crm;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private MockController mockController = new MockController();

    @Test
    public void test_get_mock_txt() {
        String mockTxt = mockController.getMockList();
        System.out.println(mockTxt);
    }
}