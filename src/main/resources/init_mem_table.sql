DROP SCHEMA IF EXISTS bot_database cascade;
CREATE SCHEMA IF NOT EXISTS bot_database;

CREATE TABLE
    mem_text (
                    id SERIAL NOT NULL PRIMARY KEY,
                    text_data TEXT
);

INSERT INTO mem_text (text_data) VALUES
    ('Вечеринка после тридцати: «Все лекарства уложены? Значит можно идти.'),
    ('Семейное положение: нет даже кота'),
    ('Я легко втираюсь в доверие. Я кот.'),
    ('Лучше быть последним — первым, чем первым — последним.'),
    ('Он силён, как бык. И почти так же умён.'),
    ('Если волк молчит то лучше его не перебивай.'),
    ('Сегодня я боролся с тупостью. И тупость победила.'),
    ('Меня постоянно преследуют умные мысли, но я быстрее….'),
    ('Иногда жизнь — это жизнь, а ты в ней иногда.');