package Controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class LoadAvatarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dirName = req.getParameter("login");
        String path = "C:\\Users\\Сайида\\IdeaProjects\\MyServer\\src\\main\\resources";
        File dir = new File(UserRepositoryServlet.DIR_AVATARS);
        if (dir.exists()) {
            File file = new File(dir + "\\" + dirName + ".jpeg");
            if (file.exists()) {
                resp.setContentLength((int) file.length());
                resp.setContentType("image/jpeg");
                resp.setBufferSize((int) file.length());
                FileInputStream fin = new FileInputStream(file);
                byte[] bytes = new byte[fin.available()];
                fin.read(bytes);
                fin.close();
                resp.getOutputStream().write(bytes);
                resp.flushBuffer();
            }
        }
    }
}
