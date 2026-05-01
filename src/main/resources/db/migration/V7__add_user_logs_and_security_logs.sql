CREATE TABLE user_logs (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           user_id BIGINT,
                           email VARCHAR(255) NOT NULL,
                           action VARCHAR(100) NOT NULL,
                           entity VARCHAR(100),
                           entity_id BIGINT,
                           ip_address VARCHAR(50),
                           user_agent TEXT,
                           request_uri TEXT,
                           http_method VARCHAR(10),
                           details TEXT,
                           created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_user_logs_user_id ON user_logs(user_id);
CREATE INDEX idx_user_logs_action ON user_logs(action);
CREATE INDEX idx_user_logs_created_at ON user_logs(created_at);

CREATE TABLE security_logs (
                             id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             user_id BIGINT,
                             email VARCHAR(255) NOT NULL,
                             action VARCHAR(100) NOT NULL,
                             success BOOLEAN NOT NULL,
                             ip_address VARCHAR(255),
                             user_agent VARCHAR(255),
                             request_uri TEXT,
                             http_method VARCHAR(10),
                             details TEXT,
                             created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_security_logs_user_id ON security_logs(user_id);
CREATE INDEX idx_security_logs_email ON security_logs(email);
CREATE INDEX idx_security_logs_created_at ON security_logs(created_at);