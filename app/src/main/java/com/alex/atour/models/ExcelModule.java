package com.alex.atour.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;

import com.alex.atour.DTO.Estimation;
import com.alex.atour.DTO.TSMReport;
import com.alex.atour.DTO._Estimation;
import com.alex.atour.db.DBManager;
import com.alex.atour.db.RealmDB;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Set;

public class ExcelModule {

    private final String templateRefereeProtocolPath = "protocol_referee.xlsx";
    private final String templateTotalProtocolPath = "protocol_total.xlsx";
    private final int TSM_ROWS = 38;

    private final double removedVal = 999;
    private final Context ctx;
    public ExcelModule(Context ctx){
        this.ctx = ctx;
    }

    final File path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/ATour/");

    //заполняет в протоколе ФИО руководителей и место проведения, (ФИО, город, разряд) судьи
    public void createRefereeReport(String refereeProtocolFileName, String refereeInfo, ArrayList<Estimation> estims, ArrayList<TSMReport> tsmReports){

        Log.e("TAG", "creatingRefereeReport");

        FileInputStream inputStream = null;
        XSSFWorkbook book = null;
        try {
            if(!path.exists()) {
                Log.e("TAG", "folder created");
                // create it, if doesn't exit
                path.mkdirs();
            }

            copyFromAssets(templateRefereeProtocolPath, refereeProtocolFileName);

            inputStream = new FileInputStream(new File(path, refereeProtocolFileName));
            book = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = book.getSheetAt(0);

            //refereeInfo
            Row row = sheet.getRow(1);
            fillCell(row, 0, refereeInfo);

            for (int i = 0; i < estims.size(); i++) {
                row = sheet.getRow(i + 6);

                // member FIO
                fillCell(row, 1, estims.get(i).getMemberFIO());

                TSMReport tsm = getTsmById(estims.get(i).getMemberID(), tsmReports);
                //category
                fillCell(row, 2, tsm.getCategory());

                //path
                fillCell(row, 4, tsm.getShortPath());

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
            book.write(os); os.close();

            Log.e("TAG", "success");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if (inputStream != null) inputStream.close();
                if (book != null) book.close();
            }catch(Exception e){ e.printStackTrace(); }
        }
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
                Log.e("TAG", "copied file from assets");
            } catch(Exception e){
                e.printStackTrace();
            }finally {
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
        }catch (Exception e){ e.printStackTrace(); }
    }


    //todo: протестить с 5ю судьями
    public void createTotalProtocol(String champID,  Set<String> membersIDs, String[] refereesRanks, ArrayList<TSMReport> tsmReports, DBManager.IRequestListener listener){
        if(!path.exists()) {
            Log.e("TAG", "folder created");
            // create it, if doesn't exit
            path.mkdirs();
        }
        FileInputStream inputStream = null;
        XSSFWorkbook book = null;
        try{
            copyFromAssets(templateTotalProtocolPath, templateTotalProtocolPath);

            inputStream = new FileInputStream(new File(path, templateTotalProtocolPath));
            book = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = book.getSheetAt(0);

            DBManager db = DBManager.getInstance();
            RealmDB realmDB = db.getRealmDB();
            Row row;
            //main logic
            //iterate persons ids
            int index = 0;
            for (String pID: membersIDs){
                //get all marks for person
                ArrayList<_Estimation> marks = realmDB.getEstimationsOfUser(champID, pID);
                double compl=0d, nov=0d, st=0d, tac=0d, tec=0d, ten=0d, inf=0d;

                int refereesSize = marks.size();
                Log.e("TAG", "refereesSize "+refereesSize+" for "+pID);
                //если судей от 5 и больше
                //
                if (refereesSize > 4) {
                    double min = marks.get(0).getComplexity(),
                            max = min;
                    double minTac= Double.MAX_VALUE, maxTac= Double.MAX_VALUE,
                            minSt= Double.MAX_VALUE, maxSt= Double.MAX_VALUE,
                            minTec= Double.MAX_VALUE, maxTec = Double.MAX_VALUE;


                    int x1 = 0, y1 = 0, x2=0, y2 = 0;//indexes of min and max value
                    //searching max and min values, indexes
                    for (int i = 0; i<refereesSize; i++){
                        _Estimation e = marks.get(i);
                        compl=e.getComplexity(); nov=e.getNovelty();
                        st=e.getStrategy()     ; tac=e.getTactics();
                        tec=e.getTechnique()   ; ten=e.getTension();
                        inf=e.getInformativeness();

                        //find min and max vals
                        min = Minimum(min, compl, nov, st, tac, tec, ten, inf);
                        max = Maximum(max, compl, nov, st, tac, tec, ten, inf);

                        //get index of min val
                        if (min == compl){x1 = i; y1 = 0;}  if (max == compl){x2 = i; y2 = 0;}
                        if (min == nov){x1 = i; y1 = 1;}    if (max == nov){x2 = i; y2 = 1;}
                        if (min == st){x1 = i; y1 = 2;}     if (max == st){x2 = i; y2 = 2;}
                        if (min == tac){x1 = i; y1 = 3;}    if (max == tac){x2 = i; y2 = 3;}
                        if (min == tec){x1 = i; y1 = 4;}    if (max == tec){x2 = i; y2 = 4;}
                        if (min == ten){x1 = i; y1 = 5;}    if (max == ten){x2 = i; y2 = 5;}
                        if (min == inf){x1 = i; y1 = 6;}    if (max == inf){x2 = i; y2 = 6;}
                    }

                    //remove min and max vals
                    removeValues(y1, x1, marks);
                    removeValues(y2, x2, marks);


                    //remove min and max val in Стратегии, Тактике, Технике
                    int iSt=0, iTa=0, iTe=0, aSt=0, aTa=0, aTe=0;
                    for (int i = 0; i < refereesSize; i++){
                        _Estimation e = marks.get(i);
                        //min
                        if (e.getStrategy()!=removedVal && minSt>e.getStrategy()) {minSt = e.getStrategy(); iSt = i;}
                        if (e.getTactics()!=removedVal && minTac>e.getTactics()) {minTac = e.getTactics(); iTa = i;}
                        if (e.getTechnique()!=removedVal && minTec>e.getTechnique()) {minTec = e.getTechnique(); iTe = i;}

                        //max
                        if (e.getStrategy()!=removedVal && maxSt<e.getStrategy()) {maxSt = e.getStrategy(); aSt = i;}
                        if (e.getTactics()!=removedVal && maxTac<e.getTactics()) {maxTac = e.getTactics(); aTa = i;}
                        if (e.getTechnique()!=removedVal && maxTec<e.getTechnique()) {maxTec = e.getTechnique(); aTe = i;}
                    }
                    marks.get(iSt).setStrategy(removedVal);
                    marks.get(iTa).setTactics(removedVal);
                    marks.get(iTe).setTechnique(removedVal);
                    marks.get(aSt).setStrategy(removedVal);
                    marks.get(aTa).setTactics(removedVal);
                    marks.get(aTe).setTechnique(removedVal);

                    //count every value
                    for (_Estimation e: marks) {
                        //dont count if it was deleted as min or max val
                        if (e.getComplexity() != removedVal) compl+=e.getComplexity();
                        if (e.getNovelty() != removedVal) nov+=e.getNovelty();
                        if (e.getStrategy() != removedVal) st+=e.getStrategy();
                        if (e.getTactics() != removedVal) tac+=e.getTactics();
                        if (e.getTechnique() != removedVal) tec+=e.getTechnique();
                        if (e.getTension() != removedVal) ten+=e.getTension();
                        if (e.getInformativeness() != removedVal) inf+=e.getInformativeness();
                    }
                    compl /= (refereesSize-2);  nov /= (refereesSize-2);
                    st /= (refereesSize-2);     tac /= (refereesSize-2);
                    tec /= (refereesSize-2);    ten /= (refereesSize-2);
                    inf /= (refereesSize-2);

                    Log.e("TAG", "gather 4: "+compl +" "+nov+" "+st+" "+tac+" "+tec+" "+ten+" "+inf);
                }else{
                    //if less than 5 referees

                    //count values
                    for (_Estimation e: marks) {
                        compl+=e.getComplexity(); nov+=e.getNovelty();
                        st+=e.getStrategy()     ; tac+=e.getTactics();
                        tec+=e.getTechnique()   ; ten+=e.getTension();
                        inf+=e.getInformativeness();
                    }
                    compl /= refereesSize;  nov /= refereesSize;
                    st /= refereesSize;     tac /= refereesSize;
                    tec /= refereesSize;    ten /= refereesSize;
                    inf /= refereesSize;

                    Log.e("TAG", "less 5: "+compl +" "+nov+" "+st+" "+tac+" "+tec+" "+ten+" "+inf);
                }

                TSMReport tsm = getTsmById(pID, tsmReports);

                //memberFIO
                row = sheet.getRow(11+index);
                fillCell(row, 1, tsm.getManagerFIO() + ", "+ tsm.getCompany());

                //region
                fillCell(row, 2, tsm.getShortPath());

                //members
                fillCell(row, 4, tsm.getMembers());

                //date
                fillCell(row, 5, tsm.getDate());

                //estimations
                fillCell(row, 6, compl);
                fillCell(row, 7, nov);
                fillCell(row, 8, st);
                fillCell(row, 9, tac);
                fillCell(row, 10, tec);
                fillCell(row, 11, ten);
                fillCell(row, 12, inf);

                //result
                double res = compl+st+tec+inf+nov+tac+ten;
                fillCell(row, 13, res);

                //place

                index++;
            }

            index = 0;
            for (String refInfo: refereesRanks){
                row = sheet.getRow(38+index);
                fillCell(row, 2, refInfo);
                index++;
            }


            //save and close streams
            File newFile = new File(path, templateTotalProtocolPath);
            if (!newFile.exists()) newFile.createNewFile();
            FileOutputStream os = new FileOutputStream(newFile);
            book.write(os); os.close();

            db.uploadTotalProtocolFile(champID, Uri.fromFile(newFile));
            listener.onSuccess();
        }catch (Exception e){
            e.printStackTrace();
            listener.onFailed("Ошибка создания файла "+e.getMessage());
        }finally{
            try {
                if (inputStream != null) inputStream.close();
                if (book != null) book.close();
            }catch(Exception e){ e.printStackTrace(); }
        }
    }

    private double Minimum(double... args){
        double m = args[0];
        for(double d: args) m = Math.min(m, d);
        return m;
    }
    private double Maximum(double... args){
        double m = args[0];
        for(double d: args) m = Math.max(m, d);
        return m;
    }
    private void removeValues(int x, int y, ArrayList<_Estimation> marks){
        switch (y){
            case 0: marks.get(x).setComplexity(removedVal);break;
            case 1: marks.get(x).setNovelty(removedVal);break;
            case 2: marks.get(x).setStrategy(removedVal);break;
            case 3: marks.get(x).setTactics(removedVal);break;
            case 4: marks.get(x).setTechnique(removedVal);break;
            case 5: marks.get(x).setTension(removedVal);break;
            case 6: marks.get(x).setInformativeness(removedVal);break;
        }
    }

    public TSMReport parseTSM(Uri tsmPath){
        Log.e("TAG", "START PARSING TSM");

        InputStream inputStream = null;
        XSSFWorkbook book = null;
        TSMReport tsm = null;

        try {

            inputStream = ctx.getContentResolver().openInputStream(tsmPath);
            book = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = book.getSheetAt(0);

            Row row;
            Cell cell;
            tsm = new TSMReport();

            //category
            row = sheet.getRow(3);
            cell = row.getCell(1);
            if (cell.getCellType() == 0){ tsm.setCategory(cell.getNumericCellValue()+""); }
            if (cell.getCellType() == 1){ tsm.setCategory(cell.getStringCellValue()); }

            //company
            row = sheet.getRow(5);
            cell = row.getCell(1);
            if (cell != null){ tsm.setCompany(cell.getStringCellValue()); }

            //managerFIO
            row = sheet.getRow(6);
            cell = row.getCell(1);
            if (cell != null){ tsm.setManagerFIO(cell.getStringCellValue()); }

            //shortPath
            row = sheet.getRow(26);
            cell = row.getCell(1);
            if (cell != null){ tsm.setShortPath(cell.getStringCellValue()); }

            //members
            StringBuilder members = new StringBuilder();
            for (int i = 10; i < 25; i++){
                row = sheet.getRow(i);
                cell = row.getCell(1);
                if (cell == null) continue;
                members.append(cell.getStringCellValue()).append(", ");
            }
            tsm.setMembers(members.toString());

            //date
            row = sheet.getRow(28);
            cell = row.getCell(1);
            if (cell.getCellType() == 0){ tsm.setDate(cell.getNumericCellValue()+""); }
            if (cell.getCellType() == 1){ tsm.setDate(cell.getStringCellValue()); }

            Log.e("TAG", tsm.toString());

/*
            Log.e("TAG", "ITERATING TSM");
            for (int i = 0; i < TSM_ROWS; i++) {
                row = sheet.getRow(i);
                cell = row.getCell(1);
                if (cell== null) continue;
                switch (cell.getCellType()){
                    case Cell.CELL_TYPE_NUMERIC://NUMERIC
                        Log.e("TAG", cell.getNumericCellValue()+"");
                        break;
                    case Cell.CELL_TYPE_STRING://STRING
                        Log.e("TAG", cell.getStringCellValue());
                        break;
                }
            }
*/
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream!=null) inputStream.close();
                if (book!=null) book.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tsm;
    }
    private TSMReport getTsmById(String id, ArrayList<TSMReport> tsmReports){
        for (TSMReport tsm: tsmReports){
            if (tsm.getId().equals(id)) return tsm;
        }
        return null;
    }
}
