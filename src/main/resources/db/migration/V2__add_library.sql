CREATE TABLE "library" (
	"id" uuid PRIMARY KEY,
	"name" character varying(256) NOT NULL UNIQUE,
	"address" character varying(256) NOT NULL,
	"version" bigint NOT NULL
);

ALTER TABLE "books" ADD CONSTRAINT "books_fk0" FOREIGN KEY ("library_id") REFERENCES "library"("id");
