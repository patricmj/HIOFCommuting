package bachelor.register;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
	
	HttpClient client;
	String url;

	public JSONParser(String url) {
		client = new DefaultHttpClient();
		this.url = url;
	}

	public JSONArray loadData() throws ClientProtocolException, IOException, JSONException {
		StringBuilder strBuilder = new StringBuilder(url);
		
		HttpGet get = new HttpGet(strBuilder.toString());
		HttpResponse r = client.execute(get);
		int status = r.getStatusLine().getStatusCode();
		if(status == 200) {
			HttpEntity e = r.getEntity();
			String data = EntityUtils.toString(e);
			JSONObject jsonObj = new JSONObject(data);
			JSONArray jArray = jsonObj.getJSONArray("institutions");
			return jArray;
		} else {
			return null;
		}
	}
	
}
