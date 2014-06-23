package net.shopin.concurrency;
/**
 * PuzzleNode
 * <p/>
 * Link node for the puzzle solving framework
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.LinkedList;
import java.util.List;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-20
 * Time: 上午10:21
 */
@Immutable
public class PuzzleNode<P,M> {

    final P pos;
    final M move;
    final PuzzleNode<P,M> prev;

    public PuzzleNode(P pos, M move, PuzzleNode<P, M> prev) {
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }


    List<M> asMoveList(){
        List<M> solution = new LinkedList<M>();
        for(PuzzleNode<P,M> n = this;n.move!=null;n=n.prev)
            solution.add(0,n.move);
        return solution;
    }




}
