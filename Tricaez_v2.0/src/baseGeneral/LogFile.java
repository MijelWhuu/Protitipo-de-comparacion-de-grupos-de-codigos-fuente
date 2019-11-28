package baseGeneral;

import java.io.IOException;
import java.util.logging.*;

/**
 *
 * @author GLP
 */

public class LogFile {
    
    public static Logger log;
    
    static{
        try{
            boolean append = true;
            int limit = 1000000;
            int numLogFiles = 2;
            FileHandler fh = new FileHandler("src/files/log.txt",append);
            fh.setFormatter(new Formatter(){
                public String format(LogRecord rec){
                    StringBuffer buf = new StringBuffer(1000);
                    buf.append(new java.util.Date());
                    buf.append(' ');
                    buf.append(rec.getLevel());
                    buf.append(' ');
                    buf.append(formatMessage(rec));
                    buf.append("\r\n");
                    return buf.toString();
                }
            });
            log = Logger.getLogger("LogFile");
            log.addHandler(fh);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
