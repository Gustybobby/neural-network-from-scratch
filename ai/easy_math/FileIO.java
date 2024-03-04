package easy_math;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileIO {
    public static void recordToCSV(String path, String filename, double[] data) {
        try {
            Files.createDirectories(Paths.get(path));
            PrintWriter pw = new PrintWriter(new File(path + "/" + filename));
            for (double num : data) {
                pw.println(num);
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
