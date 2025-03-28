package org.example.data;

public class Anexo {

    public String pdf1;
    public String pdf2;

    public String getPdf2() {
        return pdf2;
    }

    public void setPdf2(String pdf2) {
        this.pdf2 = pdf2;
    }

    public String getPdf1() {
        return pdf1;
    }

    public void setPdf1(String pdf1) {
        this.pdf1 = pdf1;
    }

    @Override
    public String toString(){
        return "anexo1: " + pdf1 +
                "\n anexo2: " + pdf2;
    }


}
