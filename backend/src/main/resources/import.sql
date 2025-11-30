-- =================================================================
-- INSERTS PARA DATOS DE PRUEBA (SINTAXIS CORREGIDA PARA H2)
-- UNA SENTENCIA INSERT POR CADA FILA
-- =================================================================

-- Estados
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (1, 'SISMOGRAFO', 'enLinea');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (2, 'SISMOGRAFO', 'Inhabilitado');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (3, 'SISMOGRAFO', 'Fuera de Servicio');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (4, 'ORDEN_INSPECCION', 'PteRealización');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (5, 'ORDEN_INSPECCION', 'ParcialmenteRealizado');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (6, 'ORDEN_INSPECCION', 'Finalizado');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (7, 'ORDEN_INSPECCION', 'CierreDefinitivo');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (8, 'SISMOGRAFO', 'pteCertificacion');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (9, 'SISMOGRAFO', 'Disponible');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (10, 'SISMOGRAFO', 'enInstalacion');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (11, 'SISMOGRAFO', 'enRevicion');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (12, 'SISMOGRAFO', 'Descartado');
INSERT INTO T_ESTADO (id_estado, ambito, nombre) VALUES (13, 'SISMOGRAFO', 'enReparacion');

-- Empleados
INSERT INTO T_EMPLEADO (id_empleado, nombre_empleado, apellido_empleado, rol_empleado, mail, telefono) VALUES (1, 'Agustin', 'Bieber', 'RESPONSABLE_REPARACIONES', 'agustinbieber@gmail.com', '3512345671');
INSERT INTO T_EMPLEADO (id_empleado, nombre_empleado, apellido_empleado, rol_empleado, mail, telefono) VALUES (2, 'Jane', 'Doe', 'RESPONSABLE_REPARACIONES', 'janeDoe@gmail.com', '3517654321');
INSERT INTO T_EMPLEADO (id_empleado, nombre_empleado, apellido_empleado, rol_empleado, mail, telefono) VALUES (3, 'Lucas', 'Sanchez', 'RESPONSABLE_INSPECCIONES', 'lucassanchezqw@gmail.com', '3514667890');

-- Sismógrafos
INSERT INTO T_SISMOGRAFO (id_sismografo, fecha_adquisicion, numero_serie, fabricante, modelo, id_estado) VALUES (1, '2024-12-12', 12, 'ZETLAB', 'Modelo F3', 1);
INSERT INTO T_SISMOGRAFO (id_sismografo, fecha_adquisicion, numero_serie, fabricante, modelo, id_estado) VALUES (2, '2024-12-12', 11, 'ZETLAB', 'Modelo F3-A', 1);

-- Estaciones Sismológicas
INSERT INTO T_ESTACION_SISMOLOGICA (id_estacion, nombre_estacion, latitud, longitud, nro_certificacion_adquisicion, fecha_solicitud_certificacion, documento_certificacion_adq, id_sismografo) VALUES (1, 'Estación Bosque Alegre', -31.60581, -64.56881, 5, '2024-12-20', 'Documentacion', 1);
INSERT INTO T_ESTACION_SISMOLOGICA (id_estacion, nombre_estacion, latitud, longitud, nro_certificacion_adquisicion, fecha_solicitud_certificacion, documento_certificacion_adq, id_sismografo) VALUES (2, 'Estación Pilar', -31.66861, -63.88290, 20, '2023-03-13', 'Documentacion', 2);

-- Órdenes de Inspección
INSERT INTO T_ORDEN_INSPECCION (id_orden, id_estacion, id_responsable, observaciones_cierre) VALUES (1, 1, 1, 'No hay Observaciones todavía');
INSERT INTO T_ORDEN_INSPECCION (id_orden, id_estacion, id_responsable, observaciones_cierre) VALUES (2, 1, 1, 'No hay Observaciones todavía');
INSERT INTO T_ORDEN_INSPECCION (id_orden, id_estacion, id_responsable, observaciones_cierre) VALUES (3, 1, 1, 'No hay Observaciones todavía');

-- Cambios de Estado de las Órdenes
INSERT INTO T_CAMBIO_ESTADO (id_cambio_estado, id_orden, id_estado_anterior, id_estado_nuevo, fecha_hora_inicio, fecha_hora_fin, id_responsable_cambio) VALUES (1, 1, NULL, 4, DATEADD('DAY', -2, CURRENT_TIMESTAMP()), DATEADD('DAY', -1, CURRENT_TIMESTAMP()), 1);
INSERT INTO T_CAMBIO_ESTADO (id_cambio_estado, id_orden, id_estado_anterior, id_estado_nuevo, fecha_hora_inicio, fecha_hora_fin, id_responsable_cambio) VALUES (2, 1, 4, 5, DATEADD('DAY', -1, CURRENT_TIMESTAMP()), CURRENT_TIMESTAMP(), 1);
INSERT INTO T_CAMBIO_ESTADO (id_cambio_estado, id_orden, id_estado_anterior, id_estado_nuevo, fecha_hora_inicio, fecha_hora_fin, id_responsable_cambio) VALUES (3, 1, 5, 6, CURRENT_TIMESTAMP(), NULL, 1);
INSERT INTO T_CAMBIO_ESTADO (id_cambio_estado, id_orden, id_estado_anterior, id_estado_nuevo, fecha_hora_inicio, fecha_hora_fin, id_responsable_cambio) VALUES (4, 2, NULL, 4, CURRENT_TIMESTAMP(), NULL, 1);
INSERT INTO T_CAMBIO_ESTADO (id_cambio_estado, id_orden, id_estado_anterior, id_estado_nuevo, fecha_hora_inicio, fecha_hora_fin, id_responsable_cambio) VALUES (5, 3, NULL, 4, DATEADD('DAY', -1, CURRENT_TIMESTAMP()), CURRENT_TIMESTAMP(), 1);
INSERT INTO T_CAMBIO_ESTADO (id_cambio_estado, id_orden, id_estado_anterior, id_estado_nuevo, fecha_hora_inicio, fecha_hora_fin, id_responsable_cambio) VALUES (6, 3, 4, 5, CURRENT_TIMESTAMP(), NULL, 1);

-- Tipos de Motivo
INSERT INTO T_TIPO_MOTIVO (id_tipo_motivo, descripcion) VALUES (1, 'Avería por vibración');
INSERT INTO T_TIPO_MOTIVO (id_tipo_motivo, descripcion) VALUES (2, 'Desgaste de componente');
INSERT INTO T_TIPO_MOTIVO (id_tipo_motivo, descripcion) VALUES (3, 'Fallo en el sistema de registro');
INSERT INTO T_TIPO_MOTIVO (id_tipo_motivo, descripcion) VALUES (4, 'Vandalismo');
INSERT INTO T_TIPO_MOTIVO (id_tipo_motivo, descripcion) VALUES (5, 'Fallo en fuente de alimentación');

-- Update the sequence to start after the highest ID used
ALTER SEQUENCE IF EXISTS cambio_estado_seq RESTART WITH 7;

--- =================================================================
-- LIMPIEZA Y DATOS PARA LA DEMO (Reemplaza el bloque final con esto)
-- =================================================================

-- Borramos órdenes de prueba generadas anteriormente para empezar limpio
DELETE FROM T_CAMBIO_ESTADO WHERE id_orden > 200 OR id_orden = 2;
DELETE FROM T_ORDEN_INSPECCION WHERE id_orden > 200 OR id_orden = 2;

-- 1. ASEGURAR SISMÓGRAFOS Y ESTACIONES EXTRAS
INSERT INTO T_SISMOGRAFO (id_sismografo, fecha_adquisicion, numero_serie, fabricante, modelo, id_estado) VALUES (101, '2023-05-20', 9901, 'Sony', 'Modelo K5-A', 1);
INSERT INTO T_SISMOGRAFO (id_sismografo, fecha_adquisicion, numero_serie, fabricante, modelo, id_estado) VALUES (103, '2023-08-10', 9903, 'LG', 'Modelo K5', 1);

-- Estación 101 (Volcán) y 103 (Antártida)
INSERT INTO T_ESTACION_SISMOLOGICA (id_estacion, nombre_estacion, latitud, longitud, id_sismografo) VALUES (101, 'Estación Volcán Lanín', -39.6, -71.5, 101);
INSERT INTO T_ESTACION_SISMOLOGICA (id_estacion, nombre_estacion, latitud, longitud, id_sismografo) VALUES (103, 'Estación Base Marambio', -64.2, -56.6, 103);

-- 2. CREAR LAS 4 ÓRDENES (Todas FINALIZADAS para que se vean)

-- A. Orden 1 (Sierra de la Invernada - Ya existía, aseguramos estado)
-- (Asumimos que ya está insertada arriba, solo aseguramos el cambio de estado)
UPDATE T_CAMBIO_ESTADO SET fecha_hora_fin = CURRENT_TIMESTAMP() WHERE id_orden = 1;
INSERT INTO T_CAMBIO_ESTADO (id_cambio_estado, id_orden, id_estado_anterior, id_estado_nuevo, fecha_hora_inicio, fecha_hora_fin, id_responsable_cambio) VALUES (500, 1, 5, 6, CURRENT_TIMESTAMP(), NULL, 1);

-- B. Orden 204 (ESTACIÓN PILAR - La que faltaba)
INSERT INTO T_ORDEN_INSPECCION (id_orden, id_estacion, id_responsable, observaciones_cierre) VALUES (204, 2, 1, null);
INSERT INTO T_CAMBIO_ESTADO (id_cambio_estado, id_orden, id_estado_anterior, id_estado_nuevo, fecha_hora_inicio, fecha_hora_fin, id_responsable_cambio) VALUES (504, 204, 5, 6, CURRENT_TIMESTAMP(), NULL, 1);

-- C. Orden 201 (Volcán Lanín)
INSERT INTO T_ORDEN_INSPECCION (id_orden, id_estacion, id_responsable, observaciones_cierre) VALUES (201, 101, 1, null);
INSERT INTO T_CAMBIO_ESTADO (id_cambio_estado, id_orden, id_estado_anterior, id_estado_nuevo, fecha_hora_inicio, fecha_hora_fin, id_responsable_cambio) VALUES (501, 201, 5, 6, CURRENT_TIMESTAMP(), NULL, 1);

-- D. Orden 203 (Base Marambio)
INSERT INTO T_ORDEN_INSPECCION (id_orden, id_estacion, id_responsable, observaciones_cierre) VALUES (203, 103, 1, null);
INSERT INTO T_CAMBIO_ESTADO (id_cambio_estado, id_orden, id_estado_anterior, id_estado_nuevo, fecha_hora_inicio, fecha_hora_fin, id_responsable_cambio) VALUES (503, 203, 5, 6, CURRENT_TIMESTAMP(), NULL, 1);

ALTER SEQUENCE IF EXISTS cambio_estado_seq RESTART WITH 600;