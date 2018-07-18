ALTER TABLE "books_libraries" DROP CONSTRAINT "books_libraries_fk0";
ALTER TABLE "books_libraries" DROP CONSTRAINT "books_libraries_fk1";

ALTER TABLE "books_libraries" ADD CONSTRAINT "books_libraries_fk0" FOREIGN KEY ("book_id") REFERENCES "books"("id") ON DELETE CASCADE;
ALTER TABLE "books_libraries" ADD CONSTRAINT "books_libraries_fk1" FOREIGN KEY ("library_id") REFERENCES "libraries"("id") ON DELETE CASCADE;