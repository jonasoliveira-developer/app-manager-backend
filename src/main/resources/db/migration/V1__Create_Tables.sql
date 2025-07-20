
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    council_registration_number VARCHAR(50),
    subscription_type VARCHAR(50),
    account_status VARCHAR(20) NOT NULL CHECK (account_status IN ('ACTIVE', 'INACTIVE', 'BLOCKED', 'DELETED')),
    access_level VARCHAR(20) NOT NULL CHECK (access_level IN ( 'ROOT','ADMINISTRATOR','USER','CLIENT')),
    image_profile VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE clients (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    age VARCHAR(10),
    weight VARCHAR(10),
    height VARCHAR(10),
    local VARCHAR(100),
    account_status VARCHAR(20) NOT NULL CHECK (account_status IN ('ACTIVE', 'INACTIVE', 'BLOCKED', 'DELETED')),
    access_level VARCHAR(20) NOT NULL CHECK (access_level IN ( 'ROOT','ADMINISTRATOR','USER','CLIENT')),
    image_profile VARCHAR(255),
    user_id VARCHAR(36),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE care_plans (
    id VARCHAR(16) PRIMARY KEY,
    user_id VARCHAR(36),
    client_id VARCHAR(36),
    start_date DATE NOT NULL,
    expected_end_date DATE,
    actual_end_date DATE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE schedules (
     id VARCHAR(16) PRIMARY KEY,
    day_of_week VARCHAR(20) NOT NULL,
    session_time VARCHAR(20) NOT NULL,
    care_plan_id VARCHAR(36),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (care_plan_id) REFERENCES care_plans(id)
);

CREATE TABLE payments (
    id VARCHAR(16) PRIMARY KEY,
    opened_date DATE NOT NULL,
    closed_date DATE,
    payment_status VARCHAR(20) CHECK ( payment_status IN ('OPEN', 'CLOSED', 'LATE')),
    care_plan_id VARCHAR(36),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (care_plan_id) REFERENCES care_plans(id)
);

CREATE TABLE reports (
    id VARCHAR(36) PRIMARY KEY,
    council_registration_number VARCHAR(100),
    date DATE NOT NULL,
    text TEXT,
    user_id VARCHAR(36),
    client_id VARCHAR(36),
     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (client_id) REFERENCES clients(id)
);