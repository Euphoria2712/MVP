-- Índices para búsquedas frecuentes
CREATE INDEX idx_users_email
    ON users(email);

CREATE INDEX idx_users_rol
    ON users(rol);

CREATE INDEX idx_user_profiles_user_id
    ON user_profiles(user_id);

CREATE INDEX idx_user_memories_user_id
    ON user_memories(user_id);

CREATE INDEX idx_user_memories_relevancia
    ON user_memories(user_id, relevancia DESC);

CREATE INDEX idx_user_intolerancias_profile
    ON user_intolerancias(profile_id);

CREATE INDEX idx_user_preferencias_profile
    ON user_preferencias(profile_id);