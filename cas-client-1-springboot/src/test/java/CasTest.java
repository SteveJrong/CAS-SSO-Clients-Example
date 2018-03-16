import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class CasTest {
	public static void main(String... args) throws Exception {
		String username = "admin";
		String password = "123456";
		String ticket = validateFromCAS(username, password);

		String serviceURL = "http://cas.client.com:8081/info";
		getJsonByInternet(serviceURL + "?ticket=" + ticket);
	}

	public static String getJsonByInternet(String path) {
		try {
			URL url = new URL(path.trim());
			// 打开连接
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			if (200 == urlConnection.getResponseCode()) {
				// 得到输入流
				InputStream is = urlConnection.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while (-1 != (len = is.read(buffer))) {
					baos.write(buffer, 0, len);
					baos.flush();
				}
				return baos.toString("utf-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String validateFromCAS(String username, String password) throws Exception {
		String ticket = null;
		String url = "https://cas.server.com:8443/cas/v1/tickets";
		try {
			HttpURLConnection hsu = (HttpURLConnection) openConn(url);
			String s = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
			s += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

			System.out.println(s);
			OutputStreamWriter out = new OutputStreamWriter(hsu.getOutputStream());
			BufferedWriter bwr = new BufferedWriter(out);
			bwr.write(s);
			bwr.flush();
			bwr.close();
			out.close();

			String tgt = hsu.getHeaderField("location");
			System.out.println(hsu.getResponseCode());
			if (tgt != null && hsu.getResponseCode() == 201) {
				System.out.println(tgt);

				System.out.println("Tgt is : " + tgt.substring(tgt.lastIndexOf("/") + 1));
				tgt = tgt.substring(tgt.lastIndexOf("/") + 1);
				bwr.close();
				closeConn(hsu);

				String serviceURL = "http://cas.client.com:8081/info";
				String encodedServiceURL = URLEncoder.encode("service", "utf-8") + "="
						+ URLEncoder.encode(serviceURL, "utf-8");
				System.out.println("Service url is : " + encodedServiceURL);

				String myURL = url + "/" + tgt;
				System.out.println(myURL);
				hsu = (HttpURLConnection) openConn(myURL);
				out = new OutputStreamWriter(hsu.getOutputStream());
				bwr = new BufferedWriter(out);
				bwr.write(encodedServiceURL);
				bwr.flush();
				bwr.close();
				out.close();

				System.out.println("Response code is:  " + hsu.getResponseCode());

				BufferedReader isr = new BufferedReader(new InputStreamReader(hsu.getInputStream()));
				String line;
				System.out.println(hsu.getResponseCode());
				System.out.println("ticket is:");
				while ((line = isr.readLine()) != null) {
					System.out.println(line);
					ticket = line;
				}
				isr.close();
				hsu.disconnect();
				return ticket;

			} else {
				return null;
			}

		} catch (MalformedURLException mue) {
			mue.printStackTrace();
			throw mue;

		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		}
	}

	static URLConnection openConn(String urlk) throws MalformedURLException, IOException {

		URL url = new URL(urlk);
		HttpURLConnection hsu = (HttpURLConnection) url.openConnection();
		hsu.setDoInput(true);
		hsu.setDoOutput(true);
		hsu.setRequestMethod("POST");
		return hsu;
	}

	static void closeConn(HttpURLConnection c) {
		c.disconnect();
	}
}