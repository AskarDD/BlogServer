package Controllers;

import Model.POJOs.Message;
import Model.dao.IUserDao;
import com.google.gson.Gson;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Created by Сайида on 14.07.2017.
 */
public class UserRepositoryServlet extends HttpServlet {
    static final String DIR_AVATARS = "D:\\Blog\\usersAvatar";
    @Autowired
    IUserDao iud;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String fileName = login + ".jpeg";
        boolean dirCreated = true;
        File resDirectory = new File(DIR_AVATARS);
        if (!resDirectory.exists()){
            dirCreated = resDirectory.mkdir();
        }
        if (!dirCreated){
            createDir(DIR_AVATARS);
        }
        if (!resDirectory.exists())
            dirCreated = resDirectory.mkdir();
        if (dirCreated){
            String path = resDirectory + "\\" + fileName;
            File file = new File(path);
            boolean fileCreated = true;
            if (!file.exists())
                fileCreated = file.createNewFile();
            if (fileCreated){
                BufferedImage input = ImageIO.read(req.getInputStream());
                ImageIO.write(input, "jpeg", file);
                input.flush();
                iud.uploadImageUri(login, file.getAbsolutePath());

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

    private boolean createDir(String path){
        String[] arrDir = path.split("\\+");
        if (arrDir.length > 2) {
            String root = arrDir[0].trim();
            for (String string : arrDir) {
                if (!string.trim().isEmpty()) {
                    root += "\\" + string;
                    File dir = new File(root);
                    if (dir.exists())
                        continue;
                    else
                        dir.mkdir();
                    if (!dir.exists())
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }
}
