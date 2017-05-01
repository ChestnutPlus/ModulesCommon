package com.chestnut.Common.tips;

/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年10月20日16:28:09
 *     desc  : 避免数组越界
 *     thanks To:
 *     dependent on:
 * </pre>
 */
public class CyclePoint {

    private int point = 0;      //当前指向
    public int num = 0;        //总数
    private boolean isCycle = false;

    public interface PointCallBack {
        void pointNext(int point, int num);
        void pointLast(int point, int num);
        void changeSize(int point, int num);
        void onListEnd(int point, int num);
    }
    private PointCallBack callBack = null;
    public void setCallBack(PointCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     *      构造函数，传入大小和当前的指向，无指明指向，为0.
     * @param point 当前的指向
     * @param num 传入大小
     * @param isCycle 是否是循环point
     */
    public CyclePoint(int point, int num, boolean isCycle) {
        if (num<=0 || point<0) {
            this.point = 0;
            this.num = 0;
            return;
        }
        this.point = point;
        this.num = num;
        this.isCycle = isCycle;
    }

    /**
     *      构造函数，传入大小和当前的指向，无指明指向，为0.
     * @param num 传入大小
     * @param isCycle 是否是循环point
     */
    public CyclePoint(int num, boolean isCycle) {
        this(0,num,isCycle);
    }

    /**
     *      得到当前的位置
     * @return 当前的位置
     */
    public int get() {
        return point;
    }

    /**
     *      移动指针到下一个。
     * @return  下一个的位置
     */
    public int next() {
        if (isCycle)
            point = ++point == num ? 0 : point;
        else
            point = ++point == num ? num-1 : point;
        if (callBack!=null)
            callBack.pointNext(point,num);
        if (point==num-1 && callBack!=null)
            callBack.onListEnd(point,num);
        return point;
    }

    /**
     *      移到到上一个位置。
     * @return  上一个位置。
     */
    public int last() {
        if (isCycle)
            point = --point == -1 ? num-1 : point;
        else
            point = --point == -1 ? 0 : point;
        if (callBack!=null)
            callBack.pointLast(point,num);
        if (point==num-1 && callBack!=null)
            callBack.onListEnd(point,num);
        return point;
    }

    /**
     *      改变容量，point回归0.
     * @param newNum 新的容量
     * @param isCycle 是否是循环point
     * @return 新的容量
     */
    public int changeSize(int newNum, boolean isCycle) {
        this.isCycle = isCycle;
        num = newNum;
        point = 0;
        if (callBack!=null)
            callBack.changeSize(point,num);
        return num;
    }
}
