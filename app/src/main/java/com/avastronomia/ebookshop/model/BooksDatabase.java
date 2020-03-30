package com.avastronomia.ebookshop.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Category.class, Book.class}, version = 1)
public abstract class BooksDatabase extends RoomDatabase {

    private static BooksDatabase instance;

    public abstract CategoryDAO categoryDAO();
    public abstract BookDAO bookDAO();

    public static synchronized BooksDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), BooksDatabase.class, "books_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InitialDataAsyncTask(instance).execute();
        }
    };

    private static class InitialDataAsyncTask extends AsyncTask<Void, Void, Void>{
        private CategoryDAO categoryDao;
        private BookDAO bookDAO;

        public InitialDataAsyncTask(BooksDatabase booksDatabase) {
            categoryDao = booksDatabase.categoryDAO();
            bookDAO = booksDatabase.bookDAO();

            Category category1 = new Category();
            category1.setCategoryName("Text Books");
            category1.setCategoryDescription("Text Books Description");

            Category category2 = new Category();
            category2.setCategoryName("Novels");
            category2.setCategoryDescription("Novels Description");

            Category category3 = new Category();
            category3.setCategoryName("Other Books");
            category3.setCategoryDescription("Text Books Description");

            categoryDao.insert(category1);
            categoryDao.insert(category2);
            categoryDao.insert(category3);

            Book book1 = new Book();
            book1.setBookName("High school Java ");
            book1.setUnitPrice("$150");
            book1.setCategoryId(1);

            Book book2 = new Book();
            book2.setBookName("Mathematics for beginners");
            book2.setUnitPrice("$200");
            book2.setCategoryId(1);

            Book book3 = new Book();
            book3.setBookName("Object Oriented Android App Design");
            book3.setUnitPrice("$150");
            book3.setCategoryId(1);

            Book book4 = new Book();
            book4.setBookName("A Game of Cats");
            book4.setUnitPrice("$19.99");
            book4.setCategoryId(2);

            bookDAO.insert(book1);
            bookDAO.insert(book2);
            bookDAO.insert(book3);
            bookDAO.insert(book4);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
