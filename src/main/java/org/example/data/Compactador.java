package org.example.data;

import java.io.*;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compactador {

    public static void addFileInZip(ZipOutputStream saida,String arquivoDeEntrada, String nomeArquivo)throws IOException{
        InputStream file = new URL(arquivoDeEntrada).openStream();

        // Cria uma entrada no arquivo ZIP para o PDF
        ZipEntry zipEntry = new ZipEntry(nomeArquivo);
        saida.putNextEntry(zipEntry);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = file.read(buffer)) != -1) {
            saida.write(buffer, 0, bytesRead); // Grava os dados diretamente no ZIP
        }

        saida.closeEntry();
    }

    public static void addFileInZip(ZipOutputStream saida,FileInputStream arquivoDeEntrada, String nomeArquivo)throws IOException{
        InputStream file = arquivoDeEntrada;

        // Cria uma entrada no arquivo ZIP para o PDF
        ZipEntry zipEntry = new ZipEntry(nomeArquivo);
        saida.putNextEntry(zipEntry);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = file.read(buffer)) != -1) {
            saida.write(buffer, 0, bytesRead); // Grava os dados diretamente no ZIP
        }

        saida.closeEntry();
    }
}
