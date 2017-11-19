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

COPY note(noteid, created, content, userid) FROM 'data.csv';