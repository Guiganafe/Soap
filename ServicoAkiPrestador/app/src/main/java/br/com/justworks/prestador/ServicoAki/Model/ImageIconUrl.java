package br.com.justworks.prestador.ServicoAki.Model;

public class ImageIconUrl {
    private String pdf;
    private String svg;

    public ImageIconUrl() {
    }

    public ImageIconUrl(String pdf, String svg) {
        this.pdf = pdf;
        this.svg = svg;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }
}
