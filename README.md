# Java-filmorate
### Описание проекта.
Этот проект представляет собой социальную сеть с 
рекомендациями фильмов от друзей.

Версия языка: 17.

Зависимости: springframework, lombok, zalando, h2database, 

### Возможности.

- Создание учетной записи.
- Добавление фильмов.
- Добавление друзей.
- Лайки фильмам.

### Схема базы данных.
![Фото диаграмы БД](https://github.com/naumovMaksim/java-filmorate/blob/main/Untitled.png)

### Примеры запросов к базе данных.
1. Получить всех пользователей: 
```
SELECT * 
FROM Users;
```
2. Получить пользователя по id:
```
SELECT *
FROM Users
WHERE user_id = <id пользователя>;
```
3. Получить друзей пользователя:
```
SELECT *
FROM Users AS u
LEFT OUTER JOIN User_friends AS uf ON (u.user_id = uf.user_id);
```
4. Получить все фильмы:
```
SELECT *
FROM Films;
```
5. Получить фильм по id:
```
SELECT *
FROM Films
WHERE film_id = <id фильма>;
```
6. Получить жанр фильма:
```
SELECT name
FROM Genere AS g
LEFT OUTER JOIN Genre_film AS gf ON (g.genere_id = gf.genere_id)
WHERE gf.film_id = <id фильма>;
```
7. Получить рейтинг фильма:
```
SELECT name
FROM Rating AS r
LEFT OUTER JOIN Films AS f ON (r.rating_id = f.rating_id)
WHERE f.film_id = <id фильма>
```
8. Получить первые <N> самых популярных фильмов:
```
SELECT f.name
FROM Films AS f
RIGHT OUTER JOIN Film_likes AS fl ON (al.film_id = f.film_id) 
GROUP BY f.name 
ORDER BY COUNT(al.user_id) 
LIMIT <N>;
```
