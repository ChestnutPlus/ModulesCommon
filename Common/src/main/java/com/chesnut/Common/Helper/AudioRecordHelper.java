package com.chesnut.Common.Helper;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 15:07
 *     desc  :  AudioRecordHelper
 *              AudioRecord的封装
 *     thanks To:
 *     dependent on:
 *     update log:
 *          1.0.0   2017年4月1日21:46:47   栗子  initial
 *          1.0.1   2017年4月15日16:07:39  栗子
 *              (1) 修改了默认的 audioRate = 44100，为当前的标准。
 *                  发现，若为16000，一些机型会报错。
 *              (2) 发现，华为的转换：PCM - WAV 出现问题，
 *                  检查到最后，是因为，实时的分贝计算中：byte[] -> short[]
 *                  所导致的。
 * </pre>
 */

public class AudioRecordHelper {

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private AudioRecord recorder;
    //录音源
    private int audioSource = MediaRecorder.AudioSource.MIC;
    //录音的采样频率,设置音频采样率，44100 是目前的标准，但是某些设备仍然支持 22050，16000，11025
    private int audioRate = 44100;
    //录音的声道
    private int audioChannel = AudioFormat.CHANNEL_IN_MONO;
    //量化的深度
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    //缓存的大小
    private int bufferSize = AudioRecord.getMinBufferSize(audioRate,audioChannel,audioFormat);
    //记录播放状态
    private boolean isRecording = false;
    //wav文件目录
    private String outFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Temp.wav";
    //pcm文件目录
    private String inFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Temp.pcm";

    /**
     * 设置参数
     * @param audioSource   录音源
     * @param audioRate     录音Rate
     * @param audioChannel  声道
     * @param audioFormat   编码
     */
    public AudioRecordHelper(int audioSource, int audioRate, int audioChannel, int audioFormat) {
        this.audioSource = audioSource;
        this.audioRate = audioRate;
        this.audioChannel = audioChannel;
        this.audioFormat = audioFormat;
        bufferSize = AudioRecord.getMinBufferSize(audioRate,audioChannel,audioFormat);
        recorder = new AudioRecord(audioSource,audioRate,audioChannel,audioFormat,bufferSize);
    }

    /**
     * 默认构造函数，初始化默认值
     */
    public AudioRecordHelper(){
        recorder = new AudioRecord(audioSource,audioRate,audioChannel,audioFormat,bufferSize);
    }

    /**
     * 初始化，每次录音前调用。
     * @param outFile   录音文件路径
     */
    public void init(String outFile) {
        this.outFile = outFile;
        //创建文件
        File pcmFile = new File(inFile);
        File wavFile = new File(outFile);
        if(pcmFile.exists()){
            pcmFile.delete();
        }
        if(wavFile.exists()){
            wavFile.delete();
        }
        try{
            pcmFile.createNewFile();
            wavFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //开始录音
    public void startRecord(){
        isRecording = true;
        recorder.startRecording();
        singleThreadExecutor.execute(() -> {
            //建立文件输出流
            BufferedOutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(new File(inFile)));
            }catch (IOException e){
                if (callBack!=null)
                    callBack.onRecordFail(outFile,e.getMessage());
                return;
            }
            if (callBack!=null)
                callBack.onRecordStart(outFile);
            byte[] buffer = new byte[bufferSize];
            while (isRecording) {
                int r = recorder.read(buffer, 0, bufferSize);
                try {
                    if (callBack!=null) {
                        float v = 0;
                        // 将 buffer 内容取出，进行平方和运算
                        for (short aBuffer : Bytes2Shorts(buffer)) {
                            v += aBuffer * aBuffer;
                        }
                        // 平方和除以数据总长度，得到音量大小。
                        double mean = v / (double) r;
                        double volume = 10 * Math.log10(mean);
                        callBack.onRecordDBChange(volume);
                    }
                } catch (Exception e) {
                    if (callBack!=null) {
                        callBack.onRecordDBChange(0);
                    }
                }
                if(r>0){
                    try{
                        os.write(buffer);
                    }catch(IOException e){
                        if (callBack!=null)
                            callBack.onRecordFail(outFile,e.getMessage());
                        return;
                    }
                }
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //停止录音
    public void stopRecord(){
        isRecording = false;
        recorder.stop();
        singleThreadExecutor.execute(() -> {
            // 这里得到可播放的音频文件
            FileInputStream in;
            FileOutputStream out;
            long totalAudioLen;
            long totalDataLen;
            long longSampleRate = audioRate;
            int channels = 1;
            long byteRate = 16 * audioRate * channels / 8;
            byte[] data = new byte[bufferSize];
            try {
                in = new FileInputStream(inFile);
                out = new FileOutputStream(outFile);
                totalAudioLen = in.getChannel().size();
                //由于不包括RIFF和WAV
                totalDataLen = totalAudioLen + 36;
                WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
                while (in.read(data) != -1) {
                    out.write(data);
                }
                in.close();
                out.close();
                if (callBack!=null)
                    callBack.onRecordEnd(outFile);
            } catch (IOException e) {
                e.printStackTrace();
                if (callBack!=null)
                    callBack.onRecordFail(outFile,e.getMessage());
            }
        });
    }

    /**
     * 释放资源
     */
    public void close() {
        callBack = null;
        singleThreadExecutor.shutdown();
        recorder.release();
    }

    /**
     * 给PCM文件加上WAV的文件头
     *      任何一种文件在头部添加相应的头文件才能够确定的表示这种文件的格式，wave是RIFF文件结构，
     *      每一部分为一个chunk，其中有RIFF WAVE chunk， FMT Chunk，Fact chunk,Data chunk,
     *      其中Fact chunk是可以选择的
     *
     * @param out               输出流
     * @param totalAudioLen     totalAudioLen
     * @param totalDataLen      totalDataLen
     * @param longSampleRate    longSampleRate
     * @param channels          声道
     * @param byteRate          byteRate
     * @throws IOException IOException
     */
    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate,
                                     int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);//数据大小
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channels;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) ( ((audioChannel == AudioFormat.CHANNEL_IN_MONO ? 16 : 8 ) * channels) / 8);
        header[33] = 0;
        //每个样本的数据位数
        header[34] = (byte) (audioChannel == AudioFormat.CHANNEL_IN_MONO ? 16 : 8);
        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

    /**
     * 设置监听器
     * @param callBack  监听器
     */
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    private CallBack callBack = null;
    public interface CallBack {
        void onRecordStart(String file);
        void onRecordDBChange(double dbValue);
        void onRecordFail(String file, String msg);
        void onRecordEnd(String file);
    }

    public short[] Bytes2Shorts(byte[] buf) {
        byte bLength = 2;
        short[] s = new short[buf.length / bLength];
        for (int iLoop = 0; iLoop < s.length; iLoop++) {
            byte[] temp = new byte[bLength];
            for (int jLoop = 0; jLoop < bLength; jLoop++) {
                temp[jLoop] = buf[iLoop * bLength + jLoop];
            }
            s[iLoop] = getShort(temp);
        }
        return s;
    }

    //对 Short[] 转 byte[] ， 参考： http://www.jb51.net/article/45721.htm
    private short getShort(byte[] buf) {
        return getShort(buf, this.testCPU());
    }

    private boolean testCPU() {
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            // System.out.println("is big ending");
            return true;
        } else {
            // System.out.println("is little ending");
            return false;
        }
    }

    private short getShort(byte[] buf, boolean bBigEnding) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 2) {
            throw new IllegalArgumentException("byte array size > 2 !");
        }
        short r = 0;
        if (bBigEnding) {
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0x00ff);
            }
        } else {
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x00ff);
            }
        }
        return r;
    }
}
