DROP TABLE Professor cascade;
DROP TABLE Project cascade;
DROP TABLE Graduate cascade;
DROP TABLE Department cascade;
DROP TABLE work_in cascade;
DROP TABLE work_proj cascade;
DROP TABLE work_dept cascade;

CREATE TABLE Professor (ssn CHAR(11) NOT NULL,
                        name CHAR(30) NOT NULL,
                        age INTEGER,
                        rank INTEGER, 
						specialty CHAR(30) NOT NULL,
						PRIMARY KEY(ssn));
				
CREATE TABLE Department (dno INTEGER NOT NULL,
                         dname CHAR(30) NOT NULL,
                         office INTEGER,
                         chairman CHAR(11) NOT NULL,
                         PRIMARY KEY(dno),
                         FOREIGN KEY(chairman) references Professor(ssn));

CREATE TABLE Project (pno INTEGER NOT NULL,
                      sponsor text,
                      start_date text,
                      end_date text,
					  budget INTEGER,
					  manager CHAR(11),
					  PRIMARY KEY(pno),
					  FOREIGN KEY(manager) REFERENCES Professor(ssn));
				  
CREATE TABLE Graduate (ssn CHAR(11) NOT NULL,
                       grad_advisor CHAR(11) NOT NULL,
                       name CHAR(30) NOT NULL,
                       age INTEGER,
                       deg_pg INTEGER,
                       advises CHAR(11),
                       major CHAR(11) NOT NULL,
					   PRIMARY KEY(ssn),
					   FOREIGN KEY(major) REFERENCES Graduate(ssn));

CREATE TABLE work_in (ssn CHAR(11) NOT NULL,
                      pno INTEGER NOT NULL,
                      PRIMARY KEY(ssn, pno),
                      FOREIGN KEY (ssn) REFERENCES Professor(ssn),
                      FOREIGN KEY (pno) REFERENCES Project(pno)
                      ON DELETE CASCADE);
                      
CREATE TABLE work_proj (since CHAR(11) NOT NULL,
						supervisor CHAR(11) NOT NULL,
						ssn CHAR(11) NOT NULL,
						pno INTEGER NOT NULL,
						PRIMARY KEY(supervisor),
						FOREIGN KEY(ssn) REFERENCES Graduate(ssn),
						FOREIGN KEY(pno) REFERENCES Project(pno),
						FOREIGN KEY(supervisor) REFERENCES Professor(ssn)
						ON DELETE CASCADE);
						
CREATE TABLE work_dept (time_pc CHAR(11) NOT NULL,
						ssn CHAR(11) NOT NULL,
						dno INTEGER NOT NULL,
						PRIMARY KEY(ssn, dno),
						FOREIGN KEY(ssn) REFERENCES Professor(ssn),
						FOREIGN KEY(dno) REFERENCES Department(dno)
						ON DELETE CASCADE);



DROP TABLE Place cascade;
DROP TABLE Telephone cascade;
DROP TABLE Musicians cascade;
DROP TABLE Album cascade;
DROP TABLE Instrument cascade;
DROP TABLE Songs cascade;
DROP TABLE Lives cascade;
DROP TABLE Perform cascade;
DROP TABLE Plays cascade;

CREATE TABLE Place (address CHAR(30) NOT NULL, PRIMARY KEY(address));

CREATE TABLE Telephone (phone_no CHAR(30) NOT NULL,
                        home CHAR(30) NOT NULL,
						PRIMARY KEY(phone_no),
						FOREIGN KEY(home) REFERENCES Place(address)
						ON DELETE CASCADE);

CREATE TABLE Musicians (ssn CHAR(11) NOT NULL,
                        name CHAR(30) NOT NULL,
                        home CHAR(30) NOT NULL,
                        PRIMARY KEY(ssn));

CREATE TABLE Album (albumIdentifier CHAR(30) NOT NULL,
                    copyrightDate CHAR(30) NOT NULL,
					speed INTEGER NOT NULL,
					title CHAR(30) NOT NULL,
					producer CHAR(11) NOT NULL,
					PRIMARY KEY(albumIdentifier),
					FOREIGN KEY(producer) REFERENCES Musicians(ssn));

CREATE TABLE Instrument (instrId INTEGER NOT NULL,
                         dname CHAR(30) NOT NULL,
                         key CHAR(30) NOT NULL,
                         played_by CHAR(11) NOT NULL,
						 PRIMARY KEY(instrId),
						 FOREIGN KEY(played_by) REFERENCES Musicians(ssn));

CREATE TABLE Songs (songId INTEGER NOT NULL,
                    title CHAR(30) NOT NULL,
                    author CHAR(30) NOT NULL,
					album CHAR(30) NOT NULL,
					PRIMARY KEY(songId),
					FOREIGN KEY(album) REFERENCES Album(albumIdentifier)
					ON DELETE CASCADE);

CREATE TABLE Lives (musician CHAR(11) NOT NULL,
                    home CHAR(30) NOT NULL,
                    PRIMARY KEY(musician, home),
                    FOREIGN KEY(musician) REFERENCES Musicians(ssn),
                    FOREIGN KEY(home) REFERENCES Place(address)
                    ON DELETE CASCADE);

CREATE TABLE Perform (ssn CHAR(11) NOT NULL,
                      songId INTEGER NOT NULL,
					  PRIMARY KEY(ssn, songId),
					  FOREIGN KEY(ssn) REFERENCES Musicians(ssn),
					  FOREIGN KEY(songId) REFERENCES Songs(songId)
					  ON DELETE CASCADE);

CREATE TABLE Plays (ssn CHAR(11) NOT NULL,
                    instrId INTEGER NOT NULL,
					PRIMARY KEY(ssn, instrId),
					FOREIGN KEY(ssn) REFERENCES Musicians(ssn),
					FOREIGN KEY(instrId) REFERENCES Instrument(instrId)
					ON DELETE CASCADE);
