-- Table: public.movement_items

CREATE TABLE public.movement_items (
    id integer NOT NULL,
    movement_id integer NOT NULL,
    name character varying(255) NOT NULL,
    quantity double precision DEFAULT 0.0 NOT NULL,
    price double precision DEFAULT 0.0 NOT NULL
);

ALTER TABLE public.movement_items OWNER TO postgres;

CREATE SEQUENCE public.movement_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.movement_items_id_seq OWNER TO postgres;

ALTER SEQUENCE public.movement_items_id_seq OWNED BY public.movement_items.id;

ALTER TABLE ONLY public.movement_items ALTER COLUMN id SET DEFAULT nextval('public.movement_items_id_seq'::regclass);

ALTER TABLE ONLY public.movement_items ADD CONSTRAINT movement_items_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.movement_items ADD CONSTRAINT movement FOREIGN KEY (movement_id) REFERENCES movements (id) MATCH FULL;