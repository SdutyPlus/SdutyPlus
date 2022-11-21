package com.d108.sduty.utils;

import java.util.Date;

public class TimeCompare {
	public static boolean compare(Date expireTime) {
		System.out.println(expireTime.getTime());
		System.out.println(System.currentTimeMillis());
		long diffSec = (expireTime.getTime() - System.currentTimeMillis()) / 1000;		
		if(diffSec >= 0)
			return true;
		else {
			return false;
		}
		
	}
}
