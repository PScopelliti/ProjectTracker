CREATE KEYSPACE notespace
WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

USE notespace;

CREATE TABLE notespace.note (
    noteid uuid,
    created timestamp,
    content text,
    userid uuid,
    PRIMARY KEY (noteid, created)
);
CREATE INDEX note_by_user ON notespace.note (userid);


INSERT INTO notespace.note (noteid, created, content, userid) VALUES (uuid(), toTimestamp(now()), 'some content 1', b709a8b4-68a1-46e7-90b8-a5c8da9316b6);
INSERT INTO notespace.note (noteid, created, content, userid) VALUES (uuid(), toTimestamp(now()), 'some content 2', b709a8b4-68a1-46e7-90b8-a5c8da9316b6);
INSERT INTO notespace.note (noteid, created, content, userid) VALUES (uuid(), toTimestamp(now()), 'some content 3', b709a8b4-68a1-46e7-90b8-a5c8da9316b6);
INSERT INTO notespace.note (noteid, created, content, userid) VALUES (uuid(), toTimestamp(now()), 'some content 4', b709a8b4-68a1-46e7-90b8-a5c8da9316b6);