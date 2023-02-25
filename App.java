import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static String path, filehash;
    private static List<String> dirhash = new ArrayList<>();

    private static String gethash(MessageDigest md, File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);       
        byte[] byteArray = new byte[1024];
        int bytesCount = 0; 
        while ((bytesCount = fis.read(byteArray)) != -1) {
            md.update(byteArray, 0, bytesCount);
        };
        fis.close();
        byte[] bytes = md.digest();

        String hashstring = "";
        for(int i=0; i< bytes.length ;i++)
        {
            hashstring += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return hashstring;
    }

    private static void searchfile(File directory, String extn, MessageDigest md5) {
        File[] list = directory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                try {
                    if (file.isFile() && file.getName().endsWith(extn) && (filehash.compareTo(gethash(md5, file)) == 0)) return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        if (list != null) {
            for (int i=0 ; i<list.length ; i++) {
                if (path.compareTo(list[i] + "") != 0)
                System.out.println(list[i]);
            }
        }
        File[] files = directory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        if (files != null)
        for (int i=0 ; i<files.length ; i++) {
            searchfile(files[i], extn, md5);
        }
    }

    private static void directoryhash(File dir, MessageDigest md) throws IOException {
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        for (int i=0 ; i<files.length ; i++) {
            dirhash.add(gethash(md, files[i]));
        }
        File[] directories = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        for (int i=0 ; i<directories.length ; i++) {
            directoryhash(directories[i], md);
        }
    }

    private static boolean checkdirectory(File dir, MessageDigest md, int index, boolean flag) throws IOException {
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        for (int i=0 ; i<files.length ; i++) {
            if (gethash(md, files[i]).compareTo(dirhash.get(index)) == 0) index++;
            else return false;
        }
        File[] directories = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        for (int i=0 ; i<directories.length ; i++) {
            flag = checkdirectory(directories[i], md, index, flag);
            if (flag == false) return flag;
        }
        return flag;
    }

    private static void searchdirectory(File directory, int dirlen, MessageDigest md5) throws IOException {
        File[] list = directory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (file.isDirectory() && file.list().length == dirlen) return true;
                return false;
            }
        });
        if (list != null)
        for (int i=0 ; i<list.length ; i++) {
            if (checkdirectory(list[i], md5, 0, true) == true && (path.compareTo(list[i] + "") != 0))
            System.out.println(list[i]);
        }
        File[] files = directory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        if (files != null)
        for (int i=0 ; i<files.length ; i++) {
            searchdirectory(files[i], dirlen, md5);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.print("Enter path of the File/Directory : ");
        Scanner sc = new Scanner(System.in);
        path = sc.next();
        File file = new File(path);
        if (file.exists()) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                if (file.isFile()) {
                    filehash = gethash(md5, file);
                    System.out.println("Duplicate Files : ");
                    String[] findext = file.getName().split("[.]");
                    String extn = "." + findext[findext.length - 1];
                    File[] roots = File.listRoots();
                    for (int i=roots.length-1 ; i>=0 ; i--) {
                        searchfile(roots[i], extn, md5);
                    }
                }
                else {
                    directoryhash(file, md5);
                    System.out.println("Duplicate Directories : ");
                    File[] roots = File.listRoots();
                    for (int i=roots.length-1 ; i>=0 ; i--) {
                        searchdirectory(roots[i], file.list().length, md5);
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Invalid Path/Filename! Try again");
            main(args);
        }
    }
}