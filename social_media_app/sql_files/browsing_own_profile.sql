
--Fetch all albums of the currently logged in user
SELECT aid, albumName, dateCreated
FROM Albums as a
WHERE a.uid = ? -- ? = uid of person logged in
GROUP BY a.uid

--Fetch all photos that belong to a specific album
SELECT p.url, p.caption
FROM Photos as p
WHERE p.aid = ? -- ? = aid of user’s album

--after fetching the photo pids from the album, can re-use the queries in render_photo to display to user

--Get photo pids if you want to browse your own photos outside of an album (and render them with render_photo queries)
SELECT pid
FROM Photos AS p, Albums AS a
WHERE p.aid = a.aid AND a.uid = ? 

--Get all of currently logged in user’s info (given logged in user’s uid)
SELECT *
FROM Users as u
WHERE u.uid = ?;
