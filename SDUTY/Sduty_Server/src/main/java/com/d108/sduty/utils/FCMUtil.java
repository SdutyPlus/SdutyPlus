package com.d108.sduty.utils;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

@Component
public class FCMUtil {
	private final String MESSAGE_TITLE = "SDUTY";
	//전체 보내기
	public void send_FCM_All(List<String>list, Map<String, String> map) {
		for(int i = 0; i < list.size(); i++) {
			send_FCM(list.get(i), MESSAGE_TITLE, map.get("message"));
		}
	}
	
	//특정 사용자한테 보내기
	public void send_FCM(String tokenId, String title, String content) {
		try {
			FileInputStream refreshToken = new FileInputStream("/var/lib/jenkins/workspace/gumid108/Sduty_Server/src/main/resources/service-account-key.json");
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(refreshToken)).setDatabaseUrl("firebase-adminsdk-jv8ed@sduty-5125f.iam.gserviceaccount.com")
					.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}
			String registrationToken = tokenId;
			Message msg = Message.builder()
					.setAndroidConfig(
							AndroidConfig.builder()
							.setTtl(3600 * 1000)
							.setPriority(AndroidConfig.Priority.NORMAL)
							.setNotification(AndroidNotification.builder()
									.setTitle(title)
									.setBody(content)
									.setIcon(null)
									.setColor(null).build())
							.build())
					.setToken(registrationToken)
					.build();
			String response = FirebaseMessaging.getInstance().send(msg);
			System.out.println("Successfully sent message: " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
