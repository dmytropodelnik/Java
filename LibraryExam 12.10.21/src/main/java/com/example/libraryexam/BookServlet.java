package com.example.libraryexam;

import org.json.simple.parser.JSONParser;
import models.Book;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Db;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@WebServlet("/books")
@MultipartConfig  // !!1 Без этого multipart/form-data не работает
public class BookServlet extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bookId = req.getParameter( "id" ) ;
        JSONObject answer = new JSONObject() ;
        if( bookId == null || "".equals( bookId ) ) {
            answer.put( "status", "-1" ) ;
            answer.put( "message", "Id required" ) ;
        } else {
            // Удаление из БД
            if (Db.getBookOrm().deleteBook(bookId)) {
                answer.put( "status", "1" ) ;
                answer.put( "message", bookId ) ;
            } else {
                answer.put( "status", "-2" ) ;
                answer.put( "message", "Cannot delete book" ) ;
            }
        }
        resp.setContentType( "application/json" ) ;
        resp.getWriter().print( answer.toString() ) ;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Данные, передаваемые не в формате <form>
        // не разбираются в req.getParameter
        resp.setContentType( "application/json" ) ;
        try {
            InputStream reader = req.getInputStream() ;
            int sym ;
            StringBuilder sb = new StringBuilder() ;
            while( ( sym = reader.read() ) != -1 ) {
                sb.append( (char) sym ) ;
            }
            String body = new String(
                    sb.toString().getBytes(
                            StandardCharsets.ISO_8859_1 ),
                    StandardCharsets.UTF_8
            ) ;
            // error - testing
            if( body.contains( "?" ) ) {
                resp.getWriter().print( "{\"status\":-3}" ) ;
                return ;
            }
            JSONObject params = (JSONObject)
                    new JSONParser().parse( body ) ;
            if( Db.getBookOrm().updateBook( new Book(
                    (String) params.get( "id" ),
                    (String) params.get( "title" ),
                    (String) params.get( "author" ),
                    null
            ) ) ) {
                resp.getWriter().print( "{\"status\":1}" ) ;
            } else {
                resp.getWriter().print( "{\"status\":-1}" ) ;
            }
        } catch( Exception ex ) {
            System.err.println( "BookServlet(PUT): " + ex.getMessage() ) ;
            resp.getWriter().print( "{\"status\":-2}" ) ;
        }
    }

    @Override
    // API style - returns JSON of Book[]
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType( "application/json" ) ;
        resp.getWriter().print(
                new JSONArray(
                        Db.getBookOrm().getBooks()
                ).toString()
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // author, title, cover(file)
        String author = req.getParameter("author");
        String title = req.getParameter("title");
        Part cover = req.getPart("cover");
        int resultStatus;
        String resultMessage;
        // TODO data validation
        if (author == null || author.length() < 2) {
            resultStatus = -1;
            resultMessage = "Author empty or too short";
        } else if (title == null || title.length() < 2) {
            resultStatus = -2;
            resultMessage = "Title empty or too short";
        } else if (cover.getSize() == 0) {
            resultStatus = -3;
            resultMessage = "Cover file required";
        } else {
            String savedName = moveUploadedFile(cover, true);
            if (savedName == null) {
                resultStatus = -4;
                resultMessage = "Cover save error";
            } else {
                if (Db.getBookOrm().addBook(
                        new Book(
                                author,
                                title,
                                savedName
                        ))
                ) {
                    resultStatus = 1;
                    resultMessage = author + " " + title + " " + savedName;
                } else {
                    resultStatus = -5;
                    resultMessage = "Book store error";
                }
            }
        }

        JSONObject result = new JSONObject();
        result.put("status", resultStatus);
        result.put("message", resultMessage);

        resp.setContentType("application/json");
        resp.getWriter().print(result.toString());
    }

    /**
     * Save uploaded file, optionally make developer's copy
     *
     * @param filePart    HttpPart with file
     * @param makeDevCopy true - to make copy in project folder
     * @return saved file name / null
     */
    private String moveUploadedFile(Part filePart, boolean makeDevCopy) {
        if (filePart.getSize() == 0) {
            System.err.println("moveUploadedFile: size - 0");
            return null;
        }
        String hostingFolder =
                this.getServletContext().getRealPath("/uploads") + "/";
        String devFolder = "D:\\JAVA ITSTEP\\GIT\\LibraryExam 12.10.21\\src\\main\\webapp\\uploads\\";
        String uploadedFilename = null;
        try {
            uploadedFilename = filePart.getSubmittedFileName();
        } catch (Exception ignored) {
            String contentDisposition =
                    filePart.getHeader("content-disposition");
            if (contentDisposition != null) {
                for (String part : contentDisposition.split("; ")) {
                    if (part.startsWith("filename")) {
                        uploadedFilename = part.substring(10, part.length() - 1);
                        break;
                    }
                }
            }
        }
        if (uploadedFilename == null) {
            System.err.println("moveUploadedFile: filename extracting error");
            return null;
        }
        int extPosition = uploadedFilename.lastIndexOf(".");
        if (extPosition == -1) {
            System.err.println("moveUploadedFile: filename without extension");
            return null;
        }
        String fileExtension = uploadedFilename.substring(extPosition);
        String initFileName = uploadedFilename.substring(0, extPosition);
        String fileName;
        int counter = 1;
        File file;
        do {
            fileName = "_" + initFileName + "(" + counter + ")" + fileExtension;
            file = new File(hostingFolder + fileName);
            ++counter;
        } while (file.exists());
        try {
            Files.copy(
                    filePart.getInputStream(),
                    file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            if (makeDevCopy) {
                Files.copy(
                        filePart.getInputStream(),
                        new File(devFolder + fileName).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } catch (IOException ex) {
            System.err.println("moveUploadedFile: " + ex.getMessage());
            return null;
        }
        return fileName;
    }
}
