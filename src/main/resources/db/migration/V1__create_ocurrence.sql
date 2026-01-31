CREATE TABLE occurrence (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            type VARCHAR(50) NOT NULL,
                            date DATE NOT NULL,
                            description VARCHAR(280) NOT NULL,
                            latitude NUMERIC(9, 6) NOT NULL,
                            longitude NUMERIC(9, 6) NOT NULL,
                            photo_url VARCHAR(500),
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_occurrence_type ON occurrence(type);
CREATE INDEX idx_occurrence_date ON occurrence(date);
CREATE INDEX idx_occurrence_created_at ON occurrence(created_at);