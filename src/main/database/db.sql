create database if not exists Violet_Descent_Game;

use Violet_Descent_Game;

create table leader_board(
	game_id int auto_increment primary key,
    player_name varchar(10),
    score int
);


create table saved_games(
	game_save_id int auto_increment primary key,
    game_level int,
    run_time double,
    health double
);

INSERT INTO saved_games (save_name, game_level, run_time, health) VALUES 
('Checkpoint', 1, 45.5, 3.0),
('Midnight', 3, 120.2, 2.5),
('BossRush', 5, 300.0, 1.0),
('SpeedRun', 2, 85.4, 3.0),
('Casual', 1, 30.1, 3.0),
('HardMode', 4, 210.8, 0.5),
('TheEnd', 10, 560.3, 1.5),
('TestRun', 1, 10.0, 3.0),
('LevelUP', 6, 400.7, 2.0),
('FinalSave', 8, 480.2, 2.5);

INSERT INTO saved_games (save_name, game_level, run_time, health) VALUES 
('Checkpoint', 44, 4555.5, 3.0);

delete from saved_games;
delete from leader_board;

alter table saved_games
drop column save_name;

DELIMITER $$

CREATE PROCEDURE PopulateDummyData()
BEGIN
    DECLARE i INT DEFAULT 1;
    
    WHILE i <= 100 DO
        -- 1. Populate leader_board
        -- Creates names like "Player1", "Player2"... (Max 10 chars)
        -- Random Score between 100 and 10000
        INSERT INTO leader_board (player_name, score) 
        VALUES (
            CONCAT('Player', i), 
            FLOOR(100 + (RAND() * 9900))
        );

        -- 2. Populate saved_games
        -- Random Level between 1 and 20
        -- Random Runtime between 10.0 and 600.0 seconds
        -- Random Health between 1 and 3 (Based on your Player class maxHealth=3)
        INSERT INTO saved_games (game_level, run_time, health) 
        VALUES (
            FLOOR(1 + (RAND() * 20)), 
            ROUND(10 + (RAND() * 590), 2), 
            FLOOR(1 + (RAND() * 3))
        );

        SET i = i + 1;
    END WHILE;
END$$

DELIMITER ;

-- Execute the procedure to insert data
CALL PopulateDummyData();

-- Clean up (remove the procedure after use)
DROP PROCEDURE PopulateDummyData;