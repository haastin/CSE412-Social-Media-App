--User Posting a Photo

--New entry for photo (given photo info)
INSERT INTO Photos (aid, caption, url)
VALUES (?, ?, ?);


--User Creates Album

--New entry for album (given album info, dateCreated is populated with default value)
INSERT INTO Albums (uid, albumName)
Values (?, ?);


--User Posts Comment

--New entry for comment (given comment info, date is populated with default value)
INSERT INTO Comments (pid, uid, text)
VALUES (?, ?, ?);


--User Likes a Photo

--New entry for likes (given like info)
INSERT INTO Likes (pid, uid)
VALUES (?, ?);


--User Tags or Creates a Photo with a Tag

--New entry for tags (given tag info)
INSERT INTO Tags (pid, word) 
VALUES (?, ?);


--User Friends Another User

--New entry for friends (given friend info, whenFriends is populated with default value)
INSERT INTO Friends (uid, fid)
VALUES (?, ?); 

--User created in registration