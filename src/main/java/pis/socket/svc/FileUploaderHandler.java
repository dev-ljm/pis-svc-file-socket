package pis.socket.svc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import pis.socket.svc.dto.CarImageDto;
import pis.socket.svc.dto.Message;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class FileUploaderHandler  extends ChannelInboundHandlerAdapter {

    private CarImageDto carImage;
    private String msgId;
    private File file;

    private MessageDispatcher messageDispatcher;
    static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    public FileUploaderHandler(CarImageDto carImage, MessageDispatcher messageDispatcher) {
        this.carImage = carImage;
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        Channel channel = ctx.channel();
        if (!channelGroup.contains(channel)) { // 초기진입

            int copyLength = 22;
            int msgIdx = 0;
            ByteBuf carInfo = byteBuf.copy(msgIdx, copyLength);

            this.msgId = carInfo.copy(msgIdx++, 1).toString(Charset.forName("UTF-8"));
            String flag = carInfo.copy(msgIdx++, 1).toString(Charset.forName("UTF-8"));
            String carNo = carInfo.copy(msgIdx++, 20).toString(Charset.forName("UTF-8"));
            carNo = StringUtils.deleteWhitespace((StringUtils.trimToEmpty(carNo)).replaceAll("@", "").toUpperCase());

            Message message = null;
            if("A".equals(this.msgId)) {
                // 입차 이미지파일
                LocalDateTime currentLocalDateTime = LocalDateTime.now();
                String imageName = currentLocalDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

                LocalDate currentLocalDate = LocalDate.now();
                String imageDateDir = currentLocalDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                // 차량번호로 입차정보를
                StringBuilder tempImageDir = new StringBuilder(this.carImage.getFileDir());
                tempImageDir.append(flag).append("/");
                tempImageDir.append(imageDateDir).append("/");
                String imageDir = tempImageDir.toString();

                File fileDir = new File(imageDir.toString());
                if (!fileDir.exists()) {
                    fileDir.mkdir();
                }

                String fileName = imageDir + imageName + "." + this.carImage.getFileType();

                this.file = new File(fileName);
                if (!this.file.exists()) {
                    this.file.createNewFile();
                }

                message = Message.builder()
                        .msgId(this.msgId)
                        .flag(flag)
                        .carNo(carNo)
                        .imageName(imageName)
                        .imageDateDir(imageDateDir)
                        .fileDir(this.carImage.getFileDir())
                        .linkDir(this.carImage.getLinkDir())
                        .imageType(this.carImage.getFileType())
                        .build();

                byteBuf.skipBytes(copyLength);
            } else {
                message = Message.builder()
                        .msgId(this.msgId)
                        .flag(flag)
                        .carNo(carNo)
                        .fileDir(this.carImage.getFileDir())
                        .linkDir(this.carImage.getLinkDir())
                        .build();
            }


            // 비동기 호출
            this.messageDispatcher.dispatch(ctx, message);

            channelGroup.add(channel);
        } else {

            if("A".equals(this.msgId)) {
                // 파일 업로드 전문인 경우!

                RandomAccessFile randomAccessFile = new RandomAccessFile(this.file, "rw");
                FileChannel fileChannel = randomAccessFile.getChannel();

                ByteBuffer byteBuffer = byteBuf.nioBuffer();
                while (byteBuffer.hasRemaining()) {
                    fileChannel.position(this.file.length());
                    fileChannel.write(byteBuffer);
                }

                byteBuf.release();
                fileChannel.close();
                randomAccessFile.close();
            } else if("B".equals(this.msgId)) {
                // ignore!!
                log.debug("ignore!!");
            }


        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
