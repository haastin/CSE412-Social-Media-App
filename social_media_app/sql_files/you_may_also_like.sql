/*Fetch photos that contain any of the 5 most used user tags, and order them by photos that 
contain the most tags (given userID)*/
SELECT t1.pid, COUNT(*)
FROM Tags as t1
WHERE t1.word IN
	(SELECT t2.word, COUNT(*)
	FROM Tags as t2
	WHERE t2.pid IN
		(SELECT p.pid
		FROM Photos as p, Albums as a
		WHERE p.pid = a.pid AND a.uid = ?)
    GROUP BY t2.word
    ORDER BY COUNT(*) DESC
	LIMIT 5)
GROUP BY t1.pid
ORDER BY COUNT(*) DESC;
