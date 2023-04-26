--Login

/*Check if email exists in database and verify password (given email and password), and if they exist, 
return the logged in user’s UID*/
SELECT u.email, u.password, u.uid
FROM Users AS u
WHERE u.email = ? AND password = ?;


--Registration

--Check if Email Exists (given email) 
SELECT u.email
FROM Users AS u
WHERE u.email = ?;
--(we want this to return an empty relation)

/*Create a user given a list of user info (given a minimum of firstName, lastName, dob, password(hashed), and email; 
optionally can also include gender and hometown, if not they have null fields)*/
INSERT INTO Users (firstName, lastName, password, gender, hometown, email, dob)
VALUES (?, ?, ?, ?, ?, ?, ?);