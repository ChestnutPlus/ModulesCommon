package com.chesnut.Common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Locale;

import static com.chesnut.Common.utils.ConstUtils.BYTE;
import static com.chesnut.Common.utils.ConstUtils.GB;
import static com.chesnut.Common.utils.ConstUtils.KB;
import static com.chesnut.Common.utils.ConstUtils.MB;


/**
 * <pre>
 *     author: Chestnut
 *     blog  :
 *     time  : 2016年10月7日
 *     desc  : 转换相关工具类
 *     thanks To:
 *     dependent on:
 *          ConstUtils
 *          StringUtils
 *          EmptyUtils
 *          FileUtils
 * </pre>
 */
public class ConvertUtils {

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes byte数组
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        char[] ret = new char[bytes.length << 1];
        for (int i = 0, j = 0; i < bytes.length; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * hexString转byteArr
     * <p>例如：</p>
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexString2Bytes(String hexString) {
        int len = hexString.length();
        if(len % 2 !=0){
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    /**
     * hexChar转int
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private static int hex2Dec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * charArr转byteArr
     *
     * @param chars 字符数组
     * @return 字节数组
     */
    public static byte[] chars2Bytes(char[] chars) {
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (chars[i]);
        }
        return bytes;
    }

    /**
     * byteArr转charArr
     *
     * @param bytes 字节数组
     * @return 字符数组
     */
    public static char[] bytes2Chars(byte[] bytes) {
        int len = bytes.length;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (bytes[i] & 0xff);
        }
        return chars;
    }

    /**
     * 字节数转以unit为单位的size
     *
     * @param byteNum 字节数
     * @param unit    <ul>
     *                <li>{@link ConstUtils.MemoryUnit#BYTE}: 字节</li>
     *                <li>{@link ConstUtils.MemoryUnit#KB}  : 千字节</li>
     *                <li>{@link ConstUtils.MemoryUnit#MB}  : 兆</li>
     *                <li>{@link ConstUtils.MemoryUnit#GB}  : GB</li>
     *                </ul>
     * @return 以unit为单位的size
     */
    public static double byte2Size(long byteNum, ConstUtils.MemoryUnit unit) {
        if (byteNum < 0) return -1;
        switch (unit) {
            default:
            case BYTE:
                return (double) byteNum / BYTE;
            case KB:
                return (double) byteNum / KB;
            case MB:
                return (double) byteNum / MB;
            case GB:
                return (double) byteNum / GB;
        }
    }

    /**
     * 以unit为单位的size转字节数
     *
     * @param size 大小
     * @param unit <ul>
     *             <li>{@link ConstUtils.MemoryUnit#BYTE}: 字节</li>
     *             <li>{@link ConstUtils.MemoryUnit#KB}  : 千字节</li>
     *             <li>{@link ConstUtils.MemoryUnit#MB}  : 兆</li>
     *             <li>{@link ConstUtils.MemoryUnit#GB}  : GB</li>
     *             </ul>
     * @return 字节数
     */
    public static long size2Byte(long size, ConstUtils.MemoryUnit unit) {
        if (size < 0) return -1;
        switch (unit) {
            default:
            case BYTE:
                return size * BYTE;
            case KB:
                return size * KB;
            case MB:
                return size * MB;
            case GB:
                return size * GB;
        }
    }

    /**
     * 字节数转合适大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 1...1024 unit
     */
    public static String byte2FitSize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < KB) {
            return String.format(Locale.getDefault(), "%.3fB", (double) byteNum);
        } else if (byteNum < MB) {
            return String.format(Locale.getDefault(), "%.3fKB", (double) byteNum / KB);
        } else if (byteNum < GB) {
            return String.format(Locale.getDefault(), "%.3fMB", (double) byteNum / MB);
        } else {
            return String.format(Locale.getDefault(), "%.3fGB", (double) byteNum / GB);
        }
    }

    /**
     * inputStream转outputStream
     *
     * @param is 输入流
     * @return outputStream子类
     */
    public static ByteArrayOutputStream input2OutputStream(InputStream is) {
        if (is == null) return null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] b = new byte[KB];
            int len;
            while ((len = is.read(b, 0, KB)) != -1) {
                os.write(b, 0, len);
            }
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            FileUtils.closeIO(is);
        }
    }

    /**
     * outputStream转inputStream
     *
     * @param out 输出流
     * @return inputStream子类
     */
    public static ByteArrayInputStream output2InputStream(OutputStream out) {
        if (out == null) return null;
        return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
    }

    /**
     * inputStream转byteArr
     *
     * @param is 输入流
     * @return 字节数组
     */
    public static byte[] inputStream2Bytes(InputStream is) {
        return input2OutputStream(is).toByteArray();
    }

    /**
     * byteArr转inputStream
     *
     * @param bytes 字节数组
     * @return 输入流
     */
    public static InputStream bytes2InputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * outputStream转byteArr
     *
     * @param out 输出流
     * @return 字节数组
     */
    public static byte[] outputStream2Bytes(OutputStream out) {
        if (out == null) return null;
        return ((ByteArrayOutputStream) out).toByteArray();
    }

    /**
     * outputStream转byteArr
     *
     * @param bytes 字节数组
     * @return 字节数组
     */
    public static OutputStream bytes2OutputStream(byte[] bytes) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            os.write(bytes);
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            FileUtils.closeIO(os);
        }
    }

    /**
     * inputStream转string按编码
     *
     * @param is          输入流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String inputStream2String(InputStream is, String charsetName) {
        if (is == null || StringUtils.isSpace(charsetName)) return null;
        try {
            return new String(inputStream2Bytes(is), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转inputStream按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    public static InputStream string2InputStream(String string, String charsetName) {
        if (string == null || StringUtils.isSpace(charsetName)) return null;
        try {
            return new ByteArrayInputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * outputStream转string按编码
     *
     * @param out         输出流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String outputStream2String(OutputStream out, String charsetName) {
        if (out == null) return null;
        try {
            return new String(outputStream2Bytes(out), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转outputStream按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    public static OutputStream string2OutputStream(String string, String charsetName) {
        if (string == null || StringUtils.isSpace(charsetName)) return null;
        try {
            return bytes2OutputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bitmap转byteArr
     *
     * @param bitmap bitmap对象
     * @param format 格式
     * @return 字节数组
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byteArr转bitmap
     *
     * @param bytes 字节数组
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable drawable对象
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * bitmap转drawable
     *
     * @param res    resources对象
     * @param bitmap bitmap对象
     * @return drawable
     */
    public static Drawable bitmap2Drawable(Resources res, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(res, bitmap);
    }

    /**
     * drawable转byteArr
     *
     * @param drawable drawable对象
     * @param format   格式
     * @return 字节数组
     */
    public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format) {
        return bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * byteArr转drawable
     *
     * @param res   resources对象
     * @param bytes 字节数组
     * @return drawable
     */
    public static Drawable bytes2Drawable(Resources res, byte[] bytes) {
        return bitmap2Drawable(res, bytes2Bitmap(bytes));
    }

    /**
     * view转Bitmap
     *
     * @param view 视图
     * @return bitmap
     */
    public static Bitmap view2Bitmap(View view) {
        if (view == null) return null;
        Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        }else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     *      把 Uri 转变 为 真实的 String 路径
     * @param context 上下文
     * @param uri  URI
     * @return 转换结果
     */
    public static String uri2Path(Context context, Uri uri) {
        if ( null == uri ) return null;
        String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     *      把 String path 路径转换为 Uri
     * @param path 路径
     * @return uri
     */
    public static Uri path2Uri(String path) {
        if (EmptyUtils.isEmpty(path)) return null;
        return Uri.fromFile(new File(path));
    }

    /**
     * 通信格式转换
     *
     * Java 和一些windows编程语言如c、c++、delphi所写的网络程序进行通讯时，需要进行相应的转换
     * 高、低字节之间的转换
     * windows的字节序为低字节开头
     * linux,unix的字节序为高字节开头
     * java则无论平台变化，都是高字节开头
     * 通讯协议中，都是低字节开头。
     *
     * HL:高字节在前,Big-Endian,大端
     * LH:低字节在前,Little-Endian,小端（网络端）
     *
     * Thanks To:
     *      1.  http://rdman.iteye.com/blog/1069041
     *      2.  http://blog.csdn.net/hackbuteer1/article/details/7722667
     */
    /**
     * 将int转为高字节在前，低字节在后的byte数组
     * @param n int
     * @return byte[]
     */
    public static byte[] int2HL(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将int转为低字节在前，高字节在后的byte数组
     * @param n int
     * @return byte[]
     */
    public static byte [] int2LH(int n) {
        byte [] b =  new   byte [ 4 ];
        b[0 ] = ( byte ) (n &  0xff );
        b[1 ] = ( byte ) (n >>  8  &  0xff );
        b[2 ] = ( byte ) (n >>  16  &  0xff );
        b[3 ] = ( byte ) (n >>  24  &  0xff );
        return  b;
    }

    /**
     * 将 short转为低字节在前，高字节在后的byte数组
     * @param n short
     * @return byte[]
     */
    public static byte [] short2LH(short n) {
        byte [] b = new  byte [ 2 ];
        b[0 ] = ( byte ) (n &  0xff );
        b[1 ] = ( byte ) (n >>  8  &  0xff );
        return  b;
    }

    /**
     * 将 short转为高字节在前，低字节在后的byte数组
     * @param n short
     * @return byte[]
     */
    public static byte [] short2HL( short  n) {
        byte [] b =  new   byte [ 2 ];
        b[1] = (byte) (n &  0xff );
        b[0] = (byte) (n >>  8  &  0xff );
        return  b;
    }

    /**
     * 将 float转为低字节在前，高字节在后的byte数组
     */
    public static byte [] float2LH( float  f) {
        return int2LH(Float.floatToRawIntBits(f));
    }
    /**
     * 将 float转为高字节在前，低字节在后的byte数组
     */
    public static byte [] float2HL( float  f) {
        return  int2HL(Float.floatToRawIntBits(f));
    }

    /**
     * 将高字节数组转换为int
     * @param b byte[]
     * @return int
     */
    public static int hLBytes2Int( byte [] b) {
        int s =  0 ;
        for  ( int  i =  0 ; i <  3 ; i++) {
            if  (b[i] >=  0 ) {
                s = s + b[i];
            } else  {
                s = s + 256  + b[i];
            }
            s = s * 256 ;
        }
        if  (b[ 3 ] >=  0 ) {
            s = s + b[3 ];
        } else  {
            s = s + 256  + b[ 3 ];
        }
        return  s;
    }

    /**
     * 将低字节数组转换为int
     * @param b byte[]
     * @return int
     */
    public static int lHBytes2Int( byte [] b) {
        int  s =  0 ;
        for  ( int  i =  0 ; i <  3 ; i++) {
            if  (b[ 3 -i] >=  0 ) {
                s = s + b[3 -i];
            } else  {
                s = s + 256  + b[ 3 -i];
            }
            s = s * 256 ;
        }
        if  (b[ 0 ] >=  0 ) {
            s = s + b[0 ];
        } else  {
            s = s + 256  + b[ 0 ];
        }
        return  s;
    }

    /**
     * 高字节数组到short的转换
     * @param b byte[]
     * @return short
     */
    public static short hLBytes2Short( byte [] b) {
        int  s =  0 ;
        if  (b[ 0 ] >=  0 ) {
            s = s + b[0 ];
        } else  {
            s = s + 256  + b[ 0 ];
        }
        s = s * 256 ;
        if  (b[ 1 ] >=  0 ) {
            s = s + b[1 ];
        } else  {
            s = s + 256  + b[ 1 ];
        }
        short  result = ( short )s;
        return  result;
    }

    /**
     * 低字节数组到short的转换
     * @param b byte[]
     * @return short
     */
    public static short lHBytes2Short( byte [] b) {
        int  s =  0 ;
        if  (b[ 1 ] >=  0 ) {
            s = s + b[1 ];
        } else  {
            s = s + 256  + b[ 1 ];
        }
        s = s * 256 ;
        if  (b[ 0 ] >=  0 ) {
            s = s + b[0 ];
        } else  {
            s = s + 256  + b[ 0 ];
        }
        short  result = ( short )s;
        return  result;
    }

    /**
     * 高字节数组转换为float
     * @param b byte[]
     * @return float
     */
    public static float hLBytes2Float( byte [] b) {
        int  i =  0 ;
        Float F = new  Float( 0.0 );
        i = ((((b[0 ]& 0xff )<< 8  | (b[ 1 ]& 0xff ))<< 8 ) | (b[ 2 ]& 0xff ))<< 8  | (b[ 3 ]& 0xff );
        return  F.intBitsToFloat(i);
    }

    /**
     * 低字节数组转换为float
     * @param b byte[]
     * @return float
     */
    public static float lHBytes2Float( byte [] b) {
        int  i =  0 ;
        Float F = new  Float( 0.0 );
        i = ((((b[3 ]& 0xff )<< 8  | (b[ 2 ]& 0xff ))<< 8 ) | (b[ 1 ]& 0xff ))<< 8  | (b[ 0 ]& 0xff );
        return  F.intBitsToFloat(i);
    }

    /**
     * 将字节数组转换为String
     * @param b byte[]
     * @return String
     */
    public static String bytes2String(byte [] b) {
        StringBuffer result = new  StringBuffer( "" );
        int  length = b.length;
        for  ( int  i= 0 ; i< length; i++) {
            result.append((char )(b[i] &  0xff ));
        }
        return  result.toString();
    }

    /**
     * 将字符串转换为byte数组
     * @param s String
     * @return byte[]
     */
    public static byte [] string2Bytes(String s) {
        return  s.getBytes();
    }

    /**
     *  将表示正数的bytes[](大端的)转为二进制。左边为最高位，右边为最低位。
     *  Thanks To: http://blog.csdn.net/uikoo9/article/details/27980869
     * @param bytes 数据源
     * @return String
     */
    public static String bytes2Binary(byte[] bytes){
        return new BigInteger(1, bytes).toString(2);// 这里的1代表正数,2代表二进制。
    }

    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String getBinary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     *  判断当前的编译器是不是大端？
     * @return  boolean
     */
    public static boolean isCompilerBigEndian() {
        int x;
        int x0;
        x = 0x80;
        x0= x & 0x01;  //低地址单元
        return x0 == 0 ? true : false;
    }

}
