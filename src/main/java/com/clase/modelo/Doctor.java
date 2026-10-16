package com.clase.modelo;

public class Doctor {
    private String iddoc;
    private String apeldoc;
    private String nomdoc;
    private String movildoc;
    private String maildoc;
    private String espedoc;
    private boolean coledoc;


    //modelo bbdd
    public Doctor(String iddoc, String apeldoc, String nomdoc,
            String movildoc, String maildoc, String espedoc, boolean coledoc) {
            
        this.iddoc = iddoc;
        this.apeldoc = apeldoc;
        this.nomdoc = nomdoc;
        this.movildoc = movildoc;
        this.maildoc = maildoc;
        this.espedoc = espedoc;
        this.coledoc = coledoc;
        
    }

    //modelo tabla
    public Doctor(String iddoc, String apeldoc, String nomdoc,
            String movildoc, String espedoc, boolean coledoc) {
        this.iddoc = iddoc;
        this.apeldoc = apeldoc;
        this.nomdoc = nomdoc;
        this.movildoc = movildoc;
        this.espedoc = espedoc;
        
    }

    public String getId() {
        return iddoc;
    }

    public void setId(String iddoc) {
        this.iddoc = iddoc;
    }
    public String getNombre() {
        return nomdoc;
    }

    public void setNombre(String nomdoc) {
        this.nomdoc = nomdoc;
    }

    public String getApellidos() {
        return apeldoc;
    }

    public void setApellidos(String apeldoc) {
        this.apeldoc = apeldoc;
    }

    public String getMovil() {
        return movildoc;
    }

    public void setMovil(String movildoc) {
        this.movildoc = movildoc;
    }


    public String getEmail() {
        return maildoc;
    }

    public void setEmail(String maildoc) {
        this.maildoc = maildoc;
    }

    public String getEspecialidad() {
        return espedoc;
    }

    public void setEspecialidad(String espedoc) {
        this.espedoc = espedoc;
    }

    public boolean getColegiado() {
        return coledoc;
    }

    public void setColegiado(boolean coledoc) {
        this.coledoc = coledoc;
    }

}