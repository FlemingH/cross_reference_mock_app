package com.volvo.crprotobuf;

import com.volvocars.crprotobuf.HdRoute;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testProtobuf() {

        HdRoute.NavigationRoute navigationRoute = HdRoute.NavigationRoute.newBuilder().setLength(1).setLinknumber(1).addLinks(
                HdRoute.Links.newBuilder().setLength(1).setLevel(1).setUsage(1).setNum(1).addLinkpt(
                        HdRoute.Linkpt.newBuilder().setLat(1).setLon(1)
                ).addLinkpt(
                        HdRoute.Linkpt.newBuilder().setLat(2).setLon(2)
                )
        ).build();

        System.out.println(navigationRoute);

    }
}