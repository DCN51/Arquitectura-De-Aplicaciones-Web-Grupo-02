-- =====================================================================
-- Script de siembra de roles y usuarios de prueba - MindCare
-- Ejecutar sobre la base de datos BDArquiWeb02 (PostgreSQL)
-- Requiere la extension pgcrypto para generar el hash BCrypt del password
-- Password de TODOS los usuarios de prueba: 123456
-- =====================================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1) Roles base
INSERT INTO roles (rol) VALUES ('ADMINISTRADOR') ON CONFLICT (rol) DO NOTHING;
INSERT INTO roles (rol) VALUES ('PSICOLOGO')      ON CONFLICT (rol) DO NOTHING;
INSERT INTO roles (rol) VALUES ('PACIENTE')        ON CONFLICT (rol) DO NOTHING;

-- 2) Usuarios de prueba
INSERT INTO users
    (id_user, username, idrol, nombres, apellidos, email, password,
     telefono, birth_date, genero, register_date, account_status,
     verification_status, tuition_number, especializacion, experience_years)
VALUES
    (1,  'admin',        (SELECT id FROM roles WHERE rol = 'ADMINISTRADOR'), 'Admin',   'Sistema',  'admin@mindcare.com',        crypt('123456', gen_salt('bf')), '900000001', '1985-01-01', 'M', now(), 'ACTIVO', 'VERIFICADO', NULL,         NULL,                              NULL),
    (2,  'cmendoza',     (SELECT id FROM roles WHERE rol = 'PSICOLOGO'),     'Carla',   'Mendoza',  'cmendoza@mindcare.com',     crypt('123456', gen_salt('bf')), '900000002', '1980-03-12', 'F', now(), 'ACTIVO', 'VERIFICADO', 'CPSP-0001',  'Psicologia Clinica',              10),
    (3,  'jrios',        (SELECT id FROM roles WHERE rol = 'PSICOLOGO'),     'Jorge',   'Rios',     'jrios@mindcare.com',        crypt('123456', gen_salt('bf')), '900000003', '1982-07-22', 'M', now(), 'ACTIVO', 'VERIFICADO', 'CPSP-0002',  'Psicologia Cognitivo-Conductual', 8),
    (4,  'lparedes',     (SELECT id FROM roles WHERE rol = 'PSICOLOGO'),     'Lucia',   'Paredes',  'lparedes@mindcare.com',     crypt('123456', gen_salt('bf')), '900000004', '1979-11-05', 'F', now(), 'ACTIVO', 'VERIFICADO', 'CPSP-0003',  'Psicologia Infantil',             12),
    (5,  'ana.torres',   (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Ana',     'Torres',   'ana.torres@mindcare.com',   crypt('123456', gen_salt('bf')), '900000005', '1995-04-18', 'F', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (6,  'luis.garcia',  (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Luis',    'Garcia',   'luis.garcia@mindcare.com', crypt('123456', gen_salt('bf')), '900000006', '1992-09-09', 'M', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (7,  'maria.lopez',  (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Maria',   'Lopez',    'maria.lopez@mindcare.com', crypt('123456', gen_salt('bf')), '900000007', '1998-02-14', 'F', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (8,  'diego.ramos',  (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Diego',   'Ramos',    'diego.ramos@mindcare.com', crypt('123456', gen_salt('bf')), '900000008', '1990-12-01', 'M', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (9,  'sofia.chavez', (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Sofia',   'Chavez',   'sofia.chavez@mindcare.com',crypt('123456', gen_salt('bf')), '900000009', '1996-06-23', 'F', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (10, 'pedro.silva',  (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Pedro',   'Silva',    'pedro.silva@mindcare.com', crypt('123456', gen_salt('bf')), '900000010', '1993-08-30', 'M', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (11, 'valeria.diaz', (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Valeria', 'Diaz',     'valeria.diaz@mindcare.com',crypt('123456', gen_salt('bf')), '900000011', '1997-10-17', 'F', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL)
ON CONFLICT (id_user) DO UPDATE SET
    username            = EXCLUDED.username,
    idrol               = EXCLUDED.idrol,
    nombres             = EXCLUDED.nombres,
    apellidos           = EXCLUDED.apellidos,
    email               = EXCLUDED.email,
    password            = EXCLUDED.password,
    telefono            = EXCLUDED.telefono,
    birth_date          = EXCLUDED.birth_date,
    genero              = EXCLUDED.genero,
    account_status      = EXCLUDED.account_status,
    verification_status = EXCLUDED.verification_status,
    tuition_number      = EXCLUDED.tuition_number,
    especializacion     = EXCLUDED.especializacion,
    experience_years    = EXCLUDED.experience_years;

-- 3) Reajustar la secuencia de autoincremento tras insertar IDs explicitos
DO $$
DECLARE seq_name text;
BEGIN
    seq_name := pg_get_serial_sequence('users', 'id_user');
    IF seq_name IS NOT NULL THEN
        PERFORM setval(seq_name, (SELECT COALESCE(MAX(id_user), 1) FROM users));
    END IF;
END $$;
