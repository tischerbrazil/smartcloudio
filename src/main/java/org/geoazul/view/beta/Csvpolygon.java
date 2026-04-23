package org.geoazul.view.beta;





public class Csvpolygon {
	
	
	private String qrcode;
	private String do_vertice;
	private String ao_vertice;
	private String tipo;
	private String azimute;
	private String comprimento;
	private String confrontante_desc;
	private String lado;
	private String indice;
	private String gEOMETRIA_WKT;
	private String invisivel;

 
    public Csvpolygon() {
        // this empty constructor is required
    }
 
    public Csvpolygon(
    		
    		 String qrcode,
    		 String do_vertice,
    		 String ao_vertice,
    		String tipo,
    		 String azimute,
    		 String comprimento,
    		 String confrontante_desc,
    		 String lado,
    		 String indice,
    		 String gEOMETRIA_WKT, String invisivel){
    		
    			this.setQrcode(qrcode);
    			this.setDo_vertice(do_vertice);
    			this.setAo_vertice(ao_vertice);
    			this.setTipo(tipo);
    			this.setAzimute(azimute);
    			this.setComprimento(comprimento);
    			this.setConfrontante_desc(confrontante_desc);
    			this.setLado(lado); 
    			this.setIndice(indice);
    			this.setGEOMETRIA_WKT(gEOMETRIA_WKT);
    			this.setInvisivel(invisivel);
    		
    		
    }

	
    public String getInvisivel() {
		return invisivel;
	}

	public void setInvisivel(String invisivel) {
		this.invisivel = invisivel;
	}


	public String getGeometriaWKT() {
		return gEOMETRIA_WKT;
	}

	public void setGEOMETRIA_WKT(String gEOMETRIA_WKT) {
		this.gEOMETRIA_WKT = gEOMETRIA_WKT;
	}
	
	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}


	public String getLado() {
		return lado;
	}

	public void setLado(String lado) {
		this.lado = lado;
	}


	public String getConfrontante_desc() {
		return confrontante_desc;
	}

	public void setConfrontante_desc(String confrontante_desc) {
		this.confrontante_desc = confrontante_desc;
	}


	public String getComprimento() {
		return comprimento;
	}

	public void setComprimento(String comprimento) {
		this.comprimento = comprimento;
	}


	public String getAzimute() {
		return azimute;
	}

	public void setAzimute(String azimute) {
		this.azimute = azimute;
	}


	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getAo_vertice() {
		return ao_vertice;
	}

	public void setAo_vertice(String ao_vertice) {
		this.ao_vertice = ao_vertice;
	}
	

	public String getDo_vertice() {
		return do_vertice;
	}

	public void setDo_vertice(String do_vertice) {
		this.do_vertice = do_vertice;
	}


	public String getQrcode() {
		return qrcode;
	}
	
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
 
    
}