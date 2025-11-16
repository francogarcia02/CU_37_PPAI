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
INSERT INTO T_EMPLEADO (id_empleado, nombre_empleado, apellido_empleado, rol_empleado, mail, telefono) VALUES (1, 'Agustin', 'Bieber', 'RESPONSABLE_INSPECCIONES', 'agustinbieber@gmail.com', '3512345671');
INSERT INTO T_EMPLEADO (id_empleado, nombre_empleado, apellido_empleado, rol_empleado, mail, telefono) VALUES (2, 'Jane', 'Doe', 'RESPONSABLE_REPARACIONES', 'janeDoe@gmail.com', '3517654321');

-- Sismógrafos
INSERT INTO T_SISMOGRAFO (id_sismografo, fecha_adquisicion, numero_serie, fabricante, modelo, id_estado) VALUES (1, '2024-12-12', 12, 'ZETLAB', 'Modelo 1', 1);
INSERT INTO T_SISMOGRAFO (id_sismografo, fecha_adquisicion, numero_serie, fabricante, modelo, id_estado) VALUES (2, '2024-12-12', 11, 'ZETLAB', 'Modelo 1', 1);

-- Estaciones Sismológicas
INSERT INTO T_ESTACION_SISMOLOGICA (id_estacion, nombre_estacion, latitud, longitud, nro_certificacion_adquisicion, fecha_solicitud_certificacion, documento_certificacion_adq, id_sismografo) VALUES (1, 'Estación Sierra de la Invernada', 10, 20, 5, '2024-12-20', 'Documentacion', 1);
INSERT INTO T_ESTACION_SISMOLOGICA (id_estacion, nombre_estacion, latitud, longitud, nro_certificacion_adquisicion, fecha_solicitud_certificacion, documento_certificacion_adq, id_sismografo) VALUES (2, 'Estación San Luis', 11, 23, 20, '2023-03-13', 'Documentacion', 2);

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