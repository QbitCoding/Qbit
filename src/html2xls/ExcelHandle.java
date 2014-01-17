package html2xls;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
 
import javax.print.attribute.standard.SheetCollate;
import javax.swing.JProgressBar;
 
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.biff.WritableRecordData;
import jxl.biff.drawing.SheetDrawingWriter;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.read.biff.SheetImpl;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
 
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.visitors.ObjectFindingVisitor;
 
public class ExcelHandle {
 private JProgressBar bar;
 
 public ExcelHandle(JProgressBar bar) {
  this.bar = bar;
 }
 
 public void castAll(String html_path, String excel_folder, String headname,
   String excel_name) throws HTMLException1, HTMLException2 {
  File bigfile = new File(html_path);
  FilenameFilter fileter = new FilenameFilter() {
   public boolean accept(File dir, String name) {
    boolean ff = name.split("_").length == 3;
    boolean flag = name.endsWith("html") && name.startsWith("body")
      && ff;
    return flag;
   }
  };
  File[] bfiles = bigfile.listFiles(fileter);
  List<String> xls_names = new ArrayList<String>();
  for (int i = 0; i < bfiles.length; i++) {
   String xls_name = bfiles[i].getName().split("_")[1];
   if (!xls_names.contains(xls_name)) {
    xls_names.add(xls_name);
   }
  }
  int now = 0;
  int xlsSize = xls_names.size();
  for (int s = 0; s < xlsSize; s++) {
   final String xls = xls_names.get(s);
   FilenameFilter filter2 = new FilenameFilter() {
    public boolean accept(File dir, String name) {
     boolean flag = name.endsWith("html")
       && name.startsWith("body")
       && (name.indexOf(xls) != -1);
     return flag;
    }
   };
 
   File[] files = bigfile.listFiles(filter2);
   File target = null;
   String city = "";
   WritableWorkbook wwk = null;
   WritableSheet ws1 = null;
   WritableCellFormat cellFormat2 = null;
   WritableCellFormat cellFormat1 = null;
   WritableCellFormat cellFormatt = null;
   try {
    Parser parser2 = Parser.createParser(getHtml(files[0]), "gbk");
    ObjectFindingVisitor visitor2 = new ObjectFindingVisitor(
      TableTag.class);
    parser2.visitAllNodesWith(visitor2);
    Node[] nodes2 = visitor2.getTags();
    TableTag tg2 = (TableTag) nodes2[0];
    TableRow[] tr2 = tg2.getRows();
    String citytag = tr2[0].getColumns()[0].getStringText().trim();
    String excel_path = "";
    if (!citytag.startsWith("-") && !citytag.startsWith(".")) {
     int i = citytag.indexOf("-");
     city = citytag.substring(0, i);
    }
    if (excel_name == null || excel_name.equals("")) {
     if (citytag.startsWith(".") || citytag.startsWith("-")) {
      excel_path = excel_folder + "/" + xls + ".xls";
     } else {
      int j = xls.indexOf("1");
      String date="";
      if(j!=-1){
       date = xls.substring(j, j + 4);
      }  
      excel_path = excel_folder + "/" + date + city + "内部版"
        + ".xls";
     }
    } else {
     excel_path = excel_folder + "/" + excel_name + ".xls";
    }
    target = new File(excel_path);
    wwk = Workbook.createWorkbook(target);
 
    ws1 = wwk.createSheet("所有目录", 0);
    ws1.setColumnView(0, 25);
    ws1.setColumnView(1, 25);
    ws1.setColumnView(2, 25);
    WritableFont wf = new WritableFont(WritableFont.ARIAL, 16,
      WritableFont.BOLD, false);
    cellFormat1 = new WritableCellFormat(wf);
    cellFormat1.setAlignment(Alignment.CENTRE);
    cellFormat1.setBorder(Border.ALL, BorderLineStyle.THIN);
 
    if (headname != null && !headname.equals("")) {
     Label lab = new Label(0, 0, headname);
     lab.setCellFormat(cellFormat1);
     ws1.addCell(lab);
    }
    WritableFont tf = new WritableFont(WritableFont.ARIAL, 10,
      WritableFont.BOLD, false);
    cellFormatt = new WritableCellFormat(tf);
    cellFormatt.setAlignment(Alignment.CENTRE);
    cellFormatt.setBackground(Colour.GRAY_25);
    cellFormatt.setWrap(true);
    cellFormatt.setBorder(Border.TOP, BorderLineStyle.MEDIUM);
    cellFormatt.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
    cellFormatt.setBorder(Border.LEFT, BorderLineStyle.DASHED);
    cellFormatt.setBorder(Border.RIGHT, BorderLineStyle.DASHED);
 
    WritableFont wfont = new WritableFont(WritableFont.ARIAL, 12,
      WritableFont.NO_BOLD, false);
    WritableCellFormat cellFormat = new WritableCellFormat(wfont);
    cellFormat.setAlignment(Alignment.CENTRE);
    cellFormat.setBackground(Colour.GRAY_25);
    cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
 
    WritableFont wfont2 = new WritableFont(WritableFont.ARIAL, 10,
      WritableFont.NO_BOLD, false);
    cellFormat2 = new WritableCellFormat(wfont2);
    cellFormat2.setAlignment(Alignment.CENTRE);
    cellFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);
    cellFormat2.setBorder(Border.ALL, BorderLineStyle.THIN);
    ws1.addCell(new Label(0, 1, "报告编号及内容", cellFormat));
    ws1.addCell(new Label(2, 1, "区域", cellFormat));
    ws1.addCell(new Label(0, 2, "1、铺货率", cellFormat2));
    ws1.addCell(new Label(0, 5, "2、销量", cellFormat2));
    ws1.addCell(new Label(0, 8, "3、销售额", cellFormat2));
    ws1.addCell(new Label(0, 10, "4、点上份额", cellFormat2));
    ws1.addCell(new Label(0, 11, "平均单店销量", cellFormat2));
    ws1.addCell(new Label(0, 12, "5、平均价格", cellFormat2));
    ws1.addCell(new Label(0, 15, "6、冰柜列阵", cellFormat2));
    ws1.addCell(new Label(0, 16, "14、销量外包装", cellFormat2));
    ws1.addCell(new Label(0, 19, "15、销售额外包装", cellFormat2));
    ws1.mergeCells(0, 0, 2, 0);
    ws1.mergeCells(0, 1, 1, 1);
    ws1.mergeCells(0, 2, 0, 4);
    ws1.mergeCells(0, 5, 0, 7);
    ws1.mergeCells(0, 8, 0, 9);
    ws1.mergeCells(0, 12, 0, 14);
    ws1.mergeCells(0, 16, 0, 18);
    ws1.mergeCells(0, 19, 0, 21);
    for (int i = 2; i <= 21; i++) {
     ws1.addCell(new Label(2, i, null, cellFormat2));
    }
 
   } catch (Exception e) {
    e.printStackTrace();
    throw new HTMLException1(files[0].getName());
   }
   int od = 1;
   int filesLeng = files.length;
   int max = (xlsSize) * (filesLeng);
   for (int i = 0; i < filesLeng; i++) {
    now++;
    bar.setString("已转换" + (now * 100) / max + "%");
    bar.setValue((now * 100) / max);
    for (int t = 0; t < filesLeng; t++) {
     try {
      String str = files[t].getName().split("_")[2].replace(
        ".html", "");
      int sheetOrder = Integer.parseInt(str);
      if (sheetOrder == od) {
       String html = getHtml(files[t]);
       Parser parser = Parser.createParser(html, "gbk");
       ObjectFindingVisitor visitor = new ObjectFindingVisitor(
         TableTag.class);
       parser.visitAllNodesWith(visitor);
       Node[] nodes = visitor.getTags();
       TableTag tg = (TableTag) nodes[1];
       TableRow[] tr = tg.getRows();
       TableHeader[] th = tr[0].getHeaders();
 
       String name = th[0].getStringText().trim().replace(
         "(%)", "");
       String prefix = getPrefix(sheetOrder);
       String ws_name = prefix + name;
       WritableSheet ws = wwk.createSheet(ws_name,
         sheetOrder);
       SheetSettings sset = ws.getSettings();
       sset.setHorizontalFreeze(2);
       sset.setVerticalFreeze(4);
//       ws.setColumnGroup(2, 4, true);
      
       boolean f = th[0].getStringText().endsWith("(%)");
       ws.setColumnView(0, 30);
       ws.setRowView(1, 500);
       ws.setRowView(2, 650);
       ws.setRowView(3, 550);
       for (int j = 1; j < th.length; j++) {
        ws.setColumnView(j, 10);
       }
       ws1.addHyperlink(new WritableHyperlink(1,
         sheetOrder + 1, ws_name, ws, 0, 0));
       ws.addHyperlink(new WritableHyperlink(1, 1, "返回目录",
         ws1, 0, 0));
       String head_name = th[0].getStringText().replace(
         "(%)", "");
       ws.addCell(new Label(0, 0, headname + "-"
         + head_name, cellFormat1));
       ws.addCell(new Label(0, 1, "城市:" + city));
       ws.mergeCells(0, 0, 1, 0);
       Label lable = null;
       DisplayFormat format = new NumberFormat("0.0%");
       WritableCellFormat cellF = new WritableCellFormat(
         format);
       DisplayFormat format2 = new NumberFormat("0.0");
       WritableCellFormat cellF2 = new WritableCellFormat(
         format2);
       WritableCellFormat cf = new WritableCellFormat();
       cellF.setBorder(Border.ALL, BorderLineStyle.DASHED);
       cellF2
         .setBorder(Border.ALL,
           BorderLineStyle.DASHED);
       cf.setBorder(Border.ALL, BorderLineStyle.DASHED);
 
       DisplayFormat format3 = new NumberFormat("0.0");
       WritableCellFormat cf1 = new WritableCellFormat(
         format3);
       cf1.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
       cf1.setBorder(Border.RIGHT, BorderLineStyle.DASHED);
       
       WritableCellFormat cf2=new WritableCellFormat();
       cf2.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
       cf2.setBorder(Border.BOTTOM, BorderLineStyle.DASHED);
       
       WritableCellFormat cf3=new WritableCellFormat();
       cf3.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
       cf3.setBorder(Border.BOTTOM, BorderLineStyle.DASHED);
       cf3.setBackground(Colour.SKY_BLUE);
       
       
       WritableCellFormat cf4=new WritableCellFormat();
       cf4.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
       cf4.setBorder(Border.BOTTOM, BorderLineStyle.DASHED);
       cf4.setBackground(Colour.ICE_BLUE);
              
       for (int h = 0; h < th.length; h++) {
        
        lable = new Label(h, 2, th[h].getStringText()
          .trim(), cellFormatt);
        ws.addCell(lable);
      
       }
 
       int trLength = tr.length;
 
       TableColumn[] td1 = tr[1].getColumns();
       for (int w = 0; w < td1.length; w++) {
        String text1 = td1[w].getStringText()
          .replaceAll("&nbsp;", "").trim();
        if (w == 0
          || th[w].getStringText().replaceAll(
            "&nbsp;", "").trim().equals(
            "档次")) {
         lable = new Label(w, 3, text1, cf1);
         ws.addCell(lable);
        }else if(text1== null || text1.equals(".")){
         Label num = new Label(w, 3, null, cf1);
         ws.addCell(num);
        } else {
         BigDecimal dec = new BigDecimal(text1);
         double d2 = dec.doubleValue();
         Number num = new Number(w, 3, d2, cf1);
         ws.addCell(num);
        }
       }
       for (int j = 2; j < trLength; j++) {
        TableColumn[] td = tr[j].getColumns();
        for (int k = 0; k < td.length; k++) {
         String text = td[k].getStringText()
           .replaceAll("&nbsp;", "").trim();
         double d = 0;
         if (k != 0
           && !th[k].getStringText()
             .replaceAll("&nbsp;", "")
             .trim().equals("档次")) {
          if (text != null && !text.equals(".")) {
           BigDecimal dec = new BigDecimal(
             text);
           d = dec.doubleValue();
           if (f && j > 1) {
 
            Number num = new Number(k,
              j + 2, d / 100, cellF);
            ws.addCell(num);
           } else {
            Number num = new Number(k,
              j + 2, d, cellF2);
            ws.addCell(num);
           }
 
          } else {
           lable = new Label(k, j + 2, null,
             cf);
           ws.addCell(lable);
          }
         } else {
          if(text.indexOf("公司")!=-1||text.indexOf("集团")!=-1||text.indexOf("Bev")!=-1||text.equals("AB")||text.equals("亚太啤酒")){
           lable = new Label(k, j + 2, text,
             cf3);
           ws.addCell(lable);
           
          }else if(text.indexOf("P")==-1&&k==0){
           lable = new Label(k, j + 2, text,
             cf4);
           ws.addCell(lable);
          }else{
           lable = new Label(k, j + 2, text,
             cf2);
           ws.addCell(lable);
          }
         }
 
        }
 
       }
    
       ws.setColumnGroup(4,8, true);
       ws.setColumnGroup(10,16, true);
       od++;
       break;
      }
     } catch (Exception e) {
      e.printStackTrace();
      throw new HTMLException2(files[t].getName());
     }
    }
   }
 
   try {
    wwk.write();
    wwk.close();
   } catch (Exception e) {
    e.printStackTrace();
    throw new HTMLException2(files[0].getName());
   }
  }
 
 }
 
 public String getHtml(File file) {
  StringBuffer sb = null;
  try {
   FileReader fr = new FileReader(file);
   BufferedReader br = new BufferedReader(fr);
   sb = new StringBuffer();
   String str = null;
   while ((str = br.readLine()) != null) {
    sb.append(str);
   }
   fr.close();
   br.close();
  } catch (Exception e) {
   e.printStackTrace();
  }
  return sb.toString();
 }
 
 public String getPrefix(int sheetOrder) {
  if (sheetOrder == 1) {
   return "1.1-";
  } else if (sheetOrder == 2) {
   return "1.2-";
  } else if (sheetOrder == 3) {
   return "1.3-";
  } else if (sheetOrder == 4) {
   return "2.1-";
  } else if (sheetOrder == 5) {
   return "2.2-";
  } else if (sheetOrder == 6) {
   return "2.3-";
  } else if (sheetOrder == 7) {
   return "3.1-";
  } else if (sheetOrder == 8) {
   return "3.2-";
  } else if (sheetOrder == 9) {
   return "4.1-";
  } else if (sheetOrder == 10) {
   return "4.2-";
  } else if (sheetOrder == 11) {
   return "5.1-";
  } else if (sheetOrder == 12) {
   return "5.2-";
  } else if (sheetOrder == 13) {
   return "5.3-";
  } else if (sheetOrder == 14) {
   return "6.1-";
  } else if (sheetOrder == 15) {
   return "14.1-";
  } else if (sheetOrder == 16) {
   return "14.2-";
  } else if (sheetOrder == 17) {
   return "14.3-";
  } else if (sheetOrder == 18) {
   return "15.1-";
  } else if (sheetOrder == 19) {
   return "15.2-";
  } else if (sheetOrder == 20) {
   return "15.3-";
  } else {
   return "";
  }
 }
 
}
