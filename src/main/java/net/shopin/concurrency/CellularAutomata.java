package net.shopin.concurrency;

/**
 * CellularAutomata
 *
 * Coordinating computation in a cellular automaton with CyclicBarrier
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-12
 * Time: 下午3:22
 */
public class CellularAutomata {

    private final  Board mainBoard;

    private final CyclicBarrier barrier;

    private final Worker[] workers;


    public CellularAutomata(final Board mainBoard) {
        this.mainBoard = mainBoard;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count,new Runnable() {
            @Override
            public void run() {
                mainBoard.commitNewValues();
            }
        });
        this.workers = new Worker[count];
        for(int i = 0;i<count;i++)
            workers[i] = new Worker(mainBoard.getSubBoard(count,i));

    }

    private class Worker implements Runnable{

        private final Board board;

        private Worker(Board board) {
            this.board = board;
        }

        private int computeValue(int x, int y) {
            // Compute the new value that goes in (x,y)
            return 0;
        }
        @Override
        public void run() {
            while (!board.hasConverged()){
                for (int x = 0;x<board.getMaxX();x++)
                    for (int y = 0;y<board.getMaxY();y++)
                        board.setNewValue(x,y,computeValue(x,y));
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    return;
                } catch (BrokenBarrierException e) {
                    return;
                }

            }
        }
    }

    public void start(){
        for(int i=0;i<workers.length;i++)
            new Thread(workers[i]).start();
        mainBoard.waitForConvergence();
    }


    public static void main(String[] args) {
        final Board board = new Board() {
            @Override
            public int getMaxX() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public int getMaxY() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public int getValue(int x, int y) {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public int setNewValue(int x, int y, int value) {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void commitNewValues() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean hasConverged() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void waitForConvergence() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Board getSubBoard(int numPartitions, int index) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        new CellularAutomata(board).start();
    }

    interface Board {
        int getMaxX();
        int getMaxY();
        int getValue(int x, int y);
        int setNewValue(int x, int y, int value);
        void commitNewValues();
        boolean hasConverged();
        void waitForConvergence();
        Board getSubBoard(int numPartitions, int index);
    }
}
