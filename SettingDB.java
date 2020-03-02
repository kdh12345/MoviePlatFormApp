package org.androidtown.movieproject2.DBClasses;

public class SettingDB {
    private SettingDB(){};

    public static final String T_NAME = "MOVIE_TABLE";
    public static final String COL_ID = "ID";
    public static final String COL_MOVIE = "MOVIE";
    public static final String COL_DETAIL = "DETAIL";
    public static final String COL_SIMPLE_COMMENTS = "SIMPLE_COMMENTS";
    public static final String COL_COMMENTS = "COMMENTS";

    //second Table for the commets
    public static final String COMMENTS_T_NAME = "MOVIE_TABLE";
    public static final String COL_MOVIE_ID_FOREIGN = "MOVIE_ID";

    public static final String SQL_CREATE_COMMENTS_TABLE = "CREATE TABLE IF NOT EXISTS "+COMMENTS_T_NAME+" "+
            "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COL_MOVIE_ID_FOREIGN + " INTEGER, "+
            COL_COMMENTS + " TEXT, "+
            "FOREIGN KEY("+ COL_MOVIE_ID_FOREIGN +") REFERENCES "+T_NAME+"("+COL_ID+") ON DELETE CASCADE"+
            ")";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+T_NAME+" "+
            "(" +
            COL_ID + " INTEGER PRIMARY KEY NOT NULL, "+
            COL_MOVIE + " TEXT, "+
            COL_DETAIL + " TEXT, "+
            COL_SIMPLE_COMMENTS + " TEXT, "+
            COL_COMMENTS + " TEXT"+
            ")";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + T_NAME;

    public static final String SQL_SELECT = "SELECT * FROM " +T_NAME;

    //or replace
    public static final String SQL_INSERT = "INSERT OR IGNORE INTO " + T_NAME + " (" + COL_ID +", "
            + COL_MOVIE +", "+COL_DETAIL +", "+COL_SIMPLE_COMMENTS +", "+COL_COMMENTS +") VALUES ";

    public static final String SQL_DELETE = "DELETE FROM "+T_NAME ;
    public static final String SQL_UPDATE = "UPDATE "+T_NAME+" SET " ;
}
