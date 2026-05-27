-- ADMIN: admin@kuanto.cl / admin123
INSERT INTO users (
    id, nombre, apellido, email, password,
    rol, activo, created_at, updated_at
) VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Admin',
    'Kuanto',
    'admin@kuanto.cl',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ADMIN',
    TRUE,
    NOW(),
    NOW()
);

INSERT INTO user_profiles (
    id, user_id, ciudad,
    presupuesto, supermercado_fav, updated_at
) VALUES (
    '00000000-0000-0000-0000-000000000011',
    '00000000-0000-0000-0000-000000000001',
    'Santiago',
    'alto',
    'megacanasta',
    NOW()
);

-- USER: user@kuanto.cl / user123
INSERT INTO users (
    id, nombre, apellido, email, password,
    rol, activo, created_at, updated_at
) VALUES (
    '00000000-0000-0000-0000-000000000002',
    'Usuario',
    'Prueba',
    'user@kuanto.cl',
    '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQkLmPKpfCMwFb5RDlCLXLbh7GXMM6',
    'USER',
    TRUE,
    NOW(),
    NOW()
);

INSERT INTO user_profiles (
    id, user_id, ciudad,
    presupuesto, supermercado_fav, updated_at
) VALUES (
    '00000000-0000-0000-0000-000000000022',
    '00000000-0000-0000-0000-000000000002',
    'Santiago',
    'medio',
    'simermart',
    NOW()
);

INSERT INTO user_memories (
    id, user_id, contenido,
    origen, relevancia, created_at
) VALUES (
    '00000000-0000-0000-0000-000000000033',
    '00000000-0000-0000-0000-000000000002',
    'Le gustan los postres chilenos tradicionales',
    'USER_STATED',
    4,
    NOW()
);