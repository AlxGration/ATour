package com.alex.atour.DTO;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExcelModule {


    private final String templateRefereeProtocolPath = "protocol_referee.xlsx";

    private final Context ctx;
    public ExcelModule(Context ctx){
        this.ctx = ctx;
    }

    final File path =
            Environment.getExternalStoragePublicDirectory
                    (
                            Environment.DIRECTORY_DOCUMENTS + "/ATour/"
                    );

    //заполняет в протоколе ФИО руководителей
    //todo:: и место проведения
    public void createReportForReferee(String refereeProtocolFileName, String[] fios){

        Log.e("TAG", "createReportForReferee");

        //файл общий
        try {

            if(!path.exists())
            {
                Log.e("TAG", "folder created");
                // Make it, if it doesn't exit
                path.mkdirs();
            }

            copyFromAssets(templateRefereeProtocolPath, refereeProtocolFileName);

            FileInputStream inputStream = new FileInputStream(new File(path, refereeProtocolFileName));
            XSSFWorkbook book = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = book.getSheetAt(0);

            for (int i = 0; i < fios.length; i++){

                Log.e("TAG", i+": "+fios[i]);

                Row row = sheet.getRow(i+6);
                Cell fioCell = row.getCell(1);
                if (fioCell == null){
                    fioCell = row.createCell(1);
                }
                fioCell.setCellValue(fios[i]);
            }

            //save and close streams
            File newFile = new File(path, refereeProtocolFileName);
            if (!newFile.exists()) newFile.createNewFile();
            FileOutputStream os = new FileOutputStream(newFile);
            book.write(os);
            os.close();
            inputStream.close();
            book.close();
            Log.e("TAG", "success");
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.e("TAG", "end");
    }

    private void copyFromAssets(String originalName, String fileName) {
        AssetManager assetManager = ctx.getAssets();
        try {
            InputStream inputStream = assetManager.open(originalName);
            File f = new File(path, fileName);
            FileOutputStream outputStream = new FileOutputStream(f);
            try {
                byte[] buffer = new byte[1024];
                int read;
                while((read = inputStream.read(buffer)) != -1){
                    outputStream.write(buffer, 0, read);
                }
            } catch(Exception e){
                e.printStackTrace();
            }finally {
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
