--Get all of currently logged in user’s info (given logged in user’s uid); keep track of old vals
SELECT *
FROM Users as u
WHERE u.uid = ?;

/*Update user info (given logged in user’s uid what fields they want to change 
(not all fields may be changed by a user, in which case the old values are just passed in)*/

UPDATE Users AS u
SET firstName = ?, lastName = ?, gender = ?, hometown = ?, dob = ?
WHERE u.uid = ?;

--Update album (given user logged in's uid)
UPDATE Albums AS a
SET albumName = ?
WHERE a.uid = ?

--Update a pic(given user logged in's uid); can update a pic's caption and tags but not the pic itself
UPDATE Photos AS p
SET caption = ?
WHERE p.pid = ? AND p.aid IN
(SELECT a.aid
FROM Albums AS a
WHERE a.uid = ?)

--Update a comment
UPDATE Comments AS c
SET text = ?
WHERE c.uid = ?

--Update a tag if you want to change its word; otherwise delete it and/or create a new one 
UPDATE Tags as t
SET word = ?
WHERE t.pid = ? AND t.pid IN
(SELECT p.pid
FROM Photos as p, Albums as a
WHERE p.aid = a.aid AND a.uid = ?)