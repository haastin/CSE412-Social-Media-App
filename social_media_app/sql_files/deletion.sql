/*access control for deletion is handled either in the query or will be handled in the business logic*/

-- Delete a user (given uid of user logged in)
DELETE FROM Users
WHERE uid = ?;

-- Delete a photo (given pid)
DELETE FROM Photos AS p
WHERE p.pid = ? AND p.aid IN
(SELECT a.aid
FROM Albums AS a
WHERE a.uid = ?)

-- Delete an album (given aid)
DELETE FROM Albums
WHERE aid = ? AND uid = ?;

-- Delete a comment (given cid)
DELETE FROM Comments
WHERE cid = ? AND uid = ?;

-- Delete a like (given photo’s pid and uid of user logged in)
DELETE FROM Likes
WHERE uid = ? AND pid = ?;

-- Delete a user (given uid of user logged in and the uid of the person they want to unfollow)
DELETE FROM Friends
WHERE uid = ? and fid = ?;

-- Delete a tag (given photo’s pid and word)
DELETE FROM Tags as t
WHERE t.pid = ? AND t.pid IN
(SELECT p.pid
FROM Photos as p, Albums as a
WHERE p.aid = a.aid AND a.uid = ?)
