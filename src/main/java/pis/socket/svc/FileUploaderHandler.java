package pis.socket.svc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import pis.socket.svc.dto.CarImageDto;
import pis.socket.svc.dto.Message;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class FileUploaderHandler  extends ChannelInboundHandlerAdapter {

    private static final MessageDispatcher serviceDispatcher = new MessageDispatcher();

    private int idx;

    private CarImageDto carImage;
    private String baseDir;
    private String fileName;
    private String msgId;

    private MessageDispatcher messageDispatcher;


    public FileUploaderHandler(CarImageDto carImage, MessageDispatcher messageDispatcher) {
        this.carImage = carImage;
        this.baseDir = carImage.getBaseDir();
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        if(idx == 0) {
            System.out.println("Starting....");

            int copyLength = 22;
            int msgIdx = 0;
            ByteBuf carInfo = byteBuf.copy( + msgIdx, copyLength);

            this.msgId = carInfo.copy( + msgIdx++, 1).toString(Charset.defaultCharset());
            String flag = carInfo.copy( + msgIdx++, 1).toString(Charset.defaultCharset());
            String carNo = carInfo.copy( + msgIdx++, 20).toString(Charset.defaultCharset());

            // 입차 이미지파일
            LocalDateTime currentDateTime = LocalDateTime.now();
            String format = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // 차량번호로 입차정보를
            StringBuilder carImageFileName = new StringBuilder(this.baseDir);
            carImageFileName.append(flag).append("/");
            carImageFileName.append(carNo + "_" + format).append(".").append(this.carImage.getImageType());

            fileName = carImageFileName.toString();

            Message message = Message.builder()
                    .msgId(msgId)
                    .flag(flag)
                    .carNo(carNo)
                    .fileName(fileName)
                    .build();

            byteBuf.skipBytes(copyLength);


            // 비동기 호출
            this.messageDispatcher.dispatch(ctx, message);

        }

        if("A".equals(this.msgId)) {
            // 파일 업로드 전문인 경우!
            File file = new File(fileName);

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
        } else if("B".equals(this.msgId)) {
            // ignore!!
            log.debug("ignore!!");
        }


        idx++;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
