package com.alex.atour.models;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.alex.atour.DTO.Estimation;

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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ExcelModule {

    /*
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Log.e("TAG", "start process");
            String [] fios = {
                    "Винов Александр Сергеевич",
                    "Команда 2",
                    "Команда 3",
                    "Команда 4",
                    "Команда 5",
                    "Команда 6",
                    "Команда 7",
                    "Команда 8",
                    "Команда 9",
            };
            ExcelModule excelModule = new ExcelModule(this);
            excelModule.createReportForReferee("refereeAlex.xlsx", fios);

        }else {
            //запрашиваем разрешение
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
     */

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
    public void createRefereeReport(String refereeProtocolFileName, String refereeInfo, ArrayList<Estimation> estims){

        Log.e("TAG", "createRefereeReport");

        try {
            if(!path.exists()) {
                Log.e("TAG", "folder created");
                // create it, if doesn't exit
                path.mkdirs();
            }

            copyFromAssets(templateRefereeProtocolPath, refereeProtocolFileName);

            FileInputStream inputStream = new FileInputStream(new File(path, refereeProtocolFileName));
            XSSFWorkbook book = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = book.getSheetAt(0);

            //refereeInfo
            Row row = sheet.getRow(1);
            fillCell(row, 0, refereeInfo);

            for (int i = 0; i < estims.size() ; i++){

                Log.e("TAG", i+": "+estims.get(i).getMemberFIO());

                row = sheet.getRow(i+6);

                // member FIO
                fillCell(row, 1, estims.get(i).getMemberFIO());

                //estimations
                fillCell(row, 5, estims.get(i).getComplexity());
                fillCell(row, 6, estims.get(i).getNovelty());
                fillCell(row, 7, estims.get(i).getStrategy());
                fillCell(row, 8, estims.get(i).getTactics());
                fillCell(row, 9, estims.get(i).getTechnique());
                fillCell(row, 10, estims.get(i).getTension());
                fillCell(row, 11, estims.get(i).getInformativeness());

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

    private void fillCell(Row row, int ind, String data){
        Cell cell = row.getCell(ind);
        if (cell == null){
            cell = row.createCell(ind);
        }
        cell.setCellValue(data);
    }
    private void fillCell(Row row, int ind, double data){
        Cell cell = row.getCell(ind);
        if (cell == null){
            cell = row.createCell(ind);
        }
        BigDecimal bd = new BigDecimal(Double.toString(data)).setScale(2, RoundingMode.HALF_DOWN);
        cell.setCellValue(bd.doubleValue());
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
