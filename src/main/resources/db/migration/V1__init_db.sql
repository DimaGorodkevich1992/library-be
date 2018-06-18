--CREATE EXTENSION pgcrypto;

CREATE TABLE "books" (
	"id" UUID PRIMARY KEY ,
	"name" character varying(256) NOT NULL unique,
	"author" character varying(256) NOT NULL,
	"published" TIMESTAMP NOT NULL,
	"number_pages" int4 NOT NULL,
	"version" bigint,
	"library_id" UUID NOT NULL
	)




