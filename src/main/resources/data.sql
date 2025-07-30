-- Insertando Usuarios
INSERT INTO app_user (user_name, password) VALUES ('mandalorian', 'thisIsTheWay');

--Insertando Generos
INSERT INTO gender (id, label) VALUES
(1, 'male'),
(2, 'female'),
(3, 'n/a'),
(4, 'none'),
(5, 'hermaphrodite'),
(6, 'unknown');


--Insertando Planetas
INSERT INTO planet (id, name) VALUES
(1, 'Tatooine'),
(2, 'Alderaan'),
(3, 'Naboo'),
(4, 'Kamino'),
(5, 'Endor');


--Insertando Películas
INSERT INTO film (id, title) VALUES
(1, 'A New Hope'),
(2, 'The Empire Strikes Back'),
(3, 'Return of the Jedi'),
(4, 'The Phantom Menace'),
(5, 'Attack of the Clones'),
(6, 'Revenge of the Sith'),
(7, 'The Force Awakens');


--Insertando Personajes
INSERT INTO character (name, height, mass, hair_color, gender_id, homeworld_id, dob, img) VALUES
('Luke Skywalker', 172.00, 77.00, 'blond', 1, 1, '19BBY',''),
('C-3PO', 167.00, 75.00, 'n/a', 3, 1, '112BBY',''),
('R2-D2', 96.00, 32.00, 'n/a', 3, 3, '33BBY',''),
('Darth Vader', 202.00, 136.00, 'none', 1, 1, '41.9BBY',''),
('Leia Organa', 150.00, 49.00, 'brown', 2, 2, '19BBY',''),
('Owen Lars', 178.00, 120.00, 'brown, grey', 1, 1, '52BBY',''),
('Beru Whitesun lars', 165.00, 75.00, 'brown', 2, 1, '47BBY',''),
('R5-D4', 97.00, 32.00, 'n/a', 3, 1, 'unknown',''),
('Biggs Darklighter', 183.00, 84.00, 'black', 1, 1, '24BBY',''),
('Obi-Wan Kenobi', 182.00, 77.00, 'auburn, white', 1, 1,'57BBY','');

--Películas y Personajes
INSERT INTO character_film (character_id, film_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 6),(1, 7),
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7),
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7),
(4, 3), (4, 5), (4, 6),
(5, 1), (5, 2), (5, 3), (5, 7),
(6, 2), (6, 3),
(7, 2), (7, 3),
(8, 1),
(9, 1),
(10, 1), (10, 2), (10, 3), (10, 4), (10, 5), (10, 6);






