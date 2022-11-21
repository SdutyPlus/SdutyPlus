package com.d108.sduty.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class NaverLoginServiceImpl implements NaverLoginService {

	@Override
	public Map<String, Object> getUserInfo(String token) {		
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        try {
            String apiURL = "https://openapi.naver.com/v1/nid/me";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(response.toString());
			
			JsonObject naver_account = element.getAsJsonObject().get("response").getAsJsonObject();

			String name = naver_account.getAsJsonObject().get("name").getAsString();
			String email = naver_account.getAsJsonObject().get("email").getAsString();
			String mobile = naver_account.getAsJsonObject().get("mobile").getAsString();
			System.out.println(naver_account);
			Map<String, Object> userInfo = new HashMap<String, Object>();
			userInfo.put("name", name);
			userInfo.put("email", email);
			userInfo.put("mobile", mobile);
			return userInfo;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
	}
	
}
