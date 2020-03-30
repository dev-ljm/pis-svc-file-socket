package pis.socket.svc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import pis.socket.svc.dto.CarImageDto;

import javax.annotation.Resource;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FileUploaderHandler  extends ChannelInboundHandlerAdapter {

    private static final MessageDispatcher serviceDispatcher = new MessageDispatcher();

    private int idx = 0;
    private String fileName = "";
    private CarImageDto carImage;

    private String baseDir;
    public FileUploaderHandler(CarImageDto carImage) {
        this.carImage = carImage;

        this.baseDir = carImage.getBaseDir();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        // 입차 차량정보
        if (idx == 0) {

            int copyLength = 22;
            int msgIdx = 0;
            ByteBuf carInfo = byteBuf.copy(msgIdx, copyLength);

            String msgId = carInfo.copy(msgIdx++, 1).toString(Charset.defaultCharset());
            String flag = carInfo.copy(msgIdx++, 1).toString(Charset.defaultCharset());
            String carNo = carInfo.copy(msgIdx++, 20).toString(Charset.defaultCharset());

            // 차량번호로 입차정보를
            StringBuilder carImageFileName = new StringBuilder(this.baseDir);
            carImageFileName.append(flag).append("/");
            carImageFileName.append(carNo).append(".").append(this.carImage.getImageType());

            fileName = carImageFileName.toString();

            byteBuf.skipBytes(copyLength);
        }

        // 입차 이미지파일
        //LocalDateTime currentDateTime = LocalDateTime.now();
        //String format = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        File file = new File(fileName);// remember to change dest
        if (!file.exists()) {
            file.createNewFile();
        }

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        ByteBuffer byteBuffer = byteBuf.nioBuffer();

        while (byteBuffer.hasRemaining()) {
            fileChannel.position(file.length());
            fileChannel.write(byteBuffer);
        }

        byteBuf.release();
        fileChannel.close();
        randomAccessFile.close();

        idx++;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private static String getPhysicalFileName(String uploadFileName) {
        LocalTime currentTime = LocalTime.now();
        String hhmmss = currentTime.format(DateTimeFormatter.ofPattern("HHmmss"));

        int index = uploadFileName.lastIndexOf(".");
        // 확장자를뺀파일명
        String _tmpFileName = uploadFileName.substring(0, index);
        // Extension
        String _extName = uploadFileName.substring(index);
        return _tmpFileName + "_" + hhmmss + _extName;
    }
}
