SELECT * FROM Parts;

SELECT S.sname, COUNT(*)
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND S.sid = C.sid
GROUP BY S.sname;

SELECT S.sname, COUNT(*)
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND S.sid = C.sid
GROUP BY S.sname
HAVING COUNT(*) > 2;

SELECT S.sname, COUNT(*)
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND P.color = 'Green' AND S.sid = C.sid
GROUP BY S.sname
EXCEPT
SELECT S.sname, COUNT(*)
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND NOT P.color = 'Green' AND S.sid = C.sid
GROUP BY S.sname;


SELECT S.sname, MAX(C.cost)
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND S.sid = C.sid AND S.sname IN
(SELECT S.sname
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND P.color = 'Green' AND S.sid = C.sid
INTERSECT
SELECT S.sname
FROM Suppliers S, Parts P, Catalog C
WHERE P.pid = C.pid AND P.color = 'Red' AND S.sid = C.sid)
GROUP BY S.sname;
