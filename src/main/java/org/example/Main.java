package org.example;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.example.data.Compactador;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
////    ====================================================================================================================
//    Parte 1

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
//    Parte 1
//    ====================================================================================================================


        String inputPDF = "D:\\Dev\\Zipado\\anexo1.pdf";
        String outputCSV = "D:\\Dev\\Zipado\\resultado.csv";

        try (PDDocument document = PDDocument.load(new java.io.File(inputPDF));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputCSV))) {

            // Extrai texto do PDF usando PDFTextStripper
            PDFTextStripper stripper = new PDFTextStripper();

            // Definir intervalo de páginas para extração
            stripper.setStartPage(3);
            stripper.setEndPage(3);
            stripper.setLineSeparator(" "); // Separador de linhas personalizado

            String text = stripper.getText(document);

            text = text.replaceFirst("(Legenda: )", "");

            String regex = "(\\w+):\\s*([^ ]+\\s*([^ ]+))"; // Captura siglas e descrições

            Pattern pattern = Pattern.compile(regex); // Cria o padrão
            Matcher matcher = pattern.matcher(text); // Cria o matcher para o texto

            String descricao1 = "";
            if (matcher.find()) {
                descricao1 = matcher.group(2);
                System.out.println("Primeira: descricao: " + descricao1);
            }
            String descricao2 = "";
            if (matcher.find()) {
                descricao2 = matcher.group(2);
                System.out.println("Segunda: descricao: " + descricao2);
            }

            stripper.setStartPage(3);
            stripper.setEndPage(document.getNumberOfPages());
            stripper.setLineSeparator(" "); // Separador de linhas personalizado

            text = stripper.getText(document);

            if(text.contains("OD")){
            text = text.replace("OD ", descricao1 + " ");
            }

            if(text.contains("AMB")){
            text = text.replace("AMB ", descricao2 + " ");
            }

            text = text.replaceAll("(Rol de Procedimentos e Eventos em Saúde \\(RN 465/2021, vigente a partir de 01/04/2021, e suas alterações\\) )", "");
            text = text.replaceAll("(Legenda).+", "");
            text = text.replaceAll("\\r?\\n", "");
            text = text.replaceAll("  ", " ");
            text = text.replaceAll("(CAPÍTULO )", "$1\n");
            text = text.replaceAll("(PROCEDIMENTOS GERAIS PROCEDIMENTOS GERAIS )", "$1\n");
            text = text.replaceAll("(PROCEDIMENTOS CLÍNICOS AMBULATORIAIS E HOSPITALARES PROCEDIMENTOS CLÍNICOS AMBULATORIAIS E HOSPITALARES )", "$1\n");
            text = text.replaceAll("(PROCEDIMENTOS CIRÚRGICOS E INVASIVOS )", "$1\n");
            text = text.replaceAll("(PROCEDIMENTOS DIAGNÓSTICOS E TERAPÊUTICOS )", "$1\n");

            // Aqui você pode adaptar para organizar o texto em formato CSV
            String[] linhas = text.split("\n"); // Divide o texto em linhas
            for (String linha : linhas) {

                writer.write(linha.replace(" ", ";"));
                writer.newLine();
            }

            zipFileName = "D:\\Dev\\Teste_{rafael_ebata}.zip";

            try (FileOutputStream destino = new FileOutputStream(zipFileName);
                 ZipOutputStream saida = new ZipOutputStream(destino)) {

                FileInputStream arquivoDeEntrada = new FileInputStream ("D:\\Dev\\Zipado\\resultado.csv");
                 Compactador.addFileInZip(saida, arquivoDeEntrada, "resultado.csv");
                 System.out.println("PDF compactado com sucesso no arquivo ZIP: " + zipFileName);


            }catch (Exception e) {
                System.err.println("Erro ao baixar ou compactar o PDF: " + e.getMessage());
            }

            System.out.println("Conversão concluída! Arquivo CSV gerado em: " + outputCSV);
        } catch (IOException e) {
            System.err.println("Erro ao baixar ou compactar o PDF: " + e.getMessage());
        }
    }
}