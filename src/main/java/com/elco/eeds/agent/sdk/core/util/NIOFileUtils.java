package com.elco.eeds.agent.sdk.core.util;

/**
 * @ClassName NIOFileUtils
 * @Description NIO读写文件工具类
 * @Author ouyang
 * @Date 2023/2/3 14:32
 * @Version 1.0
 */

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * NIO读写文件工具类
 */
public class NIOFileUtils {

    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public NIOFileUtils(String file) throws IOException {
        super();
        this.file = file;
    }

    /**
     * NIO读取文件
     *
     * @param allocate
     * @throws IOException
     */
    public void read(int allocate) throws IOException {

        RandomAccessFile access = new RandomAccessFile(this.file, "r");
        try {
            //FileInputStream inputStream = new FileInputStream(this.file);
            FileChannel channel = access.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(allocate);

            CharBuffer charBuffer = CharBuffer.allocate(allocate);
            Charset charset = Charset.forName("GBK");
            CharsetDecoder decoder = charset.newDecoder();
            int length = channel.read(byteBuffer);
            while (length != -1) {
                byteBuffer.flip();
                decoder.decode(byteBuffer, charBuffer, true);
                charBuffer.flip();
                System.out.println(charBuffer.toString());
                // 清空缓存
                byteBuffer.clear();
                charBuffer.clear();
                // 再次读取文本内容
                length = channel.read(byteBuffer);
            }
            channel.close();

        } finally {
            if (access != null) {
                access.close();
            }
        }
    }

    /**
     * NIO写文件
     *
     * @param context
     * @param allocate
     * @param chartName
     * @throws IOException
     */
    public void writeLines(String context, int allocate, String chartName) throws IOException {
        //文件内容追加模式--推荐
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(this.file, true);
            FileChannel channel = outputStream.getChannel();
            //创建一个byte缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(allocate);
            byteBuffer.put(context.getBytes(chartName));
            byteBuffer.clear();
            channel.write(byteBuffer);
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    /**
     * NIO写文件
     *
     * @param context
     * @param allocate
     * @param chartName
     * @throws IOException
     */
    public void write(String context, int allocate, String chartName) throws IOException {
        //文件内容追加模式--推荐
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(this.file, true);
            FileChannel channel = outputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(allocate);
            byteBuffer.put(context.getBytes(chartName));
            byteBuffer.flip();//读取模式转换为写入模式
            channel.write(byteBuffer);
            channel.close();
            if (outputStream != null) {
                outputStream.close();
            }
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }

    }

    /**
     * nio事实现文件拷贝
     *
     * @param source
     * @param target
     * @param allocate
     * @throws IOException
     */
//    public static void nioCpoy(String source, String target, int allocate) throws IOException {
//        ByteBuffer byteBuffer = ByteBuffer.allocate(allocate);
//        FileInputStream inputStream = null;
//        FileOutputStream outputStream = null;
//        FileChannel outChannel = null;
//        try {
//            inputStream = new FileInputStream(source);
//            FileChannel inChannel = inputStream.getChannel();
//
//            outputStream = new FileOutputStream(target);
//            outChannel = outputStream.getChannel();
//
//            int length = inChannel.read(byteBuffer);
//            while (length != -1) {
//                byteBuffer.flip();//读取模式转换写入模式
//                outChannel.write(byteBuffer);
//                byteBuffer.clear(); //清空缓存，等待下次写入
//                // 再次读取文本内容
//                length = inChannel.read(byteBuffer);
//            }
//        } finally {
//            if (null != inputStream) {
//                inputStream.close();
//            }
//            if (null != outputStream) {
//                outputStream.close();
//            }
//            if (null != outChannel) {
//                outChannel.close();
//            }
//        }
//
//    }

    //IO方法实现文件k拷贝
//    private static void traditionalCopy(String sourcePath, String destPath) throws Exception {
//        File source = new File(sourcePath);
//        File dest = new File(destPath);
//        if (!dest.exists()) {
//            dest.createNewFile();
//        }
//        FileInputStream fis = null;
//        FileOutputStream fos = null;
//        try {
//            fis = new FileInputStream(source);
//            fos = new FileOutputStream(dest);
//            byte[] buf = new byte[1024];
//            int len = 0;
//            while ((len = fis.read(buf)) != -1) {
//                fos.write(buf, 0, len);
//            }
//        } finally {
//			if (null != fos) {
//				fos.close();
//			}
//            if (null != fis) {
//                fis.close();
//            }
//        }
//
//    }

//    public static void main(String[] args) throws Exception {
//        /*long start = System.currentTimeMillis();
//        traditionalCopy("D:\\常用软件\\JDK1.8\\jdk-8u181-linux-x64.tar.gz", "D:\\常用软件\\JDK1.8\\IO.tar.gz");
//        long end = System.currentTimeMillis();
//        System.out.println("用时为：" + (end-start));*/
//
//        long start = System.currentTimeMillis();
//        nioCpoy("D:\\常用软件\\JDK1.8\\jdk-8u181-linux-x64.tar.gz", "D:\\常用软件\\JDK1.8\\NIO.tar.gz", 1024);
//        long end = System.currentTimeMillis();
//        System.out.println("用时为：" + (end - start));
//    }

}

