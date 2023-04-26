--User Posting a Photo

--New entry for photo (given photo info)
INSERT INTO Photos (aid, caption, url)
VALUES (?, ?, ?);


--User Adds a Friend(given uid of logged in user and uid of person they want to follow); whenFriends automaticallt filled
INSERT INTO Friends (uid, fid)
VALUES (?, ?)


--User Creates a Tag(given tag namme and photo to be tagged pid)
INSERT INTO Tags (word, pid)
VALUES (?, ?);


--User Creates Album

--New entry for album (given album info), dateCreated is filled in automatically 
INSERT INTO Albums (uid, albumName)
VALUES (?, ?);


--User Posts Comment

--New entry for comment (given comment info); date is filled in automatically
INSERT INTO Comments (pid, uid, text)
VALUES (?, ?, ?);


--User Likes a Photo

--New entry for likes (given pic pid to be liked and uid of logged in user)
INSERT INTO Likes (pid, uid)
VALUES (?, ?);

--User creation happens in registration sql file