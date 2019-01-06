package com.unieap.service.esb.inf.unitls;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import com.unieap.base.vo.InfConfigVO;

public class SoapCallUtils {
	private static MessageFactory messageFactory;
	private static SOAPConnectionFactory connectionFactory;

	private static void factory() throws Exception {
		if (messageFactory == null) {
			messageFactory = MessageFactory.newInstance();
		}
		if (connectionFactory == null) {
			connectionFactory = SOAPConnectionFactory.newInstance();
		}
	}

	public static MessageFactory getMessageFactory() throws Exception {
		factory();
		return messageFactory;

	}

	public static SOAPConnectionFactory getConnectionFactory() throws Exception {
		factory();
		return connectionFactory;
	}

	public static SOAPMessage callWebService(String url, SOAPMessage request, int timeout) throws Exception {
		SOAPConnection connection = getConnectionFactory().createConnection();
		SOAPMessage response = connection.call(request, getURL(url, timeout));
		return response;
	}

	public static SOAPMessage callWebService(InfConfigVO infConfigVO, SOAPMessage request) throws Exception {
		SOAPConnection connection = getConnectionFactory().createConnection();
		SOAPMessage response = connection.call(request, getURL(infConfigVO.getUrl(), infConfigVO.getTimeout()));
		return response;
	}

	public static String callHTTPService(InfConfigVO infConfigVO, String request) throws Exception {
		URL url = new URL(infConfigVO.getUrl());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 3：设置连接参数
		// 3.1设置发送方式：POST必须大写
		connection.setRequestMethod("POST");
		// 3.2设置数据格式：Content-type
		connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		// 3.3设置输入输出，新创建的connection默认是没有读写权限的，
		connection.setDoInput(true);
		connection.setDoOutput(true);
		// Post 请求不能使用缓存
		connection.setUseCaches(false);
		// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成
		// connection.connect();
		// 4：组织SOAP协议数据，发送给服务端 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
		OutputStream os = connection.getOutputStream();
		os.write(request.getBytes());
		// 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
		os.flush();
		// 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
		// 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
		os.close();

		// 调用HttpURLConnection连接对象的getInputStream()函数,
		// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
		InputStream is = connection.getInputStream(); // <===注意，实际发送请求的代码段就在这里
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String temp = null;
		while (null != (temp = br.readLine())) {
			sb.append(temp);
		}
		is.close();
		isr.close();
		br.close();
		return sb.toString();
	}

	public static URL getURL(String url, final int timeout) throws Exception {
		URL urlval = new URL(new URL(url), "", new URLStreamHandler() {
			@Override
			protected URLConnection openConnection(URL url) throws IOException {
				URL target = new URL(url.toString());
				URLConnection connection = target.openConnection();
				// Connection settings
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
				return (connection);
			}
		});
		return urlval;
	}

	public static SOAPMessage callService(SOAPMessage request) throws Exception {
		InfConfigVO vo = new InfConfigVO();
		String url = vo.getUrl();
		SOAPMessage response = callWebService(url, request, 10000);
		return response;

	}

	public static SOAPMessage formartSoapString(String soapString) throws Exception, SOAPException, Exception {
		SOAPMessage reqMsg = getMessageFactory().createMessage(new MimeHeaders(),
				new ByteArrayInputStream(soapString.getBytes(Charset.forName("UTF-8"))));
		reqMsg.saveChanges();
		return reqMsg;
	}

	public static String replaceParameters(String xml, Map<String, Object> inputParameters) {
		for (Entry<String, Object> entry : inputParameters.entrySet()) {
			String replacedString = "{" + entry.getKey() + "}";
			if (xml.indexOf(replacedString) > 0) {
				xml = xml.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue().toString());
			}
		}
		return xml;
	}

	public static String coverSOAPMessageToStr(SOAPMessage message) throws Exception {
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		// Get reply content
		Source source = message.getSOAPPart().getContent();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		StreamResult result = new StreamResult(bos);
		tf.transform(source, result);
		return new String(bos.toByteArray());
	}
}
