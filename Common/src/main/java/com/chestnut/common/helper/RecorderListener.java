package com.chestnut.common.helper;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2017/8/7 17:12
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public abstract class RecorderListener {
    public void onRecordTooShort(String file, int THE_READY_TIME){}
    public void onRecordStart(String file){}
    public void onRecordDBChange(double dbValue){}
    public void onRecordFail(String file, String msg){}
    public void onRecordEnd(String file, int duration){}
    public void onRecordTooLong(String file, int THE_MAX_RECORD_TIME_SECOND, int theTimeLeft){}
}
