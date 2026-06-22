-- Tabla principal de usuarios
CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    apellido    VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    rol         VARCHAR(20)  NOT NULL DEFAULT 'USER',
    activo      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);

-- Perfil extendido del usuario
CREATE TABLE IF NOT EXISTS user_profiles (
    id                VARCHAR(36)  NOT NULL PRIMARY KEY,
    user_id           VARCHAR(36)  NOT NULL UNIQUE,
    ciudad            VARCHAR(100),
    presupuesto       VARCHAR(20),
    supermercado_fav  VARCHAR(50),
    updated_at        DATETIME(6)
);

-- Memorias de la IA sobre el usuario
CREATE TABLE IF NOT EXISTS user_memories (
    id          VARCHAR(36)   NOT NULL PRIMARY KEY,
    user_id     VARCHAR(36)   NOT NULL,
    contenido   VARCHAR(1000) NOT NULL,
    origen      VARCHAR(30),
    relevancia  INT           NOT NULL DEFAULT 3,
    created_at  DATETIME(6)
);

-- Intolerancias alimentarias del usuario
CREATE TABLE IF NOT EXISTS user_intolerancias (
    profile_id    VARCHAR(36)  NOT NULL,
    intolerancia  VARCHAR(100) NOT NULL
);

-- Preferencias de cocina del usuario
CREATE TABLE IF NOT EXISTS user_preferencias (
    profile_id   VARCHAR(36)  NOT NULL,
    preferencia  VARCHAR(100) NOT NULL
);