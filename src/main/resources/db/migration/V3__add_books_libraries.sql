ALTER TABLE "books" DROP CONSTRAINT "books_fk0";
ALTER TABLE "books" DROP COLUMN "library_id";
ALTER TABLE "library" RENAME TO libraries;

CREATE TABLE "books_libraries" (
	"book_id" uuid NOT NULL ,
	"library_id" uuid NOT NULL ,
	"version" bigint,
	PRIMARY KEY(book_id, library_id)
);


ALTER TABLE "books_libraries" ADD CONSTRAINT "books_libraries_fk0" FOREIGN KEY ("book_id") REFERENCES "books"("id");
ALTER TABLE "books_libraries" ADD CONSTRAINT "books_libraries_fk1" FOREIGN KEY ("library_id") REFERENCES "libraries"("id");
