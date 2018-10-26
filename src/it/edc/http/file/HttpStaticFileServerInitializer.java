package it.edc.http.file;

import java.io.File;

import javax.net.ssl.SSLException;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpStaticFileServerInitializer extends ChannelInitializer<SocketChannel> {

	public HttpStaticFileServerInitializer() {
	}

	@Override
	public void initChannel(SocketChannel ch) {

		try {
			SslContext sslCtx = SslContextBuilder.forServer(new File("domain-chain.crt"), new File("domain.pem"), "Em@43Dc").build();
			
//			SslHandler sslHandler = SSLHandlerProvider.getSSLHandler();
			
			ChannelPipeline pipeline = ch.pipeline();
			
//			pipeline.addLast(sslHandler);
			
			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
			pipeline.addLast(new HttpServerCodec());
			pipeline.addLast(new HttpObjectAggregator(65536));
			pipeline.addLast(new ChunkedWriteHandler());
			pipeline.addLast(new HttpStaticFileServerHandler());
			
		} catch (SSLException e) {
			e.printStackTrace();
		}


	}
}
