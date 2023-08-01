USE travel_journal;

-- Q1 - return 0 if admin, 1 if user, empty table if user Does Not Exist
-- user and pass are passed in values from the user input on screen

SELECT IsUser FROM ACCOUNT WHERE username = "user" AND UserPassword = "pass";

-- Q2 - throws error if account fails
-- all passed in values of first, last, user, email, pass, and isUser
-- start date is current date, isBanned starts at FALSE by default
INSERT INTO ACCOUNT (FirstName, LastName, Username, Email, UserPassword, IsUser, MembershipStartDate, isPublic)
	VALUES ("first", "last", "userName", "email@email", "pass", 1, CURDATE(), 0);

-- Q3 NOT A QUERY

-- Q4 - return empty table if not admin/no user exists, a row with values otherwise
-- "user" is information stored from the previous screen

SELECT FirstName, LastName, Email, UserPassword, IsPublic
FROM ACCOUNT WHERE Username = "user" AND IsUser = 1;

-- Q5 deletes the account, "user" is stored from previous screen
DELETE FROM ACCOUNT WHERE Username = "userName";

-- Q6 updates an account, "user" is stored from previous screen

UPDATE ACCOUNT
SET FirstName = "NewFirst", LastName = "NewLast", Email = "NewEmail", UserPassword = "NewPass", IsPublic = "0"
WHERE Username="userEdit";

/* Query 7 - SQL is used to store a new trip definition. (Or reject it if the trip name is a
duplicate.) 
all information is input except for username which is passed from previous screens */

INSERT INTO TRIP (TripName, Username, StartDate, EndDate)
VALUES ('Summer in Barcelona', 'user', '2023-05-12', '2023-07-29');

/* Query 8 - Table is automatically populated with all cities and their avg rating based on
all users ratings (as long as the rating is public). */

SELECT CityName, Country, AVG(Rating)
FROM JOURNAL_ENTRY 
WHERE PrivacyLevel = TRUE
GROUP BY CityName, Country;

/* Query 9 - “Search for City” textbox filters down to just “London” for example if you
that is typed in and “Search” button is clicked (updating this window). */

SELECT CityName, Country, AVG(Rating)
FROM JOURNAL_ENTRY 
WHERE PrivacyLevel = TRUE AND CityName = "London"
GROUP BY CityName, Country;

/* Query 10 - “Reset” goes back to all cities being loaded. (Clears the search textbox
too.) SQL #8 and SQL #10 might be the same - you decide. */

SELECT CityName, Country, AVG(Rating)
FROM JOURNAL_ENTRY 
WHERE JOURNAL_ENTRY.PrivacyLevel = TRUE
GROUP BY CityName, Country;

-- SQL #11: this window is populated with all the (public) entries from all users about one given city.
-- Dummy data: City Name and Country Name from the previous screen 

SELECT EntryDate, Rating, Note
FROM JOURNAL_ENTRY
WHERE CityName = "Dublin" AND Country = "Ireland" AND PrivacyLevel = True;

-- SQL #12: This is populated by SQL about a single public city journal entry from any user.
-- Dummy data: from previous screen EntryID 

SELECT CityName, EntryDate, Rating, Note
FROM JOURNAL_ENTRY
WHERE EntryID = 2;

-- SQL #13: Pressing “report” will flag the entry, save the info about who flagged the entry, and save each of the reasons
-- user and the entry id are passed from the previous screen, flags from current screen input

INSERT INTO USER_FLAGS
VALUES ("user", 1);

INSERT INTO FLAG_REASON
	VALUES ("user", 1, "Harassment"), ("user", 1, "Explicit Language");

/*
-- SQL #14: Public vs Private. The user can choose to click to change that if they want to
-- preset comes from account settings (0 if private, 1 if public) */

SELECT IsPublic 
FROM ACCOUNT
WHERE Username = "user";

-- SQL #15 to save the city journal entry and then return the user to the home screen
-- user, isPublic come from the stored values, rest come from input

INSERT INTO JOURNAL_ENTRY(Username,EntryDate, Note, Rating, PrivacyLevel, CityName, Country) 
VALUES ("user", CURDATE(), "Great visit!", 5, True, "NewCity", "NewCountry");

INSERT INTO JOURNAL_ENTRY(Username,EntryDate, Note, Rating, PrivacyLevel, CityName, Country) 
VALUES ("user", "2023-08-01", "note!", NULL, True, "London", "NewCountry");

-- SQL #16: query populates this screen with the user’s trip names. Clicking a trip name takes the user to the “My Trip Report” screen.
-- "user" is the username of the currently logged in user, has to be passed from previous screens

SELECT TripName
FROM TRIP
WHERE Username = "user";

-- SQL #17: query populates this screen with the user’s trip names. Clicking a trip name takes the user to the “My Trip Report” screen.
-- "user" is the username of the currently logged in user, has to be passed from previous screens
-- "trip" is the trip name of the trip that the user clicked on, has to be passed from previous screens

SELECT EntryDate, CityName, Country, Rating, Note
FROM JOURNAL_ENTRY NATURAL JOIN CITY
WHERE Username = "user" AND EntryDate BETWEEN (SELECT StartDate
												FROM TRIP
												WHERE TripName = "trip" AND Username = "user")
                                                AND
                                                (SELECT EndDate
												FROM TRIP
												WHERE TripName = "trip" AND Username = "user")
ORDER BY EntryDate;

-- SQL #18: “Or show all of my entries” shows all of this user’s journal entries. (public and private
-- alike whether or not they are part of any trip range of dates)
-- "user" is the username of the currently logged in user, has to be passed from previous screens 

SELECT EntryDate, CityName, Country, Rating, Note
FROM JOURNAL_ENTRY NATURAL JOIN CITY
WHERE Username = "user"
ORDER BY EntryDate;

-- SQL #19: Clicking on “City” reorders the info alphabetically by city name (A-Z)
-- alike whether or not they are part of any trip range of dates)
-- "user" is the username of the currently logged in user, has to be passed from previous screens
-- "trip" is the trip name of the trip that the user clicked on, has to be passed from previous screens
-- in the java, whether or not all cities are being shown should be determined

SELECT EntryDate, CityName, Country, Rating, Note
FROM JOURNAL_ENTRY NATURAL JOIN CITY
WHERE Username = "user"
ORDER BY CityName ASC;

SELECT EntryDate, CityName, Country, Rating, Note
FROM JOURNAL_ENTRY NATURAL JOIN CITY
WHERE Username = "user" AND EntryDate BETWEEN (SELECT StartDate
												FROM TRIP
												WHERE TripName = "trip" AND Username = "user")
                                                AND
                                                (SELECT EndDate
												FROM TRIP
												WHERE TripName = "trip" AND Username = "user")
ORDER BY CityName ASC;

-- We get here from My Trip Report having clicked on a Note in the list.
-- SQL #20: populates this screen.
-- entryId comes from the past screen based on which note the user clicks on and which entry that is from

SELECT EntryDate, CityName, Rating, Note
FROM JOURNAL_ENTRY
WHERE EntryID = 2;


-- BUT, “delete” deletes this city entry via SQL #21.
-- entryId comes from the past screen based on which note the user clicks on and which entry that is from
DELETE FROM JOURNAL_ENTRY
WHERE EntryID = 2;

-- #22: SQL query used to help auto-populate this screen,
-- and each Note in the list is clickable to take us to the Admin’s Review City Entry screen.

SELECT
	je.Username,
    je.CityName, 
    je.Note,
    GROUP_CONCAT(fr.Reason SEPARATOR ", "),
    uf.USERNAME AS FLAGGER
FROM
    JOURNAL_ENTRY je
    JOIN USER_FLAGS uf ON je.EntryID = uf.EntryID
    LEFT OUTER JOIN FLAG_REASON fr ON uf.Username = fr.Username AND uf.EntryID and fr.EntryID
GROUP BY
    je.Username,
    je.CityName,
    je.Note,
    uf.Username;

-- #23: not a query

-- #24: populates the "Review Flagged Entry (admin)" 
-- entry id passed in from the clicked on flag in the past screen

SELECT
	je.cityName,
    je.EntryDate,
    je.Rating,
    je.Note
FROM
	JOURNAL_ENTRY je
WHERE
	EntryID = 4;
 
-- #25: "Clear Flag" button that clears the flag from the entry
-- will remove all flags related to the entry, entryID passed from previous screen

DELETE FROM USER_FLAGS
WHERE EntryID = 1;

-- #26: "Delete entry" button which deletes this one entry
-- entryId passed from previous screen

DELETE FROM JOURNAL_ENTRY
WHERE EntryID = 3;

-- #27: "Ban User" marks the user as banned, and all of their entries are deleted.
-- mark the user as banned

UPDATE ACCOUNT
SET IsBanned = TRUE
WHERE Username = "user";

-- delete all entries of the banned user
DELETE FROM JOURNAL_ENTRY
WHERE Username = "user";





