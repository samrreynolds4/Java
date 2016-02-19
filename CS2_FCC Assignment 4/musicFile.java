import java.io.File;


// Class was created just to add to the binary Search Tree
// I wanted to implement my own way of comparing the names of the songs
// But I ran out of time
public class musicFile implements Comparable<musicFile>
{
   private String name;
   private File file;
   
   public musicFile(String name, File file)
   {
      this.name = name.toUpperCase();
      this.file = file;
   }
   
   public musicFile(String name)
   {
      this.name = name.toUpperCase();
   }
   
   public String getName()
   {
      return name;
   }
   
   public File getFile()
   {
      return file;
   }
   
   public int compareTo(musicFile n)
   {
      return name.compareTo(n.getName());
   }
   
   public String toString()
   {
      return "Name: " + name + "\n" + "Path: " + file.getPath();
   }
}