package org.geoazul.view.beta;





public class Book {
	
	
	private String qrcode;
	private String codigo;
	private String metodo_posicionamento;
	private String tipo_vertice;
	private String sigma_x;
	private String sigma_y;
	private String sigma_z;
	private String lado;
	private String indice;
	private String x;
	private String y;
	private String z;
	private String gEOMETRIA_WKT;
	private String invisivel;

 
    public Book() {
        // this empty constructor is required
    }
 
    public Book(
    		
    		 String qrcode,
    		 String codigo,
    		 String metodo_posicionamento,
    		String tipo_vertice,
    		 String sigma_x,
    		 String sigma_y,
    		 String sigma_z,
    		 String lado,
    		 String indice,
    		 String x,
    		 String y,
    		 String z,
    		 String gEOMETRIA_WKT, String invisivel){
    		
    			this.setQrcode(qrcode);
    			this.setCodigo(codigo);
    			this.setMetodo_posicionamento(metodo_posicionamento);
    			this.setTipo_vertice(tipo_vertice);
    			this.setSigma_x(sigma_x);
    			this.setSigma_y(sigma_y);
    			this.setSigma_z(sigma_z);
    			this.setLado(lado); 
    			this.setIndice(indice);
    			this.setX(x); 
    			this.setY(y);
    			this.setZ(z);
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


	public String getZ() {
		return z;
	}

	public void setZ(String z) {
		this.z = z;
	}


	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}


	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
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


	public String getSigma_z() {
		return sigma_z;
	}

	public void setSigma_z(String sigma_z) {
		this.sigma_z = sigma_z;
	}


	public String getSigma_y() {
		return sigma_y;
	}

	public void setSigma_y(String sigma_y) {
		this.sigma_y = sigma_y;
	}


	public String getSigma_x() {
		return sigma_x;
	}

	public void setSigma_x(String sigma_x) {
		this.sigma_x = sigma_x;
	}


	public String getTipo_vertice() {
		return tipo_vertice;
	}

	public void setTipo_vertice(String tipo_vertice) {
		this.tipo_vertice = tipo_vertice;
	}


	public String getMetodo_posicionamento() {
		return metodo_posicionamento;
	}

	public void setMetodo_posicionamento(String metodo_posicionamento) {
		this.metodo_posicionamento = metodo_posicionamento;
	}
	

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getQrcode() {
		return qrcode;
	}
	
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
 
    
}