package Service;

import javax.imageio.ImageIO;

/**
 * Created by Сайида on 14.07.2017.
 */
public interface IFileService {
    void createDir(String path);
    String readFile(String path);
    void writeFile(String path, String text);

    void writeFile(String path, ImageIO image);
}
