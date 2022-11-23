-- --------------------------------------------------------
-- 호스트:                          d205.kro.kr
-- 서버 버전:                        10.9.3-MariaDB-1:10.9.3+maria~ubu2204 - mariadb.org binary distribution
-- 서버 OS:                        debian-linux-gnu
-- HeidiSQL 버전:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 테이블 sdutyplusdb.daily_statistics 구조 내보내기
CREATE TABLE IF NOT EXISTS `daily_statistics` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `daily_study_time` bigint(20) DEFAULT 0,
  `user_seq` int(10) unsigned DEFAULT NULL,
  `daily_study_minute` bigint(20) DEFAULT 0,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.daily_statistics:~14 rows (대략적) 내보내기
/*!40000 ALTER TABLE `daily_statistics` DISABLE KEYS */;
INSERT INTO `daily_statistics` (`seq`, `daily_study_time`, `user_seq`, `daily_study_minute`) VALUES
	(39, 0, 38, 2),
	(41, 1, 40, 19),
	(42, 0, 41, 0),
	(58, 0, 43, 0),
	(59, 0, 44, 0),
	(60, 0, 45, 0),
	(66, 0, 51, 0),
	(69, 1, 54, 42),
	(73, 0, 58, 0),
	(81, 0, 66, 0),
	(84, 0, 69, 1),
	(86, 0, 71, 0),
	(87, 1, 72, 45),
	(89, 0, 74, 0);
/*!40000 ALTER TABLE `daily_statistics` ENABLE KEYS */;

-- 테이블 sdutyplusdb.daily_time_graph 구조 내보내기
CREATE TABLE IF NOT EXISTS `daily_time_graph` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `count` int(10) unsigned DEFAULT NULL,
  `end_time` int(10) unsigned DEFAULT NULL,
  `start_time` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.daily_time_graph:~5 rows (대략적) 내보내기
/*!40000 ALTER TABLE `daily_time_graph` DISABLE KEYS */;
INSERT INTO `daily_time_graph` (`seq`, `count`, `end_time`, `start_time`) VALUES
	(1, 245, 2, 0),
	(2, 403, 4, 2),
	(3, 152, 6, 4),
	(4, 118, 8, 6),
	(5, 82, 25, 8);
/*!40000 ALTER TABLE `daily_time_graph` ENABLE KEYS */;

-- 테이블 sdutyplusdb.feed 구조 내보내기
CREATE TABLE IF NOT EXISTS `feed` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ban_yn` bit(1) NOT NULL DEFAULT b'0',
  `content` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `img_url` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reg_time` datetime(6) NOT NULL,
  `writer_seq` int(10) unsigned NOT NULL,
  PRIMARY KEY (`seq`),
  KEY `FKqkbrrhdb9gqkl8nwwc347ol62` (`writer_seq`),
  CONSTRAINT `FKqkbrrhdb9gqkl8nwwc347ol62` FOREIGN KEY (`writer_seq`) REFERENCES `user` (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=5299 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.feed:~23 rows (대략적) 내보내기
/*!40000 ALTER TABLE `feed` DISABLE KEYS */;
INSERT INTO `feed` (`seq`, `ban_yn`, `content`, `img_url`, `reg_time`, `writer_seq`) VALUES
	(33, b'0', '모던 템플릿 하나 더 추가했어용🥰', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Ff9bc7cf4b6237d9a519c7a7bc0434119_63833046-b359-4843-a670-46bd59bc5d66?alt=media', '2022-11-15 06:30:23.256841', 40),
	(34, b'0', '확률 그지같다', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Ff09f74b1bb2cf361e6bf18d130ed8f01_bfeb0b95-4604-4056-9fdb-769e71bfd9dd?alt=media', '2022-11-15 06:38:40.295294', 40),
	(37, b'0', 'java...', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Ff7ef7da896e49631dc2279a5901eedd9_01cbbd40-3dfc-42be-b3e9-cd25166b694f?alt=media', '2022-11-15 06:45:54.677895', 40),
	(721, b'0', '질수없지', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Fbe1061a0eedd5b61be713147971b52b0_f5f2ef2d-2eb7-4b35-8960-d679b62b6034?alt=media', '2022-11-18 02:20:50.383472', 38),
	(728, b'0', '살려줘.....', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Fb7400b769bfead74cf4c25c35eacc144_34c8b0aa-8fd2-43f0-a59f-8f5910dae661?alt=media', '2022-11-18 09:37:49.942081', 54),
	(5254, b'0', '🌸어떻게든 되게 하자🌸', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F6f3f16046bfa1a7a896e49f8d68a3c12_fa21aa6d-6f09-4c53-b1c8-463c2ade6235?alt=media', '2022-11-18 15:39:28.140312', 54),
	(5255, b'0', '초초초안은 된 것 같아...😇', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Fe1bf5e4b889592f1e2aabef39a793fb9_142db0fc-22c8-4a97-891c-e902f7822d28?alt=media', '2022-11-18 18:47:47.735746', 54),
	(5256, b'0', '오늘 안으로 디자인 초안 잡고 발표 리허설 + 시간 기록까지 뽀갠다 (시연 시나리오도 잡아야지)', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Fcef29b4a5b4f6cb4dc665813ed09d654_dab0b72c-bf85-4e39-9326-c93e7daa5210?alt=media', '2022-11-19 09:04:08.476661', 54),
	(5257, b'0', '아자자자자자자자자자🔥🔥🔥🔥', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F5908ae0ed771f79e60cdca69af1fcaef_64d55ce5-30ef-4e4e-9e1e-b2fdc4e6b1a4?alt=media', '2022-11-19 14:40:26.972942', 54),
	(5259, b'0', '피피티 초안 잡은듯...? ㅠㅠ\n아직 빠진 자료 있는데 그거 채우고\n몇 개 애매한 내용 컨펌 받음 바로 채우지 않을까\n지금 발표 시간 10분정도 나오드라 ㅠㅠ\n시연이랑 UCC는 넣어보지도 못했는데\n호이이이이이이 ㅠㅠㅠㅜㅠㅠ\n내일 시연 시나리오 잡아보면서 어색한 UI도 수정해야 하고\n포팅메뉴얼/README 쓰고\njira 스프린트 닫아야 하고\n피피티 수정하고', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F43f232a9812aecf8945c3c1201b6426c_068abfa1-3484-404e-a3db-9d011d0b7a34?alt=media', '2022-11-19 20:35:36.624504', 54),
	(5260, b'0', 'ffa', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F43d12978b101b4b799645ea1ae287ad1_595bd7c5-e178-4411-9220-1e86dd51f4aa?alt=media', '2022-11-20 11:39:34.546135', 69),
	(5262, b'0', 'ㅠㅠㅠㅠㅠㅠㅠㅠ', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Fb9202923fed176ae199448a4d5e874a1_fd94c610-1348-4ffa-9855-d97f46164e36?alt=media', '2022-11-20 11:49:18.856110', 72),
	(5267, b'0', '오늘 공부한 내용', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Fecdce32b0e1a64d2aa80fc96c372fb6a_e931faeb-2299-4ed2-b7ad-9025606b23d1?alt=media', '2022-11-20 12:16:45.447121', 40),
	(5269, b'0', '5시간 인증', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Fcb9c956345cb3e53b524dd099883c729_951c6f6b-08fd-4a1d-831b-6921a86921ba?alt=media', '2022-11-20 14:19:27.287477', 69),
	(5280, b'0', '해볼까', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F8213757a424fe7d95bebdef31d3e8732_0b05bb6c-f9fb-4e1d-9c0a-ba2a04446312?alt=media', '2022-11-20 15:36:59.289254', 69),
	(5285, b'0', '고양이는 왜 귀여울까', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F0d80a65d38d2e2bc71bfdcb47909c9fc_dda137da-2a42-4aec-9bb6-e0bc0ff0bd14?alt=media', '2022-11-20 15:39:30.055145', 38),
	(5287, b'0', '', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F2c97467ccc40bef2bbfe17a06d5208d0_e21960bd-b55d-455e-9051-818b79e57efd?alt=media', '2022-11-20 15:48:56.453224', 72),
	(5291, b'0', '오늘 공부 많이 했어요', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F6774ea22becec492c6ba9534ea2b4729_a9e3599d-91b3-495c-baa1-c2be19bbdb04?alt=media', '2022-11-20 16:36:06.639598', 69),
	(5292, b'0', '오늘의 공부', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F02398b48b760e91decd4eb52ed49d486_3bf40af6-02c9-42db-84e3-475683f813af?alt=media', '2022-11-20 16:43:51.818220', 40),
	(5295, b'0', '자율......... 발표............ ㅇ>ㅡ<', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F877ef49613e6e5cb75d7397c728e47b4_753397ab-39ad-4e7f-975e-0ad9cc5a9853?alt=media', '2022-11-20 16:54:16.816888', 54),
	(5296, b'0', '프로젝트 종료 우와아아아앙!', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F22130bbcd78f65365489a64a0ff8329d_3eb0310a-3135-4295-894b-e3e0ec3dbf6d?alt=media', '2022-11-20 16:56:42.694602', 69),
	(5297, b'0', '공부인증', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2F4fd2a1770bfb4041b7ea358fa4b88996_b8a8ea87-657b-42ec-892c-745da54e08ad?alt=media', '2022-11-20 17:13:12.570714', 40),
	(5298, b'0', '', 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/feed%2Faa9ec823cc184ee3c0830c61f434def6_c62e9b3b-ef31-47bf-b324-441e37bbee6a?alt=media', '2022-11-20 17:16:36.294445', 72);
/*!40000 ALTER TABLE `feed` ENABLE KEYS */;

-- 테이블 sdutyplusdb.feed_like 구조 내보내기
CREATE TABLE IF NOT EXISTS `feed_like` (
  `feed_like_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_seq` int(10) unsigned DEFAULT NULL,
  `user_seq` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`feed_like_seq`),
  KEY `FKning8mqcj0wbttajbretyf9qh` (`feed_seq`),
  KEY `FK8kps7bnmj4ek2gxc6ec996shd` (`user_seq`),
  CONSTRAINT `FK8kps7bnmj4ek2gxc6ec996shd` FOREIGN KEY (`user_seq`) REFERENCES `user` (`seq`),
  CONSTRAINT `FKning8mqcj0wbttajbretyf9qh` FOREIGN KEY (`feed_seq`) REFERENCES `feed` (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.feed_like:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `feed_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `feed_like` ENABLE KEYS */;

-- 테이블 sdutyplusdb.job 구조 내보내기
CREATE TABLE IF NOT EXISTS `job` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `job_name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.job:~6 rows (대략적) 내보내기
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` (`seq`, `job_name`) VALUES
	(1, '학생'),
	(2, '공무원'),
	(3, '개발자'),
	(4, '회사원'),
	(5, '연구원'),
	(6, '기타');
/*!40000 ALTER TABLE `job` ENABLE KEYS */;

-- 테이블 sdutyplusdb.jwt 구조 내보내기
CREATE TABLE IF NOT EXISTS `jwt` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `access_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `refresh_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_seq` bigint(20) NOT NULL,
  PRIMARY KEY (`seq`),
  UNIQUE KEY `UK_t1lwcqvxvqnbxdy7n3fhjmmkw` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.jwt:~15 rows (대략적) 내보내기
/*!40000 ALTER TABLE `jwt` DISABLE KEYS */;
INSERT INTO `jwt` (`seq`, `access_token`, `refresh_token`, `user_seq`) VALUES
	(17, 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIxNiIsImlhdCI6MTY2ODEzOTI5OCwiZXhwIjoxNjY5MzQ4ODk4fQ.xRUlngBg-zUEgv79tOWJ_8nFS9yD9hPsLO4L5BqDYIIp5v9nbEVA__m759ghS3nAvZelexTFOCQbvUq1mXFBFw', 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIxNiIsImlhdCI6MTY2ODEzOTI5OCwiZXhwIjoxNjY5MzQ4ODk4fQ.xRUlngBg-zUEgv79tOWJ_8nFS9yD9hPsLO4L5BqDYIIp5v9nbEVA__m759ghS3nAvZelexTFOCQbvUq1mXFBFw', 16),
	(39, 'eyJraWQiOiJrZXkyIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIzOCIsImlhdCI6MTY2ODk2MDkwOCwiZXhwIjoxNjcwNzc1MzA4fQ.TievGUuXtAwcl9ZJMJVGfGKHhyHy7OA3R6XSbyWDYgYdG0-r8Ibz6ZgFMk73kkzAGouT8twKQiOnE93Ax97c-g', 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIzOCIsImlhdCI6MTY2ODk2MDkwOCwiZXhwIjoxNjcwNzc1MzA4fQ.S2jMnJdCxF1LkGfTwSlnADtooY-BUB_a0OQ6U1EblYtAZt7JIWbLT5yzHv2YSUM1UxdSjbut0XX8tZsdtuArtw', 38),
	(41, 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI0MCIsImlhdCI6MTY2ODc3NjU4MSwiZXhwIjoxNjcwNTkwOTgxfQ.Q0kPEf33P2T1txgfgCMoHgDP7S7u1fssLDTrCgEJpynkA1YlXtmzqopid7oaszY9k6yP7g4mtVFhjPumFneGYQ', 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI0MCIsImlhdCI6MTY2ODc3NjU4MSwiZXhwIjoxNjcwNTkwOTgxfQ.bs5XeDKdpp9KrUEmPZ04Za6DST6GNG924_AyzYe3-Azeu2JVzMghwnPxNeuXmNWqDueBXzgTC82JY16d4qcfcw', 40),
	(42, 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI0MSIsImlhdCI6MTY2ODQ4NDYyNywiZXhwIjoxNjY5Njk0MjI3fQ.f7dggG83tYDKkn0yqBa08HZ1cTcQ0hBjT9P3L1al4cM9iJd3nlRBcF5TULJc9mJRh--2Idv7t36FZ1m8yYBzLA', 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI0MSIsImlhdCI6MTY2ODQ4NDYyNywiZXhwIjoxNjY5Njk0MjI3fQ.mT_-RI8JKzYz7d-xkHO7EuGhOuKCwbJTNIZHcgXbhPR8jVYv2TYgpqqxtb3sJSyfWP-WaxAdikrQh_qLOSo16A', 41),
	(44, 'eyJraWQiOiJrZXkyIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI0MyIsImlhdCI6MTY2ODY0NjcwMywiZXhwIjoxNjY5ODU2MzAzfQ.NKr98z38MQDtagyQtObAPNrIU0p80xjX-pNd7DVQPQ2oF_rMV-qtyLk9ADnjhNIc5g6IeSxHpGhKNq6r5HmrvA', 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI0MyIsImlhdCI6MTY2ODY0NjcwMywiZXhwIjoxNjY5ODU2MzAzfQ._9UD-PliosLx2oQYeYa-25_6GAt_6a-3uUxwWLdBEvWNRuQ2Fuzj0Rgp91dR5f50cVChohGGIENgWAxUuRTVpw', 43),
	(45, 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI0NCIsImlhdCI6MTY2ODY0NjUwMiwiZXhwIjoxNjY5ODU2MTAyfQ.hTGkzmQZRNW4XKT_fg778rLxl1pSu3ECLQr75oD0-yjs_gnj1LqUR7XkixP23booiNZdARfzGjEXSfQvKKC3fw', 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI0NCIsImlhdCI6MTY2ODY0NjUwMiwiZXhwIjoxNjY5ODU2MTAyfQ.MbuCX7kjhEO9bkSM9kV0pgz61VsKdF-nQuAVrBouKqbUBP8IBftGJumZJpWxnzan37-RzkFVEL1Kj6TWx77CMQ', 44),
	(46, 'eyJraWQiOiJrZXkyIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI0NSIsImlhdCI6MTY2ODY0OTg1NiwiZXhwIjoxNjY5ODU5NDU2fQ.81gfhcOb9zj0E4WUtrw-4WKxMBOQe2oYdAqnH-wDUppwHCM3J8u4gtqhNyWqPXduZDw-JgstVdclfh8PJVhcPA', 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI0NSIsImlhdCI6MTY2ODY0OTg1NiwiZXhwIjoxNjY5ODU5NDU2fQ.E0gC-B6aXW7yUPYpEF7nQphSaP0f_JjyJVSfCKXcOKHD9rtcpdrVCx3bwQqPaWbnFmsHHefK_Pdul7MIA5c4hQ', 45),
	(52, 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI1MSIsImlhdCI6MTY2ODY2NTgyNiwiZXhwIjoxNjY5ODc1NDI2fQ.1kyEMTpiGdYfmySgzDOUw5mYJ8-rhi83pm43Aic0LJgV4N8DJJqzw0B41w0E_iGq434anopPDeNnHDuFUc6arQ', 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI1MSIsImlhdCI6MTY2ODY2NTgyNiwiZXhwIjoxNjY5ODc1NDI2fQ.NkjQFnfc76-FdkC6UfJrPB-bLYd-eyNxu0PNqzx3-F4zkJiFCBCsPjFQJ35JZPWXvWNEhBTz4E9Lw-CyxpPauA', 51),
	(55, 'eyJraWQiOiJrZXkyIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI1NCIsImlhdCI6MTY2ODk2NDY0NiwiZXhwIjoxNjcwNzc5MDQ2fQ.KmjgaQTM57MdRBISgH4ZltHJi9V0xZhdqVHeC-jc-2gEugjFTWR1p5pz5gL09DUHdTULyaDN4Nl1uxTnktaFLw', 'eyJraWQiOiJrZXkyIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI1NCIsImlhdCI6MTY2ODk2NDY0NiwiZXhwIjoxNjcwNzc5MDQ2fQ.KmjgaQTM57MdRBISgH4ZltHJi9V0xZhdqVHeC-jc-2gEugjFTWR1p5pz5gL09DUHdTULyaDN4Nl1uxTnktaFLw', 54),
	(59, 'eyJraWQiOiJrZXkyIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI1OCIsImlhdCI6MTY2ODczNzQ0MSwiZXhwIjoxNjY5OTQ3MDQxfQ.5ETjKporirVYxgDUDbS_1af9dH9J3Wy9Szc35pa3BmJ_iEaS36H9w2SfkZtIDwfQqDIvtYI45DhMQCJohOYSZg', 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI1OCIsImlhdCI6MTY2ODczNzQ0MSwiZXhwIjoxNjY5OTQ3MDQxfQ.qB5Cav0VpLIY9-YBLYBGSmWS-_n-XUXXIkcJezaEtOnJlD3JUNDFIoCsXhL1iW8NaorD3IIFix5wGfgq2EZSew', 58),
	(67, 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI2NiIsImlhdCI6MTY2ODc3NDg4OCwiZXhwIjoxNjcwNTg5Mjg4fQ.a4Ixd_BKYDLSRlOv9O9TIvJPc9ZQuPWmn9qHVcgEB2HIxznApD4MmBYD220ikmBNnsHgcRg9sKoSiB7EjfX27A', 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI2NiIsImlhdCI6MTY2ODc3NDg4OCwiZXhwIjoxNjcwNTg5Mjg4fQ.3-iZpVANxhIrUdkZ_vRhmlANdbBfKEn0hG_-yaFpU6RxteYVmLQ28uQhaG1lMLwhrys1ie5gSQaUtnjcSoiXdg', 66),
	(70, 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI2OSIsImlhdCI6MTY2ODk1NzM4MSwiZXhwIjoxNjcwNzcxNzgxfQ.Wd-xPcV4bGcnjGBg6fYzvZtywpsH7W5Igw_upI50NbhmbSnAuglfK8ouW7aAZiuQ1WRnedFMzqPFOomIBRiAvA', 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI2OSIsImlhdCI6MTY2ODk1NzM4MSwiZXhwIjoxNjcwNzcxNzgxfQ.2nbBZik8A1-5_qGyLTxLnMMddhsoDtWytxuNH1qUCyNONh4a90bhH72wWGNiBkNRr3heD9eqlqy91Ooh26NTcA', 69),
	(72, 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI3MSIsImlhdCI6MTY2ODkyMjc2MiwiZXhwIjoxNjcwNzM3MTYyfQ.ZsYreBnOeXyAODGzGilX3r7jVgM5rRy-L4nRpv6AhWsVvE5k9kSJBRlG607Adqf5_xoKePvlZngSP_XhEptM8A', 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI3MSIsImlhdCI6MTY2ODkyMjc2MiwiZXhwIjoxNjcwNzM3MTYyfQ.7NIpgdj_73sH5fLvs6AXYkePmyNSMFrlRAucRci3Bc5kr4w-SpmdmcvZvNKLUzKLJR_W2fZCXUAT8UYqBEA8RQ', 71),
	(73, 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI3MiIsImlhdCI6MTY2ODk2NDQ0MCwiZXhwIjoxNjcwNzc4ODQwfQ.7wBLdtjOtMCRAciiSWbDmlHqqjWhvsQrG-K9z4TgSSkd7apqNwp3gDDTppmt41ypV28ITstDTVamdqba_OH8_g', 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI3MiIsImlhdCI6MTY2ODk2NDQ0MCwiZXhwIjoxNjcwNzc4ODQwfQ.7wBLdtjOtMCRAciiSWbDmlHqqjWhvsQrG-K9z4TgSSkd7apqNwp3gDDTppmt41ypV28ITstDTVamdqba_OH8_g', 72),
	(75, 'eyJraWQiOiJrZXkxIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI3NCIsImlhdCI6MTY2ODk2MTE2OSwiZXhwIjoxNjcwNzc1NTY5fQ.l9FZZY2RQ9AX5U04a5mYN6WdKGyXN9iFEHNzb0f_XC2o_6BBHf6P0Lo8g5-yC0NYgyHh7j0RFuytcu_GPsLJvA', 'eyJraWQiOiJrZXkzIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiI3NCIsImlhdCI6MTY2ODk2MTE2OSwiZXhwIjoxNjcwNzc1NTY5fQ.lyC7InB2tk1vyZSObZSYzkZg6A8AWq1_YdHJDfWM7lH5kc6vQw2SxaK8Jy6h8IbzwolYcPZB3u0SaJySM49qcQ', 74);
/*!40000 ALTER TABLE `jwt` ENABLE KEYS */;

-- 테이블 sdutyplusdb.off_feed 구조 내보내기
CREATE TABLE IF NOT EXISTS `off_feed` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `feed_seq` int(10) unsigned DEFAULT NULL,
  `user_seq` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`seq`),
  KEY `FKl2tw5gpgq61nybc78b6ngst7t` (`feed_seq`),
  KEY `FKqecm6o8namdsyij834qpxe7a6` (`user_seq`),
  CONSTRAINT `FKl2tw5gpgq61nybc78b6ngst7t` FOREIGN KEY (`feed_seq`) REFERENCES `feed` (`seq`),
  CONSTRAINT `FKqecm6o8namdsyij834qpxe7a6` FOREIGN KEY (`user_seq`) REFERENCES `user` (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.off_feed:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `off_feed` DISABLE KEYS */;
/*!40000 ALTER TABLE `off_feed` ENABLE KEYS */;

-- 테이블 sdutyplusdb.off_user 구조 내보내기
CREATE TABLE IF NOT EXISTS `off_user` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `from_user_seq` int(10) unsigned DEFAULT NULL,
  `to_user_seq` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`seq`),
  KEY `FKpyuyk11qd8ahdncvflsgiiyb5` (`from_user_seq`),
  KEY `FKi903est246bnpf2teh2y48hxb` (`to_user_seq`),
  CONSTRAINT `FKi903est246bnpf2teh2y48hxb` FOREIGN KEY (`to_user_seq`) REFERENCES `user` (`seq`),
  CONSTRAINT `FKpyuyk11qd8ahdncvflsgiiyb5` FOREIGN KEY (`from_user_seq`) REFERENCES `user` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.off_user:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `off_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `off_user` ENABLE KEYS */;

-- 테이블 sdutyplusdb.scrap 구조 내보내기
CREATE TABLE IF NOT EXISTS `scrap` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `feed_seq` int(10) unsigned DEFAULT NULL,
  `user_seq` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`seq`),
  KEY `FKf7wddu8ws19lairlkqk0qrkpu` (`feed_seq`),
  KEY `FKdqohmii1gb4scdovqjo6jg2qb` (`user_seq`),
  CONSTRAINT `FKdqohmii1gb4scdovqjo6jg2qb` FOREIGN KEY (`user_seq`) REFERENCES `user` (`seq`),
  CONSTRAINT `FKf7wddu8ws19lairlkqk0qrkpu` FOREIGN KEY (`feed_seq`) REFERENCES `feed` (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.scrap:~14 rows (대략적) 내보내기
/*!40000 ALTER TABLE `scrap` DISABLE KEYS */;
INSERT INTO `scrap` (`seq`, `feed_seq`, `user_seq`) VALUES
	(3, 34, 38),
	(58, 5260, 69),
	(63, 5267, 69),
	(68, 5287, 38),
	(69, 5262, 38),
	(70, 5267, 38),
	(71, 5280, 38),
	(84, 5260, 38),
	(85, 5287, 74),
	(86, 5267, 74),
	(87, 5262, 74),
	(88, 5260, 74),
	(91, 5287, 40),
	(96, 5285, 69);
/*!40000 ALTER TABLE `scrap` ENABLE KEYS */;

-- 테이블 sdutyplusdb.sub_task 구조 내보내기
CREATE TABLE IF NOT EXISTS `sub_task` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `task_seq` int(10) unsigned NOT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=384 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.sub_task:~22 rows (대략적) 내보내기
/*!40000 ALTER TABLE `sub_task` DISABLE KEYS */;
INSERT INTO `sub_task` (`seq`, `content`, `task_seq`) VALUES
	(263, '슬라이드 8까지 했다..!!', 222),
	(264, '슬라이드 21..!', 223),
	(265, '기술적 내용은 들어가는 이미지테 따라서 설명이 달라질 것 같아서 대본은 못 적음..!', 226),
	(335, '이제 다시 초안 잡는 걸로 돌아가자', 272),
	(346, 'BSH test', 286),
	(348, 'BSH test', 288),
	(349, 'BSH test', 289),
	(350, '싱기하담', 291),
	(351, '이제 조금 쉴게요...', 296),
	(356, '뭐지', 302),
	(364, '프로젝트의 굴레...', 311),
	(369, '테스트좀용', 319),
	(370, '테스트좀용', 320),
	(371, '테스트좀용', 321),
	(374, '안 끝났다아아아아아아', 314),
	(375, '2초', 315),
	(376, '열심히 하자', 317),
	(377, '냐옹', 326),
	(379, '냐옹냐옹', 328),
	(381, '1개 제대로 외우기', 332),
	(382, 'ㄱ', 338),
	(383, 'ㅂ', 346);
/*!40000 ALTER TABLE `sub_task` ENABLE KEYS */;

-- 테이블 sdutyplusdb.task 구조 내보내기
CREATE TABLE IF NOT EXISTS `task` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `duration_time` int(11) NOT NULL,
  `end_time` datetime(6) NOT NULL,
  `owner_seq` int(10) unsigned NOT NULL,
  `start_time` datetime(6) NOT NULL,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=348 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.task:~53 rows (대략적) 내보내기
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` (`seq`, `duration_time`, `end_time`, `owner_seq`, `start_time`, `title`) VALUES
	(197, 1800000, '2022-11-18 10:30:00.000000', 54, '2022-11-18 10:00:00.000000', '팀 일정 정리'),
	(198, 1500000, '2022-11-18 11:25:00.000000', 54, '2022-11-18 11:00:00.000000', 'Tag Dialog 코드 수정'),
	(199, 1680000, '2022-11-18 10:59:00.000000', 54, '2022-11-18 10:31:00.000000', '동의서 제출 및 일정 메모'),
	(200, 665000, '2022-11-18 13:53:54.000000', 54, '2022-11-18 13:42:49.000000', 'Tag Dialog 코드 수정'),
	(201, 2400000, '2022-11-18 15:40:00.000000', 54, '2022-11-18 15:00:00.000000', 'Tag Dialog 코드 분석'),
	(202, 3000000, '2022-11-18 16:50:00.000000', 54, '2022-11-18 16:00:00.000000', 'timer, report ui 수정'),
	(222, 1518000, '2022-11-19 01:05:24.000000', 54, '2022-11-19 00:40:06.000000', 'PPT 대본 및 내용 초안 작성'),
	(223, 2773000, '2022-11-19 02:15:38.000000', 54, '2022-11-19 01:29:25.000000', 'PPT 내용 및 대본 추가'),
	(225, 600000, '2022-11-19 02:30:00.000000', 54, '2022-11-19 02:20:00.000000', 'sduty bug 메모'),
	(226, 1980000, '2022-11-19 03:23:00.000000', 54, '2022-11-19 02:50:00.000000', 'PPT 대본 및 내용 초안 작성'),
	(227, 1140000, '2022-11-19 03:44:00.000000', 54, '2022-11-19 03:25:00.000000', 'PPT 조사할 자료 및 일정 정리'),
	(254, 3000000, '2022-11-19 15:55:00.000000', 54, '2022-11-19 15:05:00.000000', '일정체크, PPT 내용 체크'),
	(258, 539000, '2022-11-19 16:35:47.000000', 54, '2022-11-19 16:26:48.000000', '자료찾기'),
	(270, 1114000, '2022-11-19 16:59:26.000000', 54, '2022-11-19 16:40:52.000000', 'PPT 디자인 참고 자료'),
	(272, 3600000, '2022-11-19 18:00:00.000000', 54, '2022-11-19 17:00:00.000000', 'PPT 참고 자료 계속 보는 중'),
	(283, 2274000, '2022-11-19 21:14:02.000000', 54, '2022-11-19 20:36:08.000000', 'PPT 디자인 다듬기 시작'),
	(284, 2249000, '2022-11-19 22:07:51.000000', 54, '2022-11-19 21:30:22.000000', 'PPT 디자인 다듬기'),
	(285, 720000, '2022-11-19 22:22:00.000000', 54, '2022-11-19 22:10:00.000000', 'PPT 디자인 다듬기'),
	(286, 600000, '2022-11-19 22:40:00.000000', 54, '2022-11-19 22:30:00.000000', 'BSH test'),
	(290, 1620000, '2022-11-19 23:08:00.000000', 54, '2022-11-19 22:41:00.000000', 'PPT 디자인 다듬기'),
	(291, 3000, '2022-11-19 23:24:07.000000', 70, '2022-11-19 23:24:04.000000', '테스투'),
	(292, 1511000, '2022-11-19 23:38:47.000000', 54, '2022-11-19 23:13:36.000000', 'PPT 디자인 다듬기'),
	(294, 4200000, '2022-11-20 02:20:00.000000', 54, '2022-11-20 01:10:00.000000', 'PPT 디자인 다듬기'),
	(295, 3780000, '2022-11-20 03:53:00.000000', 54, '2022-11-20 02:50:00.000000', 'PPT 디자인 다듬기'),
	(296, 3480000, '2022-11-20 05:28:00.000000', 54, '2022-11-20 04:30:00.000000', 'PPT 디자인 다듬기'),
	(300, 1895000, '2022-11-20 15:41:45.000000', 54, '2022-11-20 15:10:10.000000', '일정 체크...'),
	(301, 2607000, '2022-11-20 16:31:32.000000', 54, '2022-11-20 15:48:05.000000', 'PPT 최종을 향해'),
	(304, 3900000, '2022-11-20 18:05:00.000000', 54, '2022-11-20 17:00:00.000000', 'PPT 최종을 향해 + 일정 체크'),
	(305, 1380000, '2022-11-20 20:28:00.000000', 54, '2022-11-20 20:05:00.000000', '일정 논의'),
	(310, 10000, '2022-11-20 20:48:18.000000', 72, '2022-11-20 20:48:08.000000', '공부 시작!'),
	(311, 12960000, '2022-11-20 18:49:00.000000', 72, '2022-11-20 15:13:00.000000', '자율 마무리'),
	(313, 4620000, '2022-11-20 22:27:00.000000', 54, '2022-11-20 21:10:00.000000', 'PPT 최종을 향하여'),
	(314, 58000, '2022-11-20 23:11:36.000000', 69, '2022-11-20 23:10:38.000000', '자율 시연 연습'),
	(315, 2000, '2022-11-20 23:12:40.000000', 69, '2022-11-20 23:12:38.000000', '자율 시연 연습 2트'),
	(316, 2846000, '2022-11-20 23:17:43.000000', 54, '2022-11-20 22:30:17.000000', 'PPT 최종을 향해'),
	(317, 2000, '2022-11-20 23:21:32.000000', 69, '2022-11-20 23:21:30.000000', '덕몽어스 '),
	(318, 1380000, '2022-11-20 19:43:00.000000', 69, '2022-11-20 19:20:00.000000', '화이팅'),
	(324, 8000, '2022-11-21 00:31:08.000000', 69, '2022-11-21 00:31:00.000000', '자율 공부'),
	(325, 1860000, '2022-11-21 00:32:00.000000', 72, '2022-11-21 00:01:00.000000', '수학공부'),
	(326, 69000, '2022-11-21 00:37:23.000000', 38, '2022-11-21 00:36:14.000000', '고양이'),
	(328, 93000, '2022-11-21 00:43:44.000000', 38, '2022-11-21 00:42:11.000000', '고양이 탐구'),
	(329, 47000, '2022-11-21 00:46:04.000000', 72, '2022-11-21 00:45:17.000000', '국어잠깐봄'),
	(331, 10000, '2022-11-21 01:12:55.000000', 69, '2022-11-21 01:12:45.000000', '언제자나요'),
	(332, 26000, '2022-11-21 01:13:35.000000', 73, '2022-11-21 01:13:09.000000', '영단어 1개 외우기'),
	(333, 3780000, '2022-11-20 12:25:41.000000', 69, '2022-11-20 11:22:41.000000', '버거킹 제조법 공부'),
	(334, 4740000, '2022-11-21 01:20:00.000000', 54, '2022-11-21 00:01:00.000000', '자율 이제 좀 끝내고 싶다'),
	(335, 13000, '2022-11-21 01:21:01.000000', 54, '2022-11-21 01:20:48.000000', '엉엉'),
	(336, 7000, '2022-11-21 01:35:05.000000', 69, '2022-11-21 01:34:58.000000', '21트째... 녹화'),
	(338, 3900000, '2022-11-21 01:44:00.000000', 40, '2022-11-21 00:39:00.000000', 'ㄱ'),
	(342, 1380000, '2022-11-21 01:53:00.000000', 54, '2022-11-21 01:30:00.000000', '산출물 확인 중...'),
	(345, 9000, '2022-11-21 01:55:55.000000', 69, '2022-11-21 01:55:46.000000', '프로젝트 종료 우와아아아앙!'),
	(346, 840000, '2022-11-21 02:13:00.000000', 40, '2022-11-21 01:59:00.000000', 'ㅂ'),
	(347, 4440000, '2022-11-21 02:13:00.000000', 72, '2022-11-21 00:59:00.000000', '새벽 공부 중');
/*!40000 ALTER TABLE `task` ENABLE KEYS */;

-- 테이블 sdutyplusdb.user 구조 내보내기
CREATE TABLE IF NOT EXISTS `user` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ban_yn` tinyint(1) DEFAULT 0,
  `continuous` bigint(20) DEFAULT 0,
  `del_yn` tinyint(1) DEFAULT 0,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fcm_token` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `img_url` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_report` date DEFAULT '0000-00-00',
  `nickname` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reg_time` datetime(6) DEFAULT NULL,
  `social_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `job` int(10) unsigned DEFAULT NULL,
  `study_time` bigint(20) DEFAULT 0,
  `study_percent` bigint(20) DEFAULT 0,
  PRIMARY KEY (`seq`),
  UNIQUE KEY `UK_n4swgcf30j6bmtb4l4cjryuym` (`nickname`),
  KEY `FKjggs8pb6h5k4hebahv0ih4w3o` (`job`),
  CONSTRAINT `FKjggs8pb6h5k4hebahv0ih4w3o` FOREIGN KEY (`job`) REFERENCES `job` (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.user:~14 rows (대략적) 내보내기
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`seq`, `ban_yn`, `continuous`, `del_yn`, `email`, `fcm_token`, `img_url`, `last_report`, `nickname`, `reg_time`, `social_type`, `job`, `study_time`, `study_percent`) VALUES
	(38, 0, 1, 0, 'toy9910@naver.com', NULL, 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/profile%2F%EC%9D%B4%EB%A6%AC%EB%AF%B8%EB%94%9C%EB%A6%AC123456?alt=media&token=b08bf67c-596d-41cc-a457-5fdb609baeda', '2022-11-21', '고양이는냐옹냐옹', '2022-11-14 07:51:55.439627', 'KAKAO', 2, 0, 0),
	(40, 0, 3, 0, 'wjddbs3918@nexon.com', NULL, 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/profile%2F%EA%B3%B5%EB%B6%80%EA%B0%80%20%EC%89%AC%EC%9B%8C%EC%9A%94?alt=media&token=632ec7f6-03f5-4703-850d-c04b2e230805', '2022-11-21', '공부가 쉬워요', '2022-11-14 17:14:15.752623', 'NAVER', 1, 2, 0),
	(41, 0, 0, 0, 'rxplus2019rxplus2019@gmail.com', NULL, NULL, '2022-11-15', 'johnsmith', '2022-11-15 03:56:42.388563', 'KAKAO', 1, 0, 0),
	(43, 0, 0, 0, 'candle1126@kakao.com', NULL, NULL, '2022-11-17', NULL, '2022-11-17 00:45:42.866305', 'KAKAO', NULL, 0, 0),
	(44, 0, 0, 0, 'djsflsdudn57@naver.com', NULL, NULL, '2022-11-17', NULL, '2022-11-17 00:55:02.379806', 'KAKAO', NULL, 0, 0),
	(45, 0, 0, 0, 'sduty123@naver.com', NULL, 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/profile%2Fsduty123?alt=media&token=de2ac4b5-dcdc-4436-a619-37803ccb0b58', '2022-11-17', 'sduty123', '2022-11-17 01:46:15.126429', 'NAVER', 2, 0, 0),
	(51, 0, 1, 0, 'qotlgus123@naver.com', NULL, '/data/user/0/com.d205.sdutyplus/cache/temp_file_20221117_151731.jpg', '2022-11-17', '배한용', '2022-11-17 06:17:06.950710', 'KAKAO', 6, 0, 0),
	(54, 0, 4, 0, 'nhee0410@naver.com', NULL, 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/profile%2F%EC%9E%90%EC%9C%A8%EB%81%9D%EB%82%98%EC%A4%98%EC%A0%9C%EB%B0%9C?alt=media&token=274d2b5f-84c1-4c93-935a-ee5e53b28e12', '2022-11-21', '자율끝나줘제발', '2022-11-17 07:10:53.858554', 'NAVER', 3, 6, 0),
	(58, 0, 1, 0, 'wkdrns3918@naver.com', NULL, 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/profile%2Fjytest?alt=media&token=2579097a-5956-4bec-bc0f-f112d7056999', '2022-11-20', 'Penguin', '2022-11-17 07:51:32.416514', 'KAKAO', 1, 0, 0),
	(66, 0, 5, 0, 'mywayjh15@gmail.com', NULL, 'asd', '2022-11-18', '왜실패함', '2022-11-18 12:32:38.852379', 'KAKAO', 1, 0, 0),
	(69, 0, 2, 0, 'woway2488@naver.com', NULL, 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/profile%2FOhMyGod?alt=media&token=0927d7ad-0619-4ba4-ad59-29be0dd3bb52', '2022-11-21', 'CODE:낭만', '2022-11-18 14:18:23.800099', 'NAVER', 2, 1, 0),
	(71, 0, 1, 0, 'jiwoon1030@naver.com', NULL, 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/profile%2F%EA%BC%AC%EB%AA%A8?alt=media&token=7cb073b7-5104-4703-9c4a-367e18b2fbd5', '2022-11-20', '꼬모', '2022-11-20 05:39:22.206904', 'KAKAO', 1, 0, 0),
	(72, 0, 2, 0, 'nhee0410@naver.com', NULL, 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/profile%2F%EC%95%84%EC%9E%90%EC%9E%90%EC%9E%90%EC%9E%A3%F0%9F%94%A5?alt=media&token=aa30a8f8-58c3-41a1-bb48-9608d2ef2115', '2022-11-21', '김민정사랑해🔥', '2022-11-20 11:45:53.520431', 'KAKAO', 1, 3, 0),
	(74, 0, 0, 0, 'sduty123@gmail.com', NULL, 'https://firebasestorage.googleapis.com/v0/b/sdutyplus.appspot.com/o/profile%2F%EB%8B%90%EB%A6%AC%EB%A6%AC%EB%A7%98%EB%B3%B4?alt=media&token=2466014f-e098-411a-bf8b-4704a61c7579', '2022-11-20', '닐리리맘보', '2022-11-20 16:19:29.344130', 'KAKAO', 1, 0, 0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- 테이블 sdutyplusdb.warn_feed 구조 내보내기
CREATE TABLE IF NOT EXISTS `warn_feed` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `feed_seq` int(10) unsigned DEFAULT NULL,
  `user_seq` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`seq`),
  KEY `FKkah60ynvo3pyyok6k6q0irmdf` (`feed_seq`),
  KEY `FK4ty7iqdon061gyemnacjv891a` (`user_seq`),
  CONSTRAINT `FK4ty7iqdon061gyemnacjv891a` FOREIGN KEY (`user_seq`) REFERENCES `user` (`seq`),
  CONSTRAINT `FKkah60ynvo3pyyok6k6q0irmdf` FOREIGN KEY (`feed_seq`) REFERENCES `feed` (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.warn_feed:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `warn_feed` DISABLE KEYS */;
/*!40000 ALTER TABLE `warn_feed` ENABLE KEYS */;

-- 테이블 sdutyplusdb.warn_user 구조 내보내기
CREATE TABLE IF NOT EXISTS `warn_user` (
  `seq` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `from_user_seq` int(10) unsigned DEFAULT NULL,
  `to_user_seq` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`seq`),
  KEY `FKbthvyvnqx5i0kh4vjtlgej7fe` (`from_user_seq`),
  KEY `FK1e2wjrijlwewjebv7h7wvmr37` (`to_user_seq`),
  CONSTRAINT `FK1e2wjrijlwewjebv7h7wvmr37` FOREIGN KEY (`to_user_seq`) REFERENCES `user` (`seq`),
  CONSTRAINT `FKbthvyvnqx5i0kh4vjtlgej7fe` FOREIGN KEY (`from_user_seq`) REFERENCES `user` (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 sdutyplusdb.warn_user:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `warn_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `warn_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
