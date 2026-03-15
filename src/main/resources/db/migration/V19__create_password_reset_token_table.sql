CREATE TABLE password_reset_token (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    token_hash VARCHAR(128) NOT NULL UNIQUE,

    expires_at TIMESTAMP NOT NULL,

    used BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL
);

ALTER TABLE password_reset_token
ADD CONSTRAINT fk_password_reset_token_user
FOREIGN KEY (user_id)
REFERENCES usuarios(id)
ON DELETE CASCADE;