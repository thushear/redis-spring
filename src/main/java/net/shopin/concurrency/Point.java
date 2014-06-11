package net.shopin.concurrency;

/**
 * Point
 * <p/>
 * Immutable Point class used by DelegatingVehicleTracker
 *
 * @author Brian Goetz and Tim Peierls
 */
/**
 * 说明:
 * User: kongming
 * Date: 14-6-11
 * Time: 下午4:50
 */
@Immutable
public class Point {

    public final int x,y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
