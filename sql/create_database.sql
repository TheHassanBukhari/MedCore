-- MedCore Hospital Management
-- Create Oracle User / Schema

CREATE USER hospital IDENTIFIED BY hosp123;

GRANT CONNECT, RESOURCE TO hospital;

ALTER USER hospital QUOTA UNLIMITED ON USERS;
