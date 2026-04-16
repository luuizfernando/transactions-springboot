-- 1. Remover as chaves estrangeiras dependentes em tb_transactions
-- (Os nomes das FKs foram obtidos no log de erro anterior)
ALTER TABLE tb_transactions DROP CONSTRAINT IF EXISTS fk1vny5bmm72q3aa5c3hgegpjlq;
ALTER TABLE tb_transactions DROP CONSTRAINT IF EXISTS fk8y6l8pt67r27r1nbybwe15okh;

-- 2. Adicionar a coluna id em tb_accounts
ALTER TABLE tb_accounts ADD COLUMN id UUID DEFAULT gen_random_uuid();

-- 3. Atualizar tb_transactions para usar os novos IDs
-- Como user_id era a PK, podemos usá-lo para fazer o join e atualizar
UPDATE tb_transactions t
SET from_account_id = a.id
FROM tb_accounts a
WHERE t.from_account_id = a.user_id;

UPDATE tb_transactions t
SET to_account_id = a.id
FROM tb_accounts a
WHERE t.to_account_id = a.user_id;

-- 4. Trocar a PK de tb_accounts
ALTER TABLE tb_accounts DROP CONSTRAINT tb_accounts_pkey CASCADE;
ALTER TABLE tb_accounts ALTER COLUMN id SET NOT NULL;
ALTER TABLE tb_accounts ADD CONSTRAINT tb_accounts_pkey PRIMARY KEY (id);

-- 5. Garantir que user_id continua único
ALTER TABLE tb_accounts ADD CONSTRAINT tb_accounts_user_id_unique UNIQUE (user_id);

-- 6. Recriar as chaves estrangeiras em tb_transactions apontando para a nova PK (id)
ALTER TABLE tb_transactions 
    ADD CONSTRAINT fk_transactions_from_account 
    FOREIGN KEY (from_account_id) REFERENCES tb_accounts(id);

ALTER TABLE tb_transactions 
    ADD CONSTRAINT fk_transactions_to_account 
    FOREIGN KEY (to_account_id) REFERENCES tb_accounts(id);
