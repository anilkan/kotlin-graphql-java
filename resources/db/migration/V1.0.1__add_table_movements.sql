-- Table: public.movements

CREATE TABLE public.movements (
    id integer NOT NULL,
    type public.movementtype NOT NULL,
    "date" timestamp NOT NULL,
    "from" integer NOT NULL,
    "to" integer NOT NULL
);

ALTER TABLE public.movements OWNER TO postgres;

CREATE SEQUENCE public.movements_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.movements_id_seq OWNER TO postgres;

ALTER SEQUENCE public.movements_id_seq OWNED BY public.movements.id;

ALTER TABLE ONLY public.movements ALTER COLUMN id SET DEFAULT nextval('public.movements_id_seq'::regclass);

ALTER TABLE ONLY public.movements ADD CONSTRAINT movements_pkey PRIMARY KEY (id);