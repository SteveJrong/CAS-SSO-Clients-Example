import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class CasTest3 {

	public static String getTicket(final String server, final String username, final String password,
			final String service) {
		notNull(server, "server must not be null");
		notNull(username, "username must not be null");
		notNull(password, "password must not be null");
		notNull(service, "service must not be null");

		return getServiceTicket(server, getTicketGrantingTicket(server, username, password), service);
	}

	/**
	 * 根据TGT获取ST
	 * 
	 * @param server
	 * @param ticketGrantingTicket
	 * @param service
	 * @return
	 */
	private static String getServiceTicket(final String server, final String ticketGrantingTicket,
			final String service) {
		if (ticketGrantingTicket == null){
			return null;
		}
		
		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod(server + "/" + ticketGrantingTicket);
		post.setRequestBody(new NameValuePair[] { new NameValuePair("service", service) });

		try {
			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();

			switch (post.getStatusCode()) {
				case 200:
					return response;
				default:
					System.out.println("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
					System.out.println("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
					break;
			}
		}

		catch (final IOException e) {
			System.err.println(e.getMessage());
		}
		finally {
			post.releaseConnection();
		}
		return null;
	}

	/**
	 * 根据用户登录信息获取TGT
	 * 
	 * 
	 */
	private static String getTicketGrantingTicket(final String server, final String username, final String password) {
		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod(server);
		post.setRequestBody(new NameValuePair[] { new NameValuePair("username", username),
				new NameValuePair("password", password) });

		try {
			client.executeMethod(post);
			final String response = post.getResponseBodyAsString();
			System.out.println("TGT=" + response);
			
			switch (post.getStatusCode()) {
				case 201: {
					final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(response);
	
					if (matcher.matches())
						return matcher.group(1);
	
					System.out.println("Successful ticket granting request, but no ticket found!");
					System.out.println("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
					break;
				}
				default:
					System.out.println("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
					System.out.println("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
					break;
				}
		} catch (final IOException e) {
			System.err.println(e.getMessage());
		} finally {
			post.releaseConnection();
		}
		return null;
	}

	/**
	 * 验证票据
	 * @param serverValidate
	 * @param serviceTicket
	 * @param service
	 */
	private static void ticketValidate(String serverValidate, String serviceTicket, String service) {
		notNull(serviceTicket, "paramter 'serviceTicket' is not null");
		notNull(service, "paramter 'service' is not null");

		final HttpClient client = new HttpClient();
		GetMethod post = null;

		try {
			post = new GetMethod(serverValidate + "?" + "ticket=" + serviceTicket + "&service="
					+ URLEncoder.encode(service, "UTF-8"));
			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();
			System.out.println(response);
			
			switch (post.getStatusCode()) {
				case 200: {
					System.out.println("成功取得用户数据");
				}
				default: {

				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			// 释放资源
			post.releaseConnection();
		}
	}
	
	/**
	 * 测试的Get请求
	 * @param serverValidate
	 * @param serviceTicket
	 * @param service
	 */
	private static void testGet(String serviceTicket) {
		final HttpClient client = new HttpClient();
		GetMethod post = null;

		try {
			post = new GetMethod("http://cas.client.com:8081/index?ticket=" + serviceTicket);
			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();
			System.out.println(response);
			
			switch (post.getStatusCode()) {
				case 200: {
					System.out.println("成功访问请求");
					break;
				}
				case 302: {
					System.out.println("重定向了？！");
					break;
				}
				default: {

				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			// 释放资源
			post.releaseConnection();
		}
	}

	private static void notNull(final Object object, final String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void main(final String[] args) throws Exception {
		final String server = "https://cas.server.com:8443/cas/v1/tickets";
		final String username = "admin";
		final String password = "123456";
		final String service = "http://cas.client.com:8081/index"; // 随意写
		final String proxyValidate = "https://cas.server.com:8443/cas/proxyValidate";

//		ticketValidate(proxyValidate, getTicket(server, username, password, service), service);
		testGet(getTicket(server, username, password, service));
	}
}