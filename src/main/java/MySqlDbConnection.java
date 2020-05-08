import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlDbConnection {
    private static final String url = "jdbc:mysql://localhost:3306/library?serverTimezone=Europe/Samara";
    private static final String user = "root";
    private static final String password = "root";

    public static final String booksTable = "library.books";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet resutlSet;

    public static void main(String[] args) {
        String query = "select count(*) from " + booksTable;
        String testTitle = "test title 123";
        addNewBook(testTitle, "test author", "test note");
        deleteBookByTitle(testTitle);

        List<Book> books = getAllBooks();
        System.out.print("[");
        for(Book it : books) {
            System.out.print(it.toString() + ", ");
        }
        System.out.print("]");

    }

    public static List<Book> getAllBooks() {
        String query = "SELECT * from " + booksTable;
        List<Book> books = new ArrayList<>();
        int count = 0;
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            resutlSet = stmt.executeQuery(query);
            while (resutlSet.next()) {
                Book book = new Book();
                book.setId(resutlSet.getInt("ID"));
                book.setTitle(resutlSet.getString("Title"));
                book.setAuthor(resutlSet.getString("Author"));
                book.setNote(resutlSet.getString("Note"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                resutlSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return books;
    }

    // note isn't required fields so need to rewrite.
    public static void addNewBook(String title, String author, String note) {
        String query = String.format("INSERT into %s (Title, Author, Note) values (?,?,?)", booksTable);

        try {
            con = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, title);
            preparedStmt.setString(2, author);
            preparedStmt.setString(3, note);
            preparedStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteBookByTitle(String title) {
        try {
            con = DriverManager.getConnection(url, user, password);
            PreparedStatement st = con.prepareStatement(String.format("DELETE FROM %s WHERE title = ?", booksTable));
            st.setString(1, title);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
