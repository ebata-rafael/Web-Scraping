package org.example;
import org.example.data.Compactador;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Main {
    public static void main(String[] args) {

        Document doc;

        try {
            doc = Jsoup.connect("https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36").header("Accept-Language", "*").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements orderedList = doc.select("ol:first-of-type li");

        int count = 1;

        String zipFileName = "D:\\Dev\\Zipado.zip";

        try (FileOutputStream destino = new FileOutputStream(zipFileName);
             ZipOutputStream saida = new ZipOutputStream(destino)) {

            for (Element li : orderedList) {
                String arquivoDeEntrada = li.selectFirst("a").attr("href");

                try {
                    Compactador.addFileInZip(saida, arquivoDeEntrada, "anexo"+count+".pdf");
                    System.out.println("PDF compactado com sucesso no arquivo ZIP: " + zipFileName);
                } catch (Exception e) {
                    System.err.println("Erro ao baixar ou compactar o PDF: " + e.getMessage());
                }
                count++;
            }

        }catch (Exception e) {
            System.err.println("Erro ao baixar ou compactar o PDF: " + e.getMessage());
        }


//        Anexo anexos = new Anexo();
//        Field[] fields = anexos.getClass().getDeclaredFields();
//        int count = 0;
//        List<File> files = new ArrayList<>();
//
//        for (Field field : fields) {
//            field.setAccessible(true);
//            try {
//                field.set(anexos, pdfLinks.get(count));
//            } catch (IllegalAccessException e) {}
//
//            files.add(new File(pdfLinks.get(count)));
//
//            count++;
//        }

//        try {
//            Compactador.zipFiles(files, "Zipado.zip");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(anexos);
    }
}