-- MedCore Hospital Management
-- Table Creation Script

CREATE TABLE DOCTOR (
    doctor_id NUMBER PRIMARY KEY,
    first_name VARCHAR2(30),
    middle_name VARCHAR2(30),
    last_name VARCHAR2(30)
);

CREATE TABLE DOCTOR_SPECIALIZATION (
    doctor_id NUMBER,
    specialization VARCHAR2(50),

    PRIMARY KEY (doctor_id, specialization),

    FOREIGN KEY (doctor_id)
    REFERENCES DOCTOR(doctor_id)
);

CREATE TABLE DOCTOR_QUALIFICATION (
    doctor_id NUMBER,
    qualification VARCHAR2(50),

    PRIMARY KEY (doctor_id, qualification),

    FOREIGN KEY (doctor_id)
    REFERENCES DOCTOR(doctor_id)
);

CREATE TABLE PATIENT (
    patient_id NUMBER PRIMARY KEY,
    age NUMBER,
    dob DATE,
    locality VARCHAR2(40),
    town_city VARCHAR2(40)
);

CREATE TABLE MEDICINE (
    code NUMBER PRIMARY KEY,
    price NUMBER(8,2),
    quantity NUMBER
);

CREATE TABLE TREATS (
    doctor_id NUMBER,
    patient_id NUMBER,

    PRIMARY KEY (doctor_id, patient_id),

    FOREIGN KEY (doctor_id)
    REFERENCES DOCTOR(doctor_id),

    FOREIGN KEY (patient_id)
    REFERENCES PATIENT(patient_id)
);

CREATE TABLE BILL (
    patient_id NUMBER,
    medicine_code NUMBER,

    PRIMARY KEY (patient_id, medicine_code),

    FOREIGN KEY (patient_id)
    REFERENCES PATIENT(patient_id),

    FOREIGN KEY (medicine_code)
    REFERENCES MEDICINE(code)
);
