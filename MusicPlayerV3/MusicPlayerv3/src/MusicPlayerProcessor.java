
import java.util.*;
import java.io.*;
   
public class MusicPlayerProcessor
{
    private BST<File>              searchTree       = new BST<>(); // search tree for searching for files
    private TwoWayLinkedList<File> songHistory      = new TwoWayLinkedList<>(); // every song played will be added to this list
    private Queue<File>            songQueue        = new LinkedList<>(); // Queue for queueing playlists
    private Queue<File>            playQueue        = new LinkedList<>(); // Queue for queueing specific songs
    private File                   musicFile;
    private FileFilter             musicFilter      = new MusicFileFilter();
    
    public MusicPlayerProcessor(File file)
    {
        musicFile = file;
        getMusic(file);
    }
    
    public File[] getMusic(File musicFile)
    {
        File[] musicFiles = null;
        
        if(musicFile.isDirectory())
        {
            musicFiles = musicFile.listFiles(musicFilter);
        }
        
        return musicFiles;
    }
    
    public File[] getFiles(File musicFile)
    {
        return musicFile.listFiles();
    }
}

class MusicFileFilter implements FileFilter
{
    public MusicFileFilter()
    {
        
    }
    
    @Override
    public boolean accept(File file)
    {
        String fileExtension;
        String filePath = file.getPath();
        fileExtension = filePath.substring(filePath.length() - 4, filePath.length());
        System.out.println(fileExtension);
        // Test if the extension is a playable audio file
        if(fileExtension.equals(".mp3") || fileExtension.equals(".wav") || fileExtension.equals(".mp4") || fileExtension.equals(".mp4a"))
        {
            return true;
        }
        
        return false;
    }
}
