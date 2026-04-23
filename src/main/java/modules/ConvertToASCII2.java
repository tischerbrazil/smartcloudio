package modules;

public class ConvertToASCII2 {

	public String convertToASCII2(String text) {
	    return text.replaceAll("[ãâàáä]", "a")
	                .replaceAll("[êèéë]", "e")
	                .replaceAll("[îìíï]", "i")
	                .replaceAll("[õôòóö]", "o")
	                .replaceAll("[ûúùü]", "u")
	                .replaceAll("[ÃÂÀÁÄ]", "A")
	                .replaceAll("[ÊÈÉË]", "E")
	                .replaceAll("[ÎÌÍÏ]", "I")
	                .replaceAll("[ÕÔÒÓÖ]", "O")
	                .replaceAll("[ÛÙÚÜ]", "U")
	                .replace('ç', 'c')
	                .replace('Ç', 'C')
	                .replace('ñ', 'n')
	                .replace('Ñ', 'N')
	                .replace(' ', '_');
	}
}
