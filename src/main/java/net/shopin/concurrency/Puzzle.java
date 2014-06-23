package net.shopin.concurrency;

import java.util.Set;

/**
 * 说明:谜题框架接口  抽象谜题
 * User: kongming
 * Date: 14-6-20
 * Time: 上午10:19
 */
public interface Puzzle<P,M> {

    P initialPosition();

    boolean isGoal(P position);

    Set<M> legalMoves(P position);

    P move(P position,M move);

}
