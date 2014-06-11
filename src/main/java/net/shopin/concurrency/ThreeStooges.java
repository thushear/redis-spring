package net.shopin.concurrency;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-11
 * Time: 下午1:38
 */

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * ThreeStooges
 * <p/>
 * Immutable class built out of mutable underlying objects,
 * demonstration of candidate for lock elision
 *
 * @author Brian Goetz and Tim Peierls
 */
@Immutable
public class ThreeStooges {

    private final Set<String> stooges = new HashSet<String>();

    public ThreeStooges() {
        stooges.add("Moe");
        stooges.add("larry");
        stooges.add("curly");
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }

    public String getStoogeNames() {
        List<String> stooges = new Vector<String>();
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
        return stooges.toString();
    }
}
