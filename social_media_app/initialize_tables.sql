CREATE TABLE Users
(uid INT(4) NOT NULL AUTO_INCREMENT,
firstName VARCHAR(50) NOT NULL,
lastName VARCHAR(50) NOT NULL,
password VARCHAR(50) NOT NULL,
gender VARCHAR(1),
hometown VARCHAR(50),
email VARCHAR(50) NOT NULL,
dob DATE NOT NULL,
PRIMARY KEY(uid),
UNIQUE(email),
CHECK (REGEXP_LIKE(email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'))
);

CREATE TABLE Albums
(aid INT(4) NOT NULL AUTO_INCREMENT,
uid INT(4) NOT NULL,
albumName VARCHAR(50) NOT NULL,
dateCreated DATETIME DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(aid),
FOREIGN KEY(uid) REFERENCES Users(uid) ON DELETE CASCADE);

CREATE TABLE Photos
(pid INT(4) NOT NULL AUTO_INCREMENT,
aid INT(4) NOT NULL,
caption VARCHAR(240),
url VARCHAR(100) NOT NULL,
PRIMARY KEY (pid),
FOREIGN KEY(aid) REFERENCES Albums(aid) ON DELETE CASCADE);

CREATE TABLE Likes
(pid INT(4) NOT NULL,
uid INT(4) NOT NULL,
PRIMARY KEY(pid, uid),
FOREIGN KEY(pid) REFERENCES Photos(pid) ON DELETE CASCADE,
FOREIGN KEY(uid) REFERENCES Users(uid) ON DELETE CASCADE);

CREATE TABLE Comments 
(cid INT(4) NOT NULL AUTO_INCREMENT,
pid INT(4) NOT NULL,
uid INT(4) NOT NULL,
date DATETIME DEFAULT CURRENT_TIMESTAMP,
text VARCHAR(240),
PRIMARY KEY(cid),
FOREIGN KEY(pid) REFERENCES Photos(pid) ON DELETE CASCADE,
FOREIGN KEY(uid) REFERENCES Users(uid) ON DELETE CASCADE);

CREATE TABLE Friends
(uid INT(4) NOT NULL,
whenFriends DATETIME DEFAULT CURRENT_TIMESTAMP,
fid INT(4) NOT NULL,
PRIMARY KEY(uid, fid),
FOREIGN KEY(uid) REFERENCES Users(uid) ON DELETE CASCADE,
FOREIGN KEY(fid) REFERENCES Users(uid) ON DELETE CASCADE);

CREATE TABLE Tags
(pid INT(4) NOT NULL,
word VARCHAR(20) NOT NULL,
PRIMARY KEY(pid, word),
FOREIGN KEY(pid) REFERENCES Photos(pid) ON DELETE CASCADE,
CHECK (REGEXP_LIKE(word, '^[a-z]+$')));

-- 70 INSERT INTO queries here

INSERT INTO Users (uid, firstName, lastName, password, gender, hometown, email, dob)
VALUES 
(1450, 'Shiro', 'Yoshiro', 'pass1', 'F', 'Misaki', 'syosh50@gmail.com', '1999-07-03'),
(1455, 'Fujikawa', 'Haru', 'pass2', 'F', 'Tokyo', 'hfuji55@gmail.com', '2001-04-06'),
(1460, 'Sato', 'Hiroshi', 'pass3', 'M', 'Kyoto', 'shiro60@gmail.com', '2005-03-30'),
(1465, 'Ogawa', 'Yukio', 'pass4', 'F', 'Otaru', 'oyuki65@gmail.com', '1999-12-17'),
(1470, 'Nanako', 'Shin', 'pass5', 'O', 'Taishi', 'nshin70@gmail.com', '2004-08-23'),
(1700, 'Amari', 'Daisuke', 'pass6', 'M', 'Misaki', 'adais00@gmail.com', '2002-11-12'),
(1705, 'Nakao', 'Asami', 'pass7', 'F', 'Kyoto', 'nasam05@gmail.com', '2002-02-07'),
(1710, 'Tanji', 'Ryota', 'pass8', 'M', 'Osaka', 'tryot10@gmail.com', '2003-06-15'),
(1715, 'Jin', 'Yasuo', 'pass9', 'O', 'Tokyo', 'jyasu15@gmail.com', '1998-04-20'),
(1720, 'Yamada', 'Michi', 'pass10', 'M', 'Kyoto', 'ymich20@gmail.com', '2004-03-09');


INSERT INTO Albums (aid, uid, albumName, dateCreated) VALUES
(4560, 1450, 'Selfies', '2023-04-17'),
(4565, 1470, 'Family', '2017-12-05'),
(4570, 1705, 'Clubs', '2022-09-29'),
(4575, 1460, 'Favorites', '2015-10-14'),
(4580, 1700, 'Beach', '2017-12-05'),
(5210, 1705, 'Home', '2023-04-17'),
(5215, 1465, 'Sports', '2023-02-01'),
(5220, 1700, 'Vacation', '2022-03-15'),
(5225, 1705, 'Friends', '2015-10-14'),
(5230, 1710, 'Nature', '2016-11-03');

INSERT INTO Photos (pid, aid, caption, url)
VALUES
(9975, 4560, 'slaying', 'slay'),
(9980, 4565, 'with my besties', 'wmyb'),
(9985, 4570, 'golden hour', 'gold'),
(9990, 4575, 'done with school', 'dwsc'),
(9995, 4580, 'happy holidays', 'holi'),
(9020, 5210, 'great summer', 'summ'),
(9025, 5215, 'smiles', 'smil'),
(9030, 5220, 'sports gang', 'spor'),
(9035, 5225, 'on vacation', 'vaca'),
(9040, 5230, 'gaming', 'game');

INSERT INTO Likes (pid, uid) VALUES (9975, 1450);
INSERT INTO Likes (pid, uid) VALUES (9980, 1455);
INSERT INTO Likes (pid, uid) VALUES (9985, 1460);
INSERT INTO Likes (pid, uid) VALUES (9990, 1465);
INSERT INTO Likes (pid, uid) VALUES (9995, 1470);
INSERT INTO Likes (pid, uid) VALUES (9020, 1700);
INSERT INTO Likes (pid, uid) VALUES (9025, 1705);
INSERT INTO Likes (pid, uid) VALUES (9030, 1710);
INSERT INTO Likes (pid, uid) VALUES (9035, 1715);
INSERT INTO Likes (pid, uid) VALUES (9040, 1720);

INSERT INTO Comments (cid, pid, uid, date, text)
VALUES (6570, 9975, 1450, '2018-05-06', 'you look amazing');

INSERT INTO Comments (cid, pid, uid, date, text)
VALUES (6120, 9980, 1455, '2019-08-11', 'awesome photos');

INSERT INTO Comments (cid, pid, uid, date, text)
VALUES (6680, 9985, 1460, '2016-01-09', 'wish i was there');

INSERT INTO Comments (cid, pid, uid, date, text)
VALUES (6710, 9990, 1465, '2016-01-22', 'gang gang');

INSERT INTO Comments (cid, pid, uid, date, text)
VALUES (6000, 9995, 1470, '2023-02-10', 'you look so good');

INSERT INTO Comments (cid, pid, uid, date, text)
VALUES (6900, 9020, 1700, '2022-11-01', 'beautiful memories');

INSERT INTO Comments (cid, pid, uid, date, text)
VALUES (7900, 9025, 1705, '2022-06-13', 'congratulations');

INSERT INTO Comments (cid, pid, uid, date, text)
VALUES (7610, 9030, 1710, '2021-07-07', 'so great');

INSERT INTO Comments (cid, pid, uid, date, text)
VALUES (7570, 9035, 1715, '2023-05-05', 'what a blast');

INSERT INTO Comments (cid, pid, uid, date, text)
VALUES (7180, 9040, 1720, '2017-04-14', 'same here');

INSERT INTO Friends (uid, whenFriends, fid) VALUES (1450, '2023-06-05', 1455);
INSERT INTO Friends (uid, whenFriends, fid) VALUES (1455, '2022-11-11', 1460);
INSERT INTO Friends (uid, whenFriends, fid) VALUES (1460, '2021-03-04', 1465);
INSERT INTO Friends (uid, whenFriends, fid) VALUES (1465, '2020-09-04', 1720);
INSERT INTO Friends (uid, whenFriends, fid) VALUES (1470, '2020-12-01', 1715);
INSERT INTO Friends (uid, whenFriends, fid) VALUES (1700, '2022-08-08', 1470);
INSERT INTO Friends (uid, whenFriends, fid) VALUES (1705, '2015-10-14', 1720);
INSERT INTO Friends (uid, whenFriends, fid) VALUES (1710, '2016-07-19', 1705);
INSERT INTO Friends (uid, whenFriends, fid) VALUES (1715, '2023-01-01', 1710);
INSERT INTO Friends (uid, whenFriends, fid) VALUES (1720, '2022-01-01', 1715);

INSERT INTO Tags (pid, word) VALUES (9975, 'wild');
INSERT INTO Tags (pid, word) VALUES (9980, 'buds');
INSERT INTO Tags (pid, word) VALUES (9985, 'cool');
INSERT INTO Tags (pid, word) VALUES (9990, 'friend');
INSERT INTO Tags (pid, word) VALUES (9995, 'best');
INSERT INTO Tags (pid, word) VALUES (9020, 'light');
INSERT INTO Tags (pid, word) VALUES (9025, 'sunny');
INSERT INTO Tags (pid, word) VALUES (9030, 'happy');
INSERT INTO Tags (pid, word) VALUES (9035, 'life');
INSERT INTO Tags (pid, word) VALUES (9040, 'viral');








