-- =====================================================================
-- Script de siembra de datos de prueba - MindCare
-- Base de datos: BDArquiWeb02 (PostgreSQL)
-- Requiere la extension pgcrypto para generar el hash BCrypt del password
-- Password de TODOS los usuarios de prueba: 123456
--
-- Contenido:
--   1) Roles base
--   2) Usuarios (admin, psicologos y pacientes)
--   3) Sesiones clinicas (necesarias para los diagnosticos)
--   4) Diagnosticos clinicos (necesarios para las actividades)
--   5) Actividades recomendadas -> se consultan con:
--        GET /api/activities/users/{userId}
--        GET /api/activities/users/{userId}?status=PENDIENTE
--        GET /api/activities/users/{userId}?status=COMPLETADA&from=...&to=...
--
-- Roles / Usuarios (id):
--   Administrador : admin (1)
--   Psicologo     : cmendoza (2), jrios (3), lparedes (4)
--   Paciente      : ana.torres (5), luis.garcia (6), maria.lopez (7),
--                   diego.ramos (8), sofia.chavez (9), pedro.silva (10),
--                   valeria.diaz (11)
-- =====================================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ---------------------------------------------------------------------
-- 1) Roles base
-- ---------------------------------------------------------------------
INSERT INTO roles (rol) VALUES ('ADMINISTRADOR') ON CONFLICT (rol) DO NOTHING;
INSERT INTO roles (rol) VALUES ('PSICOLOGO')      ON CONFLICT (rol) DO NOTHING;
INSERT INTO roles (rol) VALUES ('PACIENTE')        ON CONFLICT (rol) DO NOTHING;

-- ---------------------------------------------------------------------
-- 2) Usuarios de prueba
-- ---------------------------------------------------------------------
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
    (6,  'luis.garcia',  (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Luis',    'Garcia',   'luis.garcia@mindcare.com',  crypt('123456', gen_salt('bf')), '900000006', '1992-09-09', 'M', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (7,  'maria.lopez',  (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Maria',   'Lopez',    'maria.lopez@mindcare.com',  crypt('123456', gen_salt('bf')), '900000007', '1998-02-14', 'F', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (8,  'diego.ramos',  (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Diego',   'Ramos',    'diego.ramos@mindcare.com',  crypt('123456', gen_salt('bf')), '900000008', '1990-12-01', 'M', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (9,  'sofia.chavez', (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Sofia',   'Chavez',   'sofia.chavez@mindcare.com', crypt('123456', gen_salt('bf')), '900000009', '1996-06-23', 'F', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (10, 'pedro.silva',  (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Pedro',   'Silva',    'pedro.silva@mindcare.com',  crypt('123456', gen_salt('bf')), '900000010', '1993-08-30', 'M', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL),
    (11, 'valeria.diaz', (SELECT id FROM roles WHERE rol = 'PACIENTE'),      'Valeria', 'Diaz',     'valeria.diaz@mindcare.com', crypt('123456', gen_salt('bf')), '900000011', '1997-10-17', 'F', now(), 'ACTIVO', 'PENDIENTE',  NULL,         NULL,                              NULL)
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

-- ---------------------------------------------------------------------
-- 3) Sesiones clinicas (una por paciente)
-- ---------------------------------------------------------------------
INSERT INTO sesiones_clinicas
    (sesion_clinica_id, usuario_id, fecha_hora_inicio, fecha_hora_fin,
     emocion_detectada, nivel_urgencia, resumen_ia, requiere_derivacion, estado)
VALUES
    (1,  5,  now() - interval '30 day', now() - interval '30 day' + interval '45 minute', 'ansiedad',  'medio', 'El paciente reporta episodios de ansiedad frecuentes.', false, 'finalizada'),
    (2,  6,  now() - interval '28 day', now() - interval '28 day' + interval '40 minute', 'tristeza',  'medio', 'Se detecta estado de animo bajo sostenido.',            false, 'finalizada'),
    (3,  7,  now() - interval '25 day', now() - interval '25 day' + interval '50 minute', 'estres',    'alto',  'Altos niveles de estres laboral.',                      true,  'finalizada'),
    (4,  8,  now() - interval '22 day', now() - interval '22 day' + interval '35 minute', 'ansiedad',  'bajo',  'Ansiedad leve controlable.',                            false, 'finalizada'),
    (5,  9,  now() - interval '20 day', now() - interval '20 day' + interval '42 minute', 'tristeza',  'medio', 'Dificultades para conciliar el sueno.',                 false, 'finalizada'),
    (6,  10, now() - interval '18 day', now() - interval '18 day' + interval '38 minute', 'estres',    'medio', 'Carga academica elevada.',                              false, 'finalizada'),
    (7,  11, now() - interval '15 day', now() - interval '15 day' + interval '47 minute', 'ansiedad',  'alto',  'Crisis de ansiedad recurrentes.',                       true,  'finalizada')
ON CONFLICT (sesion_clinica_id) DO UPDATE SET
    usuario_id          = EXCLUDED.usuario_id,
    fecha_hora_inicio   = EXCLUDED.fecha_hora_inicio,
    fecha_hora_fin      = EXCLUDED.fecha_hora_fin,
    emocion_detectada   = EXCLUDED.emocion_detectada,
    nivel_urgencia      = EXCLUDED.nivel_urgencia,
    resumen_ia          = EXCLUDED.resumen_ia,
    requiere_derivacion = EXCLUDED.requiere_derivacion,
    estado              = EXCLUDED.estado;

-- ---------------------------------------------------------------------
-- 4) Diagnosticos clinicos (uno por paciente, validados por un psicologo)
-- ---------------------------------------------------------------------
INSERT INTO diagnosticos_clinicos
    (diagnostico_clinico_id, usuario_id, sesion_clinica_id, fecha_diagnostico,
     resultado_principal, descripcion_detallada, nivel_riesgo,
     recomendacion_general, validacion_psicologo_id, fecha_validacion)
VALUES
    (1, 5,  1, now() - interval '29 day', 'Trastorno de ansiedad generalizada', 'Sintomas compatibles con TAG de intensidad moderada.', 'medio', 'Iniciar tecnicas de respiracion y seguimiento semanal.', 2, now() - interval '28 day'),
    (2, 6,  2, now() - interval '27 day', 'Episodio depresivo leve',            'Animo bajo persistente sin ideacion de riesgo.',        'bajo',  'Actividad fisica y registro de emociones diario.',       3, now() - interval '26 day'),
    (3, 7,  3, now() - interval '24 day', 'Estres cronico',                     'Estres laboral con somatizaciones.',                    'alto',  'Reorganizacion de carga y tecnicas de relajacion.',      4, now() - interval '23 day'),
    (4, 8,  4, now() - interval '21 day', 'Ansiedad situacional',               'Ansiedad asociada a eventos especificos.',              'bajo',  'Exposicion gradual y psicoeducacion.',                   2, now() - interval '20 day'),
    (5, 9,  5, now() - interval '19 day', 'Insomnio de conciliacion',           'Dificultad para iniciar el sueno.',                     'medio', 'Higiene del sueno y rutina nocturna.',                   3, now() - interval '18 day'),
    (6, 10, 6, now() - interval '17 day', 'Estres academico',                   'Sobrecarga academica y ansiedad por examenes.',         'medio', 'Planificacion de estudio y pausas activas.',             4, now() - interval '16 day'),
    (7, 11, 7, now() - interval '14 day', 'Trastorno de panico',                'Crisis de panico recurrentes.',                         'alto',  'Terapia cognitivo-conductual y seguimiento cercano.',    2, now() - interval '13 day')
ON CONFLICT (diagnostico_clinico_id) DO UPDATE SET
    usuario_id              = EXCLUDED.usuario_id,
    sesion_clinica_id       = EXCLUDED.sesion_clinica_id,
    fecha_diagnostico       = EXCLUDED.fecha_diagnostico,
    resultado_principal     = EXCLUDED.resultado_principal,
    descripcion_detallada   = EXCLUDED.descripcion_detallada,
    nivel_riesgo            = EXCLUDED.nivel_riesgo,
    recomendacion_general   = EXCLUDED.recomendacion_general,
    validacion_psicologo_id = EXCLUDED.validacion_psicologo_id,
    fecha_validacion        = EXCLUDED.fecha_validacion;

-- ---------------------------------------------------------------------
-- 5) Actividades recomendadas (datos listos para el GET)
--    status: PENDIENTE / COMPLETADA
-- ---------------------------------------------------------------------
INSERT INTO recommended_activities
    (recommended_activity_id, user_id, diagnosis_id, title, description, type,
     assigned_date, completed_date, status, feedback)
VALUES
    -- Paciente 5 (ana.torres)
    (1,  5,  1, 'Respiracion diafragmatica',   'Practicar respiracion profunda 10 minutos al dia.',          'RESPIRACION',  CURRENT_DATE - 25, NULL,               'PENDIENTE',  NULL),
    (2,  5,  1, 'Diario de ansiedad',          'Registrar situaciones que generan ansiedad.',                'ESCRITURA',    CURRENT_DATE - 24, CURRENT_DATE - 20,  'COMPLETADA', 'Me ayudo a identificar disparadores.'),
    (3,  5,  1, 'Caminata consciente',         'Caminar 20 minutos prestando atencion al entorno.',          'MINDFULNESS',  CURRENT_DATE - 10, NULL,               'PENDIENTE',  NULL),
    -- Paciente 6 (luis.garcia)
    (4,  6,  2, 'Rutina de ejercicio',         'Realizar 30 minutos de actividad fisica 3 veces por semana.','EJERCICIO',    CURRENT_DATE - 22, CURRENT_DATE - 15,  'COMPLETADA', 'Mejoro mi estado de animo.'),
    (5,  6,  2, 'Registro de emociones',       'Anotar una emocion positiva cada dia.',                      'ESCRITURA',    CURRENT_DATE - 8,  NULL,               'PENDIENTE',  NULL),
    -- Paciente 7 (maria.lopez)
    (6,  7,  3, 'Tecnica de relajacion muscular','Relajacion muscular progresiva antes de dormir.',          'RELAJACION',   CURRENT_DATE - 20, NULL,               'PENDIENTE',  NULL),
    (7,  7,  3, 'Pausas activas',              'Tomar pausas de 5 minutos cada hora de trabajo.',            'HABITOS',      CURRENT_DATE - 18, CURRENT_DATE - 12,  'COMPLETADA', 'Reduje la tension durante la jornada.'),
    -- Paciente 8 (diego.ramos)
    (8,  8,  4, 'Exposicion gradual',          'Enfrentar progresivamente situaciones que generan ansiedad.','TERAPIA',      CURRENT_DATE - 16, NULL,               'PENDIENTE',  NULL),
    -- Paciente 9 (sofia.chavez)
    (9,  9,  5, 'Higiene del sueno',           'Evitar pantallas una hora antes de dormir.',                 'HABITOS',      CURRENT_DATE - 14, NULL,               'PENDIENTE',  NULL),
    (10, 9,  5, 'Rutina nocturna',             'Establecer un horario fijo para acostarse.',                 'HABITOS',      CURRENT_DATE - 13, CURRENT_DATE - 6,   'COMPLETADA', 'Concilio el sueno mas rapido.'),
    -- Paciente 10 (pedro.silva)
    (11, 10, 6, 'Planificacion de estudio',    'Dividir el estudio en bloques con descansos.',               'ORGANIZACION', CURRENT_DATE - 12, NULL,               'PENDIENTE',  NULL),
    -- Paciente 11 (valeria.diaz)
    (12, 11, 7, 'Tecnica de aterrizaje 5-4-3-2-1','Usar los sentidos para manejar crisis de panico.',        'MINDFULNESS',  CURRENT_DATE - 10, NULL,               'PENDIENTE',  NULL),
    (13, 11, 7, 'Reestructuracion cognitiva',  'Cuestionar pensamientos catastroficos por escrito.',         'TERAPIA',      CURRENT_DATE - 9,  CURRENT_DATE - 3,   'COMPLETADA', 'Disminuyo la intensidad de las crisis.')
ON CONFLICT (recommended_activity_id) DO UPDATE SET
    user_id        = EXCLUDED.user_id,
    diagnosis_id   = EXCLUDED.diagnosis_id,
    title          = EXCLUDED.title,
    description    = EXCLUDED.description,
    type           = EXCLUDED.type,
    assigned_date  = EXCLUDED.assigned_date,
    completed_date = EXCLUDED.completed_date,
    status         = EXCLUDED.status,
    feedback       = EXCLUDED.feedback;

-- ---------------------------------------------------------------------
-- 6) Reajustar las secuencias de autoincremento tras insertar IDs explicitos
-- ---------------------------------------------------------------------
DO $$
DECLARE seq_name text;
BEGIN
    seq_name := pg_get_serial_sequence('users', 'id_user');
    IF seq_name IS NOT NULL THEN
        PERFORM setval(seq_name, (SELECT COALESCE(MAX(id_user), 1) FROM users));
    END IF;

    seq_name := pg_get_serial_sequence('sesiones_clinicas', 'sesion_clinica_id');
    IF seq_name IS NOT NULL THEN
        PERFORM setval(seq_name, (SELECT COALESCE(MAX(sesion_clinica_id), 1) FROM sesiones_clinicas));
    END IF;

    seq_name := pg_get_serial_sequence('diagnosticos_clinicos', 'diagnostico_clinico_id');
    IF seq_name IS NOT NULL THEN
        PERFORM setval(seq_name, (SELECT COALESCE(MAX(diagnostico_clinico_id), 1) FROM diagnosticos_clinicos));
    END IF;

    seq_name := pg_get_serial_sequence('recommended_activities', 'recommended_activity_id');
    IF seq_name IS NOT NULL THEN
        PERFORM setval(seq_name, (SELECT COALESCE(MAX(recommended_activity_id), 1) FROM recommended_activities));
    END IF;
END $$;
