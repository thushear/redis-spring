package net.shopin.concurrency;

/**
 * MutablePoint
 * <p/>
 * Mutable Point class similar to java.awt.Point
 *
 * @author Brian Goetz and Tim Peierls
 */
/**
 * 说明:
 * User: kongming
 * Date: 14-6-11
 * Time: 下午4:32
 */
@NotThreadSafe
public class MutablePoint {

    public int x,y;

    public MutablePoint() {
        x=0;
        y=0;
    }


    public MutablePoint(MutablePoint p) {
        this.x = p.x;
        this.y = p.y;
    }
}
