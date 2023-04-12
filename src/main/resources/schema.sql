DROP TABLE IF EXISTS FILMS CASCADE;
DROP TABLE IF EXISTS GENRE_FILM CASCADE;
DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS USER_FRIENDS CASCADE;
DROP TABLE IF EXISTS FILM_LIKES CASCADE;

CREATE TABLE IF NOT EXISTS RATING (
    RATING_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY PRIMARY KEY,
    NAME VARCHAR
);

CREATE TABLE IF NOT EXISTS GENERE (
     GENERE_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     NAME VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS FILMS (
    FILM_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME VARCHAR NOT NULL,
    DESCRIPTION VARCHAR(200) NOT NULL,
    RELEASE_DATE DATE NOT NULL,
    DURATION INTEGER NOT NULL,
    RATING_ID INTEGER REFERENCES RATING (RATING_ID) ON DELETE RESTRICT,
    CONSTRAINT check_duration CHECK (DURATION > 0)
);

CREATE TABLE IF NOT EXISTS GENRE_FILM (
     GENERE_ID INTEGER REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
     FILM_ID INTEGER REFERENCES GENERE (GENERE_ID) ON DELETE CASCADE,
     PRIMARY KEY (GENERE_ID, FILM_ID)
);

CREATE TABLE IF NOT EXISTS USERS (
     USER_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     EMAIL VARCHAR NOT NULL,
     LOGIN VARCHAR NOT NULL,
     NAME VARCHAR NOT NULL,
     BIRTHDAY DATE NOT NULL,
     CONSTRAINT check_login CHECK (LOGIN <> ' ')
);

CREATE TABLE IF NOT EXISTS USER_FRIENDS (
      FRIEND_ID INTEGER REFERENCES USERS (USER_ID) ON DELETE CASCADE,
      USER_ID INTEGER REFERENCES USERS (USER_ID) ON DELETE CASCADE,
      STATUS VARCHAR NOT NULL,
      CONSTRAINT validate CHECK (FRIEND_ID <> USER_ID),
      PRIMARY KEY (FRIEND_ID, USER_ID)
);

CREATE TABLE IF NOT EXISTS FILM_LIKES (
     FILM_ID INTEGER REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
     USER_ID INTEGER REFERENCES USERS (USER_ID) ON DELETE CASCADE,
     PRIMARY KEY (FILM_ID, USER_ID)
);