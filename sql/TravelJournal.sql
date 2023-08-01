CREATE TABLE ACCOUNT(
	FirstName VARCHAR(20) NOT NULL,
    LastName VARCHAR(30) NOT NULL,
    Username VARCHAR(30) PRIMARY KEY NOT NULL,
    Email VARCHAR(40) UNIQUE NOT NULL,
    UserPassword VARCHAR(20) NOT NULL,
    IsUser BOOLEAN NOT NULL,
	MembershipStartDate DATE NOT NULL,
    IsPublic BOOLEAN,
    IsBanned BOOLEAN DEFAULT FALSE,
    CHECK((IsUser = true AND IsPublic IS NOT NULL) OR IsUser = false)
);

-- CREATE TABLE LOCATION(
-- 	LocationID INT PRIMARY KEY AUTO_INCREMENT
-- );

CREATE TABLE CITY(
	CityName VARCHAR(20),
    Country VARCHAR(20),
    -- LocationID INT NOT NULL UNIQUE,
    PRIMARY KEY (CityName, Country)
--     FOREIGN KEY (LocationID) REFERENCES LOCATION(LocationID)
--         ON DELETE RESTRICT
);

-- CREATE TABLE SITE(
-- 	SiteID INT PRIMARY KEY AUTO_INCREMENT,
-- 	SiteName VARCHAR(30) NOT NULL,
--     CityName VARCHAR(20) NOT NULL,
--     Country VARCHAR(20) NOT NULL,
--     LocationID INT NOT NULL UNIQUE,
--     FOREIGN KEY (CityName, Country) REFERENCES CITY(CityName, Country),
--     UNIQUE (SiteName, CityName, Country),
--     FOREIGN KEY (LocationID) REFERENCES LOCATION(LocationID)
--         ON DELETE RESTRICT
-- );

CREATE TABLE TRIP(
	TripName VARCHAR(30),
    Username VARCHAR(30),
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    PRIMARY KEY (TripName, Username),
    FOREIGN KEY (Username) REFERENCES ACCOUNT(Username)
        ON DELETE CASCADE,
    CHECK (StartDate <= EndDate)
);


CREATE TABLE JOURNAL_ENTRY(
	Username VARCHAR(30) NOT NULL,
    EntryDate DATE NOT NULL,
    Note VARCHAR(250),
    Rating INT 
		CHECK (Rating >= 1 AND Rating <= 5),
	PrivacyLevel BOOLEAN,
    CityName VARCHAR(20),
    Country VARCHAR(20),
    EntryID INT AUTO_INCREMENT PRIMARY KEY,
    FOREIGN KEY (Username) REFERENCES ACCOUNT(Username)
        ON DELETE CASCADE,
    FOREIGN KEY (CityName, Country) REFERENCES City(CityName, Country)
        ON DELETE RESTRICT,
    CHECK (Rating IS NOT NULL OR Note IS NOT NULL),
    UNIQUE (Username, EntryDate, CityName, Country)
);

DELIMITER $$
USE `travel_journal`$$
CREATE TRIGGER `travel_journal`.`JOURNAL_ENTRY_BEFORE_INSERT_PRIVACY` BEFORE INSERT ON `JOURNAL_ENTRY` FOR EACH ROW
BEGIN
	DECLARE UserPublic BOOLEAN;
    
    SELECT IsPublic INTO UserPublic FROM ACCOUNT WHERE ACCOUNT.Username = NEW.username;
    
	IF NEW.PrivacyLevel IS NULL AND UserPublic = TRUE
		THEN SET NEW.PrivacyLevel = TRUE;
	END IF;
	IF NEW.PrivacyLevel IS NULL AND UserPublic = FALSE
		THEN SET NEW.PrivacyLevel = FALSE;
	END IF;
END$$
DELIMITER ;

DELIMITER $$
USE `travel_journal`$$
CREATE TRIGGER `travel_journal`.`JOURNAL_ENTRY_BEFORE_INSERT_CITY` BEFORE INSERT ON `JOURNAL_ENTRY` FOR EACH ROW
BEGIN
	IF NOT EXISTS (SELECT * FROM CITY WHERE CityName = New.CityName AND Country = New.Country)
		THEN INSERT INTO CITY VALUES (New.CityName, New.Country);
	END IF;
END$$
DELIMITER ;

CREATE TABLE ENTRY_IN_TRIP(
	Username VARCHAR(30),
    EntryID INT,
    TripName VARCHAR(30),
    PRIMARY KEY (Username, EntryID, TripName),
    FOREIGN KEY (TripName, Username) REFERENCES TRIP(TripName, Username)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (EntryID) REFERENCES JOURNAL_ENTRY(EntryID)
        ON DELETE CASCADE
);

CREATE TABLE REASON(
    Reason VARCHAR(50),
    PRIMARY KEY(Reason)
);

CREATE TABLE USER_FLAGS(
    Username VARCHAR(30) NOT NULL,
    EntryID INT NOT NULL,
    PRIMARY KEY(Username, EntryID),
	FOREIGN KEY (Username) REFERENCES ACCOUNT(Username)
        ON DELETE CASCADE,
    FOREIGN KEY (EntryID) REFERENCES JOURNAL_ENTRY(EntryID)
        ON DELETE CASCADE
);

CREATE TABLE FLAG_REASON(
    Username VARCHAR(30) NOT NULL,
    EntryID INT NOT NULL,
    Reason VARCHAR(50) NOT NULL,
    PRIMARY KEY(Username, EntryID, Reason),
    FOREIGN KEY (Reason) REFERENCES REASON(Reason)
        ON DELETE RESTRICT,
    FOREIGN KEY (Username, EntryID) REFERENCES USER_FLAGS(Username, EntryID)
        ON DELETE CASCADE
);

-- CREATE TABLE CATEGORY(
--     Category VARCHAR(50),
--     PRIMARY KEY(Category)
-- );

-- CREATE TABLE SITE_CATEGORIES(
--     SiteID INT,
--     Category VARCHAR(50),
--     PRIMARY KEY(SiteID, Category),
--     FOREIGN KEY (Category) REFERENCES CATEGORY(Category)
--         ON DELETE CASCADE
--         ON UPDATE CASCADE,
--     FOREIGN KEY (SiteID) REFERENCES SITE(SiteID)
--         ON DELETE CASCADE
--         ON UPDATE CASCADE
-- );

-- DELIMITER $$
-- USE `travel_journal`$$
-- CREATE TRIGGER `travel_journal`.`CITY_BEFORE_INSERT` BEFORE INSERT ON `CITY` FOR EACH ROW
-- BEGIN
-- 	DECLARE NewLocID INT;
--     INSERT INTO LOCATION
-- 		VALUE ();
--     SELECT MAX(LOCATION.LocationID) INTO NewLocId
-- 		FROM LOCATION;
-- 	SET NEW.LocationID = NewLocID;
-- END$$
-- DELIMITER ;

-- DELIMITER $$
-- USE `travel_journal`$$
-- CREATE TRIGGER `travel_journal`.`SITE_BEFORE_INSERT` BEFORE INSERT ON `SITE` FOR EACH ROW
-- BEGIN
-- 	DECLARE NewLocID INT;
--     INSERT INTO LOCATION
-- 		VALUE ();
--     SELECT MAX(LocationID) INTO NewLocId
-- 		FROM LOCATION;
-- 	SET NEW.LocationID = NewLocID;
-- END$$
-- DELIMITER ;