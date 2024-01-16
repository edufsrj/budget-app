-- Enabling uuid-ossp extension is available
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Now, create the users table with a default value for the id column
CREATE TABLE IF NOT EXISTS users (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    nick_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    creation_time TIMESTAMP DEFAULT current_timestamp,
    update_time TIMESTAMP DEFAULT current_timestamp
);
