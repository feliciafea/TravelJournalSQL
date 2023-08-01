USE travel_journal;

INSERT INTO ACCOUNT
VALUES
    ("Cameron", "Loyet", "camloyet", "cameron@gmail.com", "password1234", True, "2020-08-08", FALSE, FALSE),
	("Bob", "Bober", "bobby", "bobBryan@gmail.com", "password456", False, "2013-07-21", NULL, NULL),
    ("Tom", "Itschner", "travel_lover123", "tom@gmail.com", "travelingisfun", True, "2019-07-10", TRUE, FALSE),
    ("first", "last", "user", "email", "pass", True, "2023-07-04", True, FALSE),
    ("first", "last", "userEdit", "emailEdit", "pass", True, "2023-07-04", True, FALSE);

INSERT INTO CITY
VALUES 
	("London", "United Kingdom"),
	("Dublin", "Ireland"),
	("Sydney", "Australia"),
    ("Dublin", "NotIreland");

INSERT INTO TRIP
VALUES 
    ("Trip to London!", "camloyet", "2023-06-21", "2023-06-23"),
    ("Vacation in Dublin", "travel_lover123", "2022-06-20", "2022-06-27"),
    ("trip", "user", "2023-04-01", "2023-07-04")
;

INSERT INTO JOURNAL_ENTRY(Username, EntryDate, Note, Rating, PrivacyLevel, CityName, Country)
VALUES 
    ("camloyet", "2023-07-01", "Saw Buckingham Palace with friends today! Didn't see my favorite King Charles though :(",
    4, TRUE, "London", "United Kingdom"),
    ("travel_lover123", "2023-06-01", "Loved me a good ole Irish pub!",
     NULL, NULL, "Dublin", "Ireland"),
     ("travel_lover123", "2023-06-03", "Shamrocks rock!",
     4, NULL, "Dublin", "Ireland"),
     ("camloyet", "2023-06-01", NULL,
     3, NULL, "Dublin", "Ireland"), 
     ("travel_lover123", "2023-02-01", ":)",
	5, TRUE, "London", "United Kingdom"),
    ("user", "2023-04-01", "london!",
    1, TRUE, "London", "United Kingdom"),
    ("travel_lover123", "2023-05-01", NULL,
    2, TRUE, "Dublin", "NotIreland"), 
    ("user", "2023-07-01", "still in london",
	5, TRUE, "London", "United Kingdom"), 
    ("user", "2023-05-01", "london again",
	1, TRUE, "London", "United Kingdom"), 
    ("user", "2023-02-01", "dublin but ireland",
	3, TRUE, "Dublin", "Ireland"), 
    ("user", "2023-03-01", "dublin but not ireland",
	4, TRUE, "Dublin", "NotIreland")
;

INSERT INTO ENTRY_IN_TRIP
VALUES 
    ("camloyet", 1, "Trip to London!"),
    ("travel_lover123", 1, "Vacation in Dublin")
;

INSERT INTO REASON
VALUES 
    ("Harassment"),
    ("Explicit Language"),
    ("Off Topic")
;

INSERT INTO USER_FLAGS
VALUES ("camloyet", 5), ("travel_lover123", 3);

INSERT INTO FLAG_REASON
	VALUES ("camloyet", 5, "Harassment"), ("camloyet", 5, "Off Topic");