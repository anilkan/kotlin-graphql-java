-- Table.safes

CREATE TABLE public.safes (
    id integer NOT NULL,
    code character varying(24) NOT NULL,
    name character varying(255) NOT NULL,
    balance double precision DEFAULT 0.0 NOT NULL
);

ALTER TABLE public.safes OWNER TO postgres;

CREATE SEQUENCE public.safes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.safes_id_seq OWNER TO postgres;

ALTER SEQUENCE public.safes_id_seq OWNED BY public.safes.id;

ALTER TABLE ONLY public.safes ALTER COLUMN id SET DEFAULT nextval('public.safes_id_seq'::regclass);

ALTER TABLE ONLY public.safes ADD CONSTRAINT safes_pkey PRIMARY KEY (id);