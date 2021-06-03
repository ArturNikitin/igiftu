INSERT INTO Users (CREATED_DATE, email, PASSWORD, ISENABLED, ISACCOUNTNONLOCKED, ROLE, login)
VALUES ('2020-11-29 17:35:00', 'admin@gmail.com', '$2a$08$KniPEeGdpzOFQS.ap/8HveDZbXuOAeVUIb/jWsuK8CxCeSmtKMOWi', true, true,
        'ROLE_ADMIN', '@admin');

INSERT INTO Wishes (CREATED_DATE, last_modified_date, name, is_booked, is_completed, is_analog_possible, access, price, location,
                     details, link, user_id)
VALUES ('2020-11-29 17:35:00', '2020-11-29 17:35:00', 'test', false, false, false, 'PUBLIC', 10.00, 'location',
        'details', 'link', 1)