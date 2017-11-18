package bobo.erp.service.teach;

import bobo.erp.entity.teach.FileInfo;
import bobo.erp.repository.teach.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * Created by bobo on 2017/11/18.
 */
@Service
public class FileDownloadService {
    @Autowired
    private FileInfoRepository fileInfoRepository;

    public void reportDownload(HttpServletResponse res, String type, Integer id){
        //下载文件会报错，但文件修复后可正常使用
        FileInfo fileInfo = fileInfoRepository.findOne(id);
        if (fileInfo.getState() == 1){
            String fileName = fileInfo.getName();
            res.setHeader("content-type", "application/octet-stream");
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            byte[] buff = new byte[4096];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = res.getOutputStream();
                File file = new File("report/" + type + "/" + fileName);
                FileInputStream fi = new FileInputStream(file);

                bis = new BufferedInputStream(fi);
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
                os.close();
                bis.close();
                fi.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
