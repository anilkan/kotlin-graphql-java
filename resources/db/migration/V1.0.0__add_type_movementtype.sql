-- Type: public.movementtype

CREATE TYPE public.movementtype AS ENUM (
    'EXPENSE',
    'INCOME'
);

ALTER TYPE public.movementtype OWNER TO postgres;