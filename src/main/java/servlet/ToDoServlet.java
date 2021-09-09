package servlet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.ToDo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "ToDoServlet",
        urlPatterns = {"/todo"}
)
public class ToDoServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(SerializationFeature.INDENT_OUTPUT);
    private final Map<Integer, ToDo> toDoMap = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        objectMapper.writer().writeValue(out, toDoMap.values());
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ToDo toDo = objectMapper.readValue(req.getInputStream(), ToDo.class);
        if (toDo.getId() <= 0) {
            toDo.setId(idGenerator.getAndIncrement());
            toDoMap.put(toDo.getId(), toDo);
        } else {
            toDoMap.computeIfPresent(toDo.getId(), (k, v) -> toDo);
        }
        ServletOutputStream out = resp.getOutputStream();
        objectMapper.writer().writeValue(out, toDo);
        out.flush();
        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("id");
        try {
            int idInt = Integer.parseInt(id);
            if (idInt > 0) {
                toDoMap.remove(idInt);
            }
        } catch (NumberFormatException e) {
            toDoMap.clear();
        }
        ServletOutputStream out = resp.getOutputStream();
        out.close();
    }
}