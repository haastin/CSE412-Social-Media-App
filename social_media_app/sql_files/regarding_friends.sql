--Find users that match first and last name when searching for friend (given first and last name)
SELECT u.uid
FROM Users AS u
WHERE u.firstName = ? AND u.lastName = ?;


--Fetch all friends of a user (given user id)
SELECT f.fid
FROM Friends AS f
WHERE f.uid = ?;


--Fetch all users that have this user ID as a friend
SELECT f.uid
FROM Friends AS f
WHERE f.fid = ?;


--Fetch all friends of friends of a user (given user id)
SELECT f2.uid, COUNT(*)
FROM Friends AS f2
WHERE f2.fid IN
	(SELECT f1.uid
	FROM Friends AS f1
	WHERE f1.fid = ?) 
AND f2.uid NOT IN
	(SELECT f1.uid
	FROM Friends AS f1
	WHERE f1.fid = ?)
GROUP BY f2.uid;


