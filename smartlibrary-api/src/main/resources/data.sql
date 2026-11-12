-- Datos iniciales de prueba.
-- Segun el alcance del taller, los estudiantes y libros ya estan registrados.

INSERT INTO estudiante (nombre, correo) VALUES ('Ana Gomez', 'ana.gomez@campusucc.edu.co');
INSERT INTO estudiante (nombre, correo) VALUES ('Luis Martinez', 'luis.martinez@campusucc.edu.co');

INSERT INTO libro (titulo, disponible) VALUES ('Diseno de Software', TRUE);
INSERT INTO libro (titulo, disponible) VALUES ('Patrones de Diseno', TRUE);
INSERT INTO libro (titulo, disponible) VALUES ('UML Distilled', FALSE);

-- Prestamo activo del libro 3 (sin fecha de devolucion): por eso el libro 3 no esta disponible.
INSERT INTO prestamo (fecha_prestamo, fecha_devolucion, estudiante_id, libro_id) VALUES (CURRENT_DATE, NULL, 2, 3);
