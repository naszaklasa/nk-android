package pl.nk.android;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.NkApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import pl.nk.android.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class NKExample extends Activity {

	/**
	 * Defined here: http://developers.nk.pl/developers
	 */
	protected static String KEY = "TWOJ_KLUCZ";
	protected static String SECRET = "TWOJ_SEKRET";
	protected static String CALLBACK_URL = "http://localhost";
	/**
	 * @see http://developers.nk.pl/pl/dokumentacja/tworcy-stron-internetowych/integracja-z-api/#scopes
	 */
	protected static String SCOPE = "BASIC_PROFILE_ROLE"; 
	protected String authCode = null;
	protected OAuthService oAuthServiceInstace = null;
	protected Token accessToken = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnLocal = (Button) findViewById(R.id.buttonLocalhost);
		btnLocal.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					retrieveAuthCode();
				} catch (Exception e) {
					appendText("Nie mogę pobrać authCode");
					e.printStackTrace();
				}
			}
		});

		Button btnReq = (Button) findViewById(R.id.buttonReqest);
		btnReq.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					makeDataRequest();
				} catch (Exception e) {
					appendText("Nie mogę pobrać danych");
					e.printStackTrace();
				}
			}
		});

		Button btnAccess = (Button) findViewById(R.id.buttonAccessToken);
		btnAccess.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					showAccessToken();
				} catch (Exception e) {
					appendText("Nie ma ustawionego accessTokena");
					e.printStackTrace();
				}
			}
		});

		Button btnClearCookie = (Button) findViewById(R.id.buttonClearCookie);
		btnClearCookie.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				clearCookies();
				appendText("Ciastka wyczyszczone");
			}
		});
	}

	protected static String getAuthCodeUrl() {
		return "https://nk.pl/oauth2/login?client_id=" + KEY
				+ "&response_type=code&redirect_uri=" + CALLBACK_URL
				+ "&scope=" + SCOPE;
	}

	protected void retrieveAuthCode() {
		WebView webview = getWebView();
		webview.setVisibility(View.VISIBLE);

		webview.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				Uri parsedUri = Uri.parse(url);
				String codeParam = null;
				try {
					codeParam = parsedUri.getQueryParameter("code");
				} catch (UnsupportedOperationException e) {
				}
				if (codeParam != null) {
					getWebView().setVisibility(View.GONE);
					appendText("Pobrany authCode: " + codeParam);
					authCode = codeParam;
					getWebView().loadData("Logowanie z NK", "text/html", "utf-8");
				}

			}
		});

		webview.loadUrl(getAuthCodeUrl());
	}

	protected WebView getWebView() {
		return (WebView) findViewById(R.id.webview);
	}

	protected void appendText(String textString) {
		TextView text = (TextView) findViewById(R.id.result);
		text.setText(textString + "\n ---------------- \n" + text.getText());
	}

	protected OAuthService getOAuthService() {
		if (oAuthServiceInstace == null) {
			oAuthServiceInstace = new ServiceBuilder().provider(NkApi.class)
					.apiKey(KEY).apiSecret(SECRET).callback(CALLBACK_URL)
					.scope("BASIC_PROFILE_ROLE").build();
		}
		return oAuthServiceInstace;
	}

	protected Token getAccessToken() {
		if (accessToken == null) {
			OAuthService service = getOAuthService();
			Verifier v = new Verifier(this.authCode);
			try {
				this.accessToken = service.getAccessToken(null, v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return accessToken;
	}

	protected void makeDataRequest() {
		String url_open = "http://opensocial.nk-net.pl/v09/social/rest/people/@me";

		OAuthService service = getOAuthService();

		OAuthRequest request = new OAuthRequest(Verb.GET, url_open);
		request.addQuerystringParameter("fields",
				"id,age,name,currentLocation,emails");

		service.signRequest(getAccessToken(), request);
		Response response = request.send();
		appendText(response.getBody());
	}

	protected void showAccessToken() {
		appendText(getAccessToken().toString());
	}

	protected void clearCookies() {
		CookieSyncManager cookieSyncManager = CookieSyncManager
				.createInstance(getWebView().getContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeSessionCookie();
		cookieSyncManager.sync();
	}

}
