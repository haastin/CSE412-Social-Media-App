--Count the number of photos each user uploads
SELECT COUNT(*), p.uid
FROM Photos as p
GROUP BY p.uid;


--Count the number of comments each user uploads
SELECT COUNT(*), c.uid
FROM Comments as c
GROUP BY c.uid;

--Given list of top ten users, find their first and last name
SELECT u.firstName, u.lastName
FROM Users AS u
WHERE u.uid IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);