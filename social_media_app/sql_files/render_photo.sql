-- Fetch Photo URL and Caption for Homepage Photo (given photoID)
SELECT p.url, p.caption, p.pid
FROM Photos AS p
WHERE p.pid = ?;

-- Fetch user who posted photo (given photoID)
SELECT u.uid, u.firstName, u.lastName
FROM Users AS u
WHERE u.uid IN(
SELECT a.uid
FROM Albums as a
WHERE a.pid = ?);

-- Fetch User First and Last Name Who Liked Photo and number of likes the photo has(given photoID)
SELECT u.firstName, u.lastName, COUNT(*)
FROM Users AS u
WHERE u.uid IN
	(SELECT l.uid
	FROM Likes AS l
	WHERE l.pid = ?);

-- Retrieve comment text, date of creation, and first/last name of user who left comments on photo (given photoID)
SELECT u.firstName, u.lastName, c.text, c.date
FROM Users AS u, Comments AS c
WHERE c.pid = ? AND u.uid = c.uid;

-- Retrieve tags associated with photo (given photo id)
SELECT t.word, t.pid
FROM Tags as t
WHERE t.pid = ?;
