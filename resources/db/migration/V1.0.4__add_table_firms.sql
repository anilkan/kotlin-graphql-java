-- Table.firms

CREATE TABLE public.firms (
    id integer NOT NULL,
    name character varying(255) NOT NULL
);

ALTER TABLE public.firms OWNER TO postgres;

CREATE SEQUENCE public.firms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.firms_id_seq OWNER TO postgres;

ALTER SEQUENCE public.firms_id_seq OWNED BY public.firms.id;

ALTER TABLE ONLY public.firms ALTER COLUMN id SET DEFAULT nextval('public.firms_id_seq'::regclass);

ALTER TABLE ONLY public.firms ADD CONSTRAINT firms_pkey PRIMARY KEY (id);