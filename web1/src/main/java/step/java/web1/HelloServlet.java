package step.java.web1;

import java.io.*;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");

        try {
            // Проверяем наличие в сессии атрибутов
            HttpSession session = request.getSession();
            String cellphoneMessage = (String) session.getAttribute("cellphoneMessage");
            if (cellphoneMessage != null) {  // есть в сессии
                // удаляем из сессии, иначе сообщение будет с каждым обновлением страницы
                session.removeAttribute("cellphoneMessage");
            } else {
                cellphoneMessage = "";
            }

            String usernameMessage = (String) session.getAttribute("usernameMessage");
            if (usernameMessage != null) {
                session.removeAttribute("usernameMessage");
            } else {
                usernameMessage = "";
            }

            String emailMessage = (String) session.getAttribute("emailMessage");
            if (emailMessage != null) {
                session.removeAttribute("emailMessage");
            } else {
                emailMessage = "";
            }

            // включаем сообщение в атрибуты запроса (для View)
            request.setAttribute("cellphoneMessage", cellphoneMessage);
            request.setAttribute("usernameMessage", usernameMessage);
            request.setAttribute("emailMessage", emailMessage);

            // Часть задачи по подстановке прошлых данных

            //  for (String attrName :
            //        new String[] {"cellValue", "nameValue"}) {
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String attrName = names.nextElement();
                if (attrName.endsWith("Value")) {
                    String theValue =
                            (String) session.getAttribute(attrName);
                    if (theValue != null) {
                        session.removeAttribute(attrName);
                    }
                    request.setAttribute(attrName,
                            (theValue == null) ? "" : theValue);
                }
            }

            // Загружаем представление (View)
            request
                    .getRequestDispatcher("hello_view.jsp")
                    .forward(request, response);
        } catch (ServletException ex) {
            System.out.println(ex.getMessage());
            // Hello
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cellphone = req.getParameter("cellular");
        String username = req.getParameter("username");
        String email = req.getParameter("email");

        String cellphoneMessage = "";
        if (cellphone == null || cellphone.isEmpty()) {
            cellphoneMessage = "Cellphone cannot be empty";
        } else {
            if (!cellphone.matches("^\\d+$")) {
                cellphoneMessage = "Cellphone must have only digits";
            }
        }

        String usernameMessage = "";
        if (username == null || username.isEmpty()) {
            usernameMessage = "Username cannot be empty";
        } else {
            if (!username.matches("^\\D+$")) {
                usernameMessage = "Username must have no digits";
            }
        }

        String emailMessage = "";
        if (email == null || email.isEmpty()) {
            emailMessage = "Email cannot be empty";
        } else {
            if (!email.matches("^[a-zA-Z]\\w{2,15}@([a-z]+\\.)+[a-z]{2,3}$")) {
                emailMessage = "Incorrect email";
            }
        }

        // HTTP сессия - способ хранения данных между запросами
        HttpSession session = req.getSession();
        session.setAttribute("cellphoneMessage", cellphoneMessage);
        session.setAttribute("usernameMessage", usernameMessage);
        session.setAttribute("emailMessage", emailMessage);

        // Задача: отображать на форме ранее введенные значения,
        // ЕСЛИ данные формы не приняты
        if (usernameMessage.length() > 0 || cellphoneMessage.length() > 0
                || emailMessage.length() > 0) {
            // есть сообщение(я) - валидация не прошла
            // сохраняем в сессию полученные значения
            session.setAttribute("cellValue", cellphone);
            session.setAttribute("nameValue", username);
            session.setAttribute("emailValue", email);
        }

        resp.sendRedirect(req.getRequestURI());
        // Клиент получит ответ со статусом 30х и Location: тот же адрес - Browser отправит запрос
        // на этот адрес методом GET !!!

        // !! На запросы POST нельзя строить View
        /*
        req.setAttribute("cellphoneMessage", cellphoneMessage);
        try {
            req.getRequestDispatcher("hello_view.jsp")
               .forward(req, resp);
        } catch (ServletException ex) {
            System.out.println(ex.getMessage());
        }
         */
    }

    public void destroy() {
    }
}