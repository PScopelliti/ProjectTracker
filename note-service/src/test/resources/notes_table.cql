CREATE KEYSPACE notespace
WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE notespace.notes (
id uuid,
userid uuid,
created timestamp,
content text,
PRIMARY KEY (id)
);
