-- Funcionário --

INSERT INTO pessoa (nome, email, telefone)VALUES('Fabian Souza Castelo', 'fabiancasteloap18@gmail.com', '(96)98809-9093');

INSERT INTO login (pessoa_id_pessoa, usuario, senha)VALUES(1, 'Fabian', '210220');

INSERT INTO funcionario(pessoa_id_pessoa, matricula, cargo)VALUES(1, 'MAT100999', 'ADM');

INSERT INTO usuario(pessoa_id_pessoa)VALUES(1);

SELECT * FROM login;

SELECT u.pessoa_id_pessoa as ID, l.usuario AS Usuario, p.nome AS Nome, p.email AS Email, p.telefone AS telefone
from usuario u
JOIN login l
JOIN pessoa p
ON u.pessoa_id_pessoa = p.id_pessoa;

SELECT u.pessoa_id_pessoa as ID, cargo AS Cargo, p.nome AS Nome, p.email AS Email, p.telefone AS telefone, matricula AS Matricula
from funcionario u
JOIN pessoa p
ON u.pessoa_id_pessoa = p.id_pessoa;

INSERT INTO livro(titulo, autor, isbn, anoPublicacao)VALUES('Aeron - Os Cinco Aventureiros', 'L.C Castro & F.S Castelo', '299900', '2019-09-20');

ALTER TABLE login MODIFY COLUMN senha VARCHAR(20) NOT NULL;
SHOW INDEX FROM login;
ALTER TABLE login DROP INDEX usuario;

UPDATE livro SET titulo = ?, autor = ?, anoPublicacao = ? WHERE isbn = ?;

SELECT * FROM pessoa;

-- Inserir na tabela pessoa
INSERT INTO pessoa (nome, email, telefone)
VALUES ('Carlos Gerente', 'carlos.gerente@email.com', '11999999999');

-- Suponha que o ID da pessoa inserida foi 10
-- Inserir na tabela funcionario
INSERT INTO funcionario (matricula, cargo, pessoa_id_pessoa)
VALUES ('MAT123', 'Gerente', 3);

-- Criar login
INSERT INTO login (usuario, senha, pessoa_id_pessoa)
VALUES ('carlos.gerente', '123456', 3);

-- Inserir na tabela pessoa
INSERT INTO pessoa (nome, email, telefone)
VALUES ('Ana Bibliotecária', 'ana.biblio@email.com', '11888888888');

-- Suponha que o ID da pessoa inserida foi 11
-- Inserir na tabela funcionario
INSERT INTO funcionario (matricula, cargo, pessoa_id_pessoa)
VALUES ('MAT124', 'Bibliotecário', 4);

-- Criar login
INSERT INTO login (usuario, senha, pessoa_id_pessoa)
VALUES ('ana.biblio', '123456', 4);

CREATE TABLE IF NOT EXISTS `jp_Biblioteca`.`emprestimo` (
  `id_emprestimo` INT NOT NULL AUTO_INCREMENT,
  `data_emp` DATE NOT NULL,
  `usuario_id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_emprestimo`),
  INDEX `fk_emprestimo_usuario1_idx` (`usuario_id_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_emprestimo_usuario1`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `jp_Biblioteca`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jp_Biblioteca`.`livro_emprestimo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jp_Biblioteca`.`livro_emprestimo` (
  `data_prevista` DATE NULL,
  `data_devolucao` DATE NULL,
  `id_livro` INT NOT NULL,
  `id_emprestimo` INT NOT NULL,
  INDEX `fk_livro_emprestimo_livro1_idx` (`id_livro` ASC) VISIBLE,
  INDEX `fk_livro_emprestimo_emprestimo1_idx` (`id_emprestimo` ASC) VISIBLE,
  CONSTRAINT `fk_livro_emprestimo_livro1`
    FOREIGN KEY (`id_livro`)
    REFERENCES `jp_Biblioteca`.`livro` (`id_livro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_livro_emprestimo_emprestimo1`
    FOREIGN KEY (`id_emprestimo`)
    REFERENCES `jp_Biblioteca`.`emprestimo` (`id_emprestimo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SELECT e.id_emprestimo, u.id_usuario, l.usuario, data_emp, data_prevista, data_devolucao FROM livro_emprestimo JOIN emprestimo e JOIN login l JOIN usuario u on u.id_usuario = usuario_id_usuario