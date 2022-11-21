package com.d108.sduty.service;

import com.d108.sduty.dto.Likes;

public interface LikesService {
	Likes insertLike(Likes like);
	void deleteLike(Likes like);
	boolean checkAlreadyLike(Likes like);
}
