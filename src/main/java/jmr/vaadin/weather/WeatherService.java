package jmr.vaadin.weather;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class WeatherService
{
	private OkHttpClient client;
	private String appid = "e26254aeec9f96decac2468f0cb2a6da";
	private String urlStart = "http://api.openweathermap.org/data/2.5/weather?q=";

	public JSONObject getWeather( String cityName, String units ) throws IOException, JSONException
	{
		String url = urlStart + cityName + "&units=metric" + "&appid=" + appid;
		if (units.equals("F"))
		{
			url = urlStart + cityName + "&units=imperial" + "&appid=" + appid;
		}
		client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		String responseData = response.body().string();
		JSONObject json = new JSONObject(responseData);
		return json;
	}

	public JSONArray getWeatherArray( String cityName, String units ) throws JSONException, IOException
	{
		return getWeather(cityName, units).getJSONArray("weather");
	}

	public double getTemperature( String cityName, String units ) throws JSONException, IOException
	{
		return getWeather(cityName, units).getJSONObject("main").getDouble("temp");
	}

	public String getLocation( String cityName, String units ) throws JSONException, IOException
	{
		return getWeather(cityName, units).getString("name");
	}

	public String getIcon( String cityName, String units ) throws JSONException, IOException
	{
		return getWeatherArray(cityName, units).getJSONObject(0).getString("icon");
	}

}
