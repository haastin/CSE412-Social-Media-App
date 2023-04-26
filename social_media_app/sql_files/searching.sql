
--Searching for comments

--Search for Comment based on text query (given text)
SELECT c.text, u.firstName, u.lastName, COUNT(*)
FROM Comments AS c, Users AS u
WHERE c.text = ? AND c.uid = u.uid
GROUP BY c.uid;
ORDER BY COUNT(*) DESC;


--Viewing all photos by tag name

--Retrieve all photo ids tagged with a searched tag (given the string submitted)
SELECT pid
FROM Tags as t
WHERE t.word = ?;
--Each pid in the list of retrieved pids will be rendered one at a time; can re-use render_photo to render them


--Viewing own photos by tag name

/* Retrieve all photo ids tagged with a searched tag that the user logged in posted 
(given the string submitted and user who’s logged in uid)*/
SELECT p.pid
FROM Photos AS p, Albums AS a
WHERE p.aid = a.aid AND a.uid = ? AND p.pid IN
(SELECT t.pid
FROM Tags as t
WHERE t.word = ?);


--Searching for Photos via Multiple Tags

/*Retrieve all photos that belong to multiple tags (given multiple tags);
This query will have to be dynamically built after receiving the number of tags a user wants to search for*/
SELECT t.pid
FROM Tags as t
WHERE t.word = ? AND t.word = ? AND t.word = ? AND t.word …;


/*Retrieve all photos that belong to multiple tags (given multiple tags) AND also belongs to user (given user id);
This query will have to be dynamically built after receiving the number of tags a user wants to search for*/
SELECT t.pid
FROM Tags as t
WHERE t.word = ? AND t.word = ? AND t.word = ? AND t.word …
AND t.pid IN
	(SELECT p.pid
	FROM Photos as p, Albums as a
	WHERE p.aid = a.aid AND a.uid = ?);



--Viewing Most Popular Tags

--Retrieve most popular tags in descending order
SELECT t.word
FROM Tags as t
GROUP BY t.word
ORDER BY COUNT(*) DESC;

--Retrieve pid of a tag (given the tag’s word), repeated for each unique word fetched above
SELECT t.pid
FROM Tags as t
WHERE t.word = ?;

--Render them using render_photo queries