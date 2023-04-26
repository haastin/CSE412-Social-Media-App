/*When rendering the homepage, we will select all photo pids not from the user and 
randomly choose among them in our business logic which ones to render */

SELECT p.pid
FROM Photos AS p, Albums AS a
WHERE p.aid = a.aid AND a.uid != ?;