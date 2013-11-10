package com.wheaton.utility;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Handler;

public class Utils {
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size=1024;
		try {
			byte[] bytes=new byte[buffer_size];
			for(;;) {
				int count = is.read(bytes, 0, buffer_size);
				if(count == -1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){}
	}
	public static HttpClient sslClient(HttpClient client) {
		try {
			X509TrustManager tm = new X509TrustManager() { 
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]{tm}, null);
			SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = client.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return new DefaultHttpClient(ccm, client.getParams());
		} catch (Exception ex) {
			return null;
		}
	}
	public static void isNetworkAvailable(final String url, final Handler handler, final int timeout) {
		new Thread() {
			private boolean responded = false;

			@Override
			public void run() {
				new Thread() {

					@Override
					public void run() {
						HttpGet requestForTest = new HttpGet(url);
						try {
							new DefaultHttpClient().execute(requestForTest); // can last...
							responded = true;
						} catch (Exception e) {}
					}

				}.start();

				try {
					int waited = 0;
					while(!responded && (waited < timeout)) {
						sleep(100);
						if(!responded ) { 
							waited += 100;
						}
					}
				} 
				catch(InterruptedException e) {} // do nothing 
				finally { 
					if (!responded) { handler.sendEmptyMessage(0); } 
					else { handler.sendEmptyMessage(1); }
				}

			}
		}.start();

	}
}