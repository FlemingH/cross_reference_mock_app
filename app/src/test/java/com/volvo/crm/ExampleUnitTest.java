package com.volvo.crm;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;

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

        Gson gson = new Gson();
        ArrayList mockList = gson.fromJson(mockTxt, ArrayList.class);

        String[] mockItems = (String[]) mockList.toArray(new String[0]);

        for (String s: mockItems
             ) {
            System.out.println(s);
        }
    }
}