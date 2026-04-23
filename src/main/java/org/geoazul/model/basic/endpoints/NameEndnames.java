package org.geoazul.model.basic.endpoints;

import static jakarta.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;
import static modules.LoadInitParameter.pg_DAT;
import static modules.LoadInitParameter.pg_HOST;
import static modules.LoadInitParameter.pg_PASS;
import static modules.LoadInitParameter.pg_PORT;
import static modules.LoadInitParameter.pg_USER;
import static modules.LoadInitParameter.save_FILE_PATH;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;

import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.CredentialValidationResult.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;

import org.example.kickoff.config.auth.KickoffIdentityStore;
import org.geoazul.model.app.ApplicationAttributeEntity;
import org.geoazul.model.app.ApplicationEntity;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.website.media.Media;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.CRS;
import org.keycloak.example.oauth.UserData;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jsonb.JacksonUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import java.nio.file.Files;

@Path("/bacen")
public class NameEndnames implements Serializable {

	@Inject
	EntityManager entityManager;

	@Inject
	KickoffIdentityStore kickoffIdentityStore;

	public CredentialValidationResult validate(UsernamePasswordCredential credential) {
		return kickoffIdentityStore.validate(credential);
	}

	public static String indentXmlString(String unformattedXml) {
		try {
			// Parse the XML string into a DOM Document
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// Optional: set this to true to ignore whitespace between elements in the
			// original document,
			// which can help with clean formatting.
			// dbf.setIgnoringElementContentWhitespace(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(unformattedXml)));

			// Use a Transformer to output the Document with indentation
			TransformerFactory tf = TransformerFactory.newInstance();
			// Optional: try setting "indent-number" attribute for specific indentation
			// levels (e.g., 4 spaces)
			// tf.setAttribute("indent-number", Integer.valueOf(4));
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			// Optional: specify encoding
			// t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			// Optional: Omit XML declaration if not needed (e.g., <?xml version="1.0"
			// ...?>)
			// t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			// Transform the DOM source to a String result
			StringWriter sw = new StringWriter();
			t.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();

		} catch (Exception e) {
			// Handle exceptions related to parsing or transformation
			e.printStackTrace();
			return unformattedXml; // Return original string in case of an error
		}
	}

	@Inject
	private UserData userData;

	public AbstractGeometry findAbstractGeometry(Long objectId) {
		try {
			return entityManager.find(AbstractGeometry.class, objectId);
		} catch (Exception ex) {
			return null;
		}
	}

	@Inject
	private HttpServletRequest request;

	@POST
	@Consumes("application/json")
	@Path("/print")
	@Transactional
	public Response create(String entityString) {


		JsonNode retornoApiPix = JacksonUtil.toJsonNode(entityString);

		String id = retornoApiPix.findValue("id").asText();

		String layerId = retornoApiPix.findValue("layerid").asText();
		
		String scale = retornoApiPix.findValue("scale").asText();
		
		String res = retornoApiPix.findValue("res").asText();
		
		Long geometryId = retornoApiPix.findValue("geometryid").asLong();

		JsonNode extCoordinates = retornoApiPix.findValue("ext");


		boolean isArrayNodeExt = extCoordinates.isArray();

		String dataUrl = retornoApiPix.findValue("data").asText();

		ArrayNode arrayNode = (ArrayNode) extCoordinates;
		for (JsonNode node : arrayNode) {
		}

		Random random = new Random();
		int randomInt = random.nextInt(1000000000);

		LocalDateTime data = LocalDateTime.now();
		String fileNameOnly = "image-" + String.valueOf(randomInt) + "_" + ".png";

		String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/" + geometryId.toString()
				+ "/";

		java.nio.file.Path path = java.nio.file.Paths.get(appDirectory);

		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				// fail to create directory
				e.printStackTrace();
			}
		}

		String filePath = appDirectory + fileNameOnly;

		String urlDirectory = "/files/" + userData.getRealmEntity() + "/" + geometryId.toString() + "/";

		String urlPath = urlDirectory + fileNameOnly;

		String fileNameOnly2 = "image-" + String.valueOf(randomInt) + "_" + ".png";
		String filePath2 = appDirectory + fileNameOnly2;

		try {
			saveBase64ImageToFile(dataUrl, filePath);
		} catch (IOException e) {
			System.err.println("An error occurred: " + e.getMessage());
			e.printStackTrace();
		}

		Integer x1 = arrayNode.get(0).intValue();
		Integer y1 = arrayNode.get(1).intValue();
		Integer x2 = arrayNode.get(2).intValue();
		Integer y2 = arrayNode.get(3).intValue();

		try {
			BufferedImage original = ImageIO.read(new File(filePath));
			BufferedImage cropped = original.getSubimage(x1, y1, x2 - x1, y2 - y1);
			ImageIO.write(cropped, "png", new File(filePath2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		
		
		String fileNamePDF = "image-" + String.valueOf(randomInt) + "_" + ".pdf";

		String destinationPDF = appDirectory + fileNamePDF;
		
		
	    String urlFinalPDF = userData.getGeoazulURLFiles() + "/" + geometryId.toString() + "/" + fileNamePDF;
		

		
		


		String servername = request.getServerName().replace("-", "_").replace(".", "_");
		String tenant = "geoazul_" + servername;

	

		String urlFinal = userData.getGeoazulURLFiles() + "/" + geometryId.toString() + "/" + fileNameOnly;
		


		HashMap<String, Object> param_map = new HashMap<String, Object>();

		urlPath = null;
		
		
		
		

		param_map.put("obraId", geometryId);
		param_map.put("svgUrl", urlFinal);
		param_map.put("svgUrlV", urlPath);
		param_map.put("svgUrlH", urlPath);

		param_map.put("escala", "1 : " + scale);

		param_map.put("svgUrlQuadra", urlPath);

		
		

		param_map.put("medCentralUrl", "https://smartcloudio.com.br/jakarta.faces.resource/images/north.png.xhtml");


		AbstractGeometry obraSIGEF = findAbstractGeometry(geometryId);
		
		Media mediaNew = new Media();
		mediaNew.setAbstractGeometry(obraSIGEF);
		mediaNew.setFilename(urlFinalPDF);
		mediaNew.setMimeType("application/pdf");
		mediaNew.setReleaseDate(LocalDateTime.now());
		mediaNew.setTitle("Mapa PDF");
		entityManager.persist(mediaNew);
		entityManager.flush();
		
		
		String urlFinalQR = userData.getGeoazulURLFiles() + "/viewer/" + mediaNew.getId();
		
		String fileNameQRCODE = "image-" + String.valueOf(randomInt) + "_qr" + ".png";
		String destinationFileQRCODE = appDirectory + fileNameQRCODE;
		try {
			
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix matrix;
			matrix = writer.encode(urlFinalQR, BarcodeFormat.QR_CODE, 300, 300);
			BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
			File outputFile = new File(destinationFileQRCODE);
			ImageIO.write(image, "png", outputFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		param_map.put("logoUrl", urlPath);
		param_map.put("arrowNorthUrl", destinationFileQRCODE);
		
		

		for (ApplicationAttributeEntity atrib : obraSIGEF.getLayer().getApplicationEntity().getAttributes()) {

			switch (atrib.getName()) {

			case "mun_uf":
				param_map.put("mun_uf", atrib.getValue());
				break;

			case "comarca":
				param_map.put("comarca", atrib.getValue());
				break;

			case "numero_rt":
				param_map.put("numero_rt", atrib.getValue());
				break;

			case "nome_rt":
				param_map.put("nome_rt", atrib.getValue());
				break;

			case "referencia_rt":
				param_map.put("referencia_rt", atrib.getValue());
				break;

			case "header1":
				param_map.put("header1", atrib.getValue());
				break;

			case "header2":
				param_map.put("header2", atrib.getValue());
				break;

			case "header3":
				param_map.put("header3", atrib.getValue());
				break;

			case "header4":
				param_map.put("header4", atrib.getValue());
				break;

			case "header_img":
		
				param_map.put("header_img", userData.getGeoazulURLFiles() + atrib.getValue());
				break;

			case "exec_img":
		
				param_map.put("exec_img", userData.getGeoazulURLFiles() + atrib.getValue());
				break;

			case "attrib_data":
				param_map.put("attrib_data", atrib.getValue());
				break;

			case "cm_projecao":
				param_map.put("cm_projecao", atrib.getValue());
				break;

			case "cm_datum":
				param_map.put("cm_datum", atrib.getValue());
				break;

			case "cm_mc":
				param_map.put("cm_mc", atrib.getValue());
				break;

			case "cm_vertice":
				param_map.put("cm_vertice", atrib.getValue());
				break;

			case "cm_latitude":
				param_map.put("cm_latitude", atrib.getValue());
				break;

			case "cm_longitude":
				param_map.put("cm_longitude", atrib.getValue());
				break;

			case "cm_ce":
				param_map.put("cm_ce", atrib.getValue());
				break;

			case "cm_cm":
				param_map.put("cm_cm", atrib.getValue());
				break;

			case "cm_dm":
				param_map.put("cm_dm", atrib.getValue());
				break;

			case "cm_va":
				param_map.put("cm_va", atrib.getValue());
				break;

			case "cm_data":
				param_map.put("cm_data", atrib.getValue());
				break;

			}
		}

		
		Connection con;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(
					"jdbc:postgresql://" + pg_HOST + ":" + pg_PORT + "/" + pg_DAT + "?currentSchema=" + tenant, pg_USER,
					pg_PASS);

			String reltype = "Relatorio";
			String reportFileUser = save_FILE_PATH + "/birt/" + reltype + ".jasper";

			

			param_map.put("REPORT_CONNECTION", con);
			JasperPrint jp;
			jp = JasperFillManager.fillReport(reportFileUser, param_map, con);

			JasperExportManager.exportReportToPdfFile(jp, destinationPDF);

			con.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String message = "{\"message\":\"Sucess\"}";

		return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity(message).build();
	}

	public static void saveBase64ImageToFile(String base64DataUrl, String destinationPath) throws IOException {
		// 1. Extract the pure Base64 string by removing the prefix
		String base64ImageString = base64DataUrl.substring(base64DataUrl.indexOf(",") + 1);

		// 2. Decode the Base64 string into a byte array using Java 8+ Base64 API
		byte[] decodedImageBytes = Base64.getDecoder().decode(base64ImageString);

		// 3. Define the destination file path
		java.nio.file.Path destinationFile = java.nio.file.Paths.get(destinationPath);

		// 4. Write the byte array to the file
		Files.write(destinationFile, decodedImageBytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/geoson/parcela/{geometry}")
	public Response listGeoJsonParcela(@Context HttpServletRequest request, @PathParam("geometry") Long geometryId) {

		String authHeader = request.getHeader("Authorization");
		String secretToken = request.getHeader("X-Secure-Token");

		Status status = Status.INVALID;

		if (authHeader != null && authHeader.startsWith("Basic")) {
			// Decoding Base64 (requires java.util.Base64)
			String base64Credentials = authHeader.substring("Basic ".length()).trim();
			String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
			// Credentials format is "user:password"
			final String[] values = credentials.split(":", 2);
			String username = values[0];
			String password = values[1];

			UsernamePasswordCredential credential = new UsernamePasswordCredential(username, password);
			CredentialValidationResult crVR = validate(credential);
			status = crVR.getStatus();

		}

		if (status == VALID) {

			Polygon polSearch = entityManager.find(Polygon.class, geometryId);

			if (polSearch == null) {
				String message = "Parcela não encontrada!";
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(message).build();
			}

			TypedQuery<Layer> parcels = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
			parcels.setParameter("id", polSearch.getLayer().getId());
			Layer layerFind = parcels.getSingleResult();

			Integer appSRID = layerFind.getApplicationEntity().getEpsg();
			Integer layerSRID = layerFind.getEpsg();

			String consultax = "select " + "abstractge0_.id ," + "abstractge0_.enabled," + "abstractge0_.imovel_id,"
					+ "abstractge0_.origin_id," + "abstractge0_.iconflag," + "abstractge0_.layer_id,"
					+ "abstractge0_.nome," + "abstractge0_.parte," + "abstractge0_.situacao,"
					// + "abstractge0_.dtype,"
					+ "abstractge0_.field,"
					+ "public.st_transform(public.st_geomfromtext(public.st_astext(polygongeo1_.geometry), " + layerSRID
					+ "), " + appSRID + ")  as geometry," + "polygongeo1_.area," + "polygongeo1_.perimetro "
					+ "from APP_GEOMETRY abstractge0_ " + "left outer join " + "APP_POLYGON polygongeo1_ "
					+ "on abstractge0_.id=polygongeo1_.id "
					+ "WHERE polygongeo1_.geometry is not null and abstractge0_.enabled = true  "
					+ "and abstractge0_.situacao > 0 AND abstractge0_.id=" + geometryId
					+ " AND public.st_srid(polygongeo1_.geometry) = " + layerSRID;

			Query findAllQuery33 = entityManager.createNativeQuery(consultax, Polygon.class);

			List<Polygon> results = findAllQuery33.getResultList();
			if (results.size() > 0) {

				// ====================================================
				SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
				tb.setName("Parcela Geometry");
				try {
					tb.add("geometry", Polygon.class, CRS.decode("EPSG:" + appSRID.toString()));
				} catch (FactoryException e) {
				}

				tb.add("nome", String.class);

				tb.add("situacao", Integer.class);

				SimpleFeatureType schema = tb.buildFeatureType();

				DefaultFeatureCollection fc = new DefaultFeatureCollection();

				SimpleFeatureBuilder fb = null;
				for (Polygon polygon : results) {
					fb = new SimpleFeatureBuilder(schema);
					fb.add(polygon.getGeometry());
					fb.add(polygon.getNome());
					fb.add(polygon.getSituacao().intValue());
					fc.add(fb.buildFeature(polygon.getId().toString()));
				}

				int decimals = 14;
				GeometryJSON gjson = new GeometryJSON(decimals);

				FeatureJSON fj = new FeatureJSON(gjson);
				StringWriter writer88 = new StringWriter();
				try {
					fj.writeFeatureCollection(fc, writer88);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return Response.ok(writer88.toString(), MediaType.APPLICATION_JSON).build();

			}

		}

		String message = "You do not have sufficient permissions to access this resource.";
		return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(message).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/geoson/parcelas/{layer}")
	public Response listGeoJsonParcelas(@Context HttpServletRequest request, @PathParam("layer") Long layerId) {

		String authHeader = request.getHeader("Authorization");
		String secretToken = request.getHeader("X-Secure-Token");

		Status status = Status.INVALID;

		if (authHeader != null && authHeader.startsWith("Basic")) {
			// Decoding Base64 (requires java.util.Base64)
			String base64Credentials = authHeader.substring("Basic ".length()).trim();
			String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
			// Credentials format is "user:password"
			final String[] values = credentials.split(":", 2);
			String username = values[0];
			String password = values[1];

			UsernamePasswordCredential credential = new UsernamePasswordCredential(username, password);
			CredentialValidationResult crVR = validate(credential);
			status = crVR.getStatus();

		}

		if (status == VALID) {

			TypedQuery<Layer> layer = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
			layer.setParameter("id", layerId);
			Layer layerFind = layer.getResultList().stream().findFirst().orElse(null);

			if (layerFind == null) {
				String message = "Camada não encontrada!";
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(message).build();
			}

			Integer appSRID = layerFind.getApplicationEntity().getEpsg();
			Integer layerSRID = layerFind.getEpsg();

			String consultax = "select " + "abstractge0_.id ," + "abstractge0_.enabled," + "abstractge0_.imovel_id,"
					+ "abstractge0_.origin_id," + "abstractge0_.iconflag," + "abstractge0_.layer_id,"
					+ "abstractge0_.nome," + "abstractge0_.parte," + "abstractge0_.situacao,"
					// + "abstractge0_.dtype,"
					+ "abstractge0_.field,"
					+ "public.st_transform(public.st_geomfromtext(public.st_astext(polygongeo1_.geometry), " + layerSRID
					+ "), " + appSRID + ")  as geometry," + "polygongeo1_.area," + "polygongeo1_.perimetro "
					+ "from APP_GEOMETRY abstractge0_ " + "left outer join " + "APP_POLYGON polygongeo1_ "
					+ "on abstractge0_.id=polygongeo1_.id "
					+ "WHERE polygongeo1_.geometry is not null and abstractge0_.enabled = true  "
					+ "and abstractge0_.situacao > 0 AND abstractge0_.layer_id=" + layerId
					+ " AND public.st_srid(polygongeo1_.geometry) = " + layerSRID;

			Query findAllQuery33 = entityManager.createNativeQuery(consultax, Polygon.class);

			List<Polygon> results = findAllQuery33.getResultList();
			if (results.size() > 0) {

				// ====================================================
				SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
				tb.setName("Polygon Geometry");
				try {
					tb.add("geometry", Polygon.class, CRS.decode("EPSG:" + appSRID.toString()));
				} catch (FactoryException e) {
				}

				tb.add("nome", String.class);

				tb.add("situacao", Integer.class);

				SimpleFeatureType schema = tb.buildFeatureType();

				DefaultFeatureCollection fc = new DefaultFeatureCollection();

				SimpleFeatureBuilder fb = null;
				for (Polygon polygon : results) {
					fb = new SimpleFeatureBuilder(schema);
					fb.add(polygon.getGeometry());
					fb.add(polygon.getNome());
					fb.add(polygon.getSituacao().intValue());
					fc.add(fb.buildFeature(polygon.getId().toString()));
				}

				int decimals = 14;
				GeometryJSON gjson = new GeometryJSON(decimals);

				FeatureJSON fj = new FeatureJSON(gjson);
				StringWriter writer88 = new StringWriter();
				try {
					fj.writeFeatureCollection(fc, writer88);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return Response.ok(writer88.toString(), MediaType.APPLICATION_JSON).build();

			}

		}

		String message = "Acesso negado!";
		return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(message).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/parcela/{parcel}")
	public Response listParcel(@Context HttpServletRequest request, @PathParam("parcel") Long parcelId) {

		String authHeader = request.getHeader("Authorization");
		String secretToken = request.getHeader("X-Secure-Token");

		Status status = Status.INVALID;

		if (authHeader != null && authHeader.startsWith("Basic")) {
			// Decoding Base64 (requires java.util.Base64)
			String base64Credentials = authHeader.substring("Basic ".length()).trim();
			String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
			// Credentials format is "user:password"
			final String[] values = credentials.split(":", 2);
			String username = values[0];
			String password = values[1];

			UsernamePasswordCredential credential = new UsernamePasswordCredential(username, password);
			CredentialValidationResult crVR = validate(credential);
			status = crVR.getStatus();

		}

		if (status == VALID) {

			TypedQuery<Polygon> polygon = entityManager.createNamedQuery(Polygon.SURFACE_ID, Polygon.class);
			polygon.setParameter("id", parcelId);
			Polygon polygonFind = polygon.getResultList().stream().findFirst().orElse(null);

			if (polygonFind == null) {
				String message = "Parcela não encontrada!";
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(message).build();
			}

			String servername = request.getServerName().replace("-", "_").replace(".", "_");
			String tenant = "geoazul_" + servername;

			String consultax = "SELECT '<?xml version=\"1.0\" encoding=\"UTF-8\"?>' ||\n" + "xmlelement(\n"
					+ "    NAME kml,\n" + "    XMLATTRIBUTES ('http://www.opengis.net/kml/2.2' AS xmlns),\n"
					+ "    xmlelement(\n" + "        NAME \"Document\",\n"
					+ "        xmlelement(NAME NAME, 'Área(s) para Financiamento Agrícola SICOR'),\n"
					+ "            xmlagg(\n" + "                xmlelement(\n"
					+ "                    NAME \"Placemark\",\n"
					+ "                    xmlelement(NAME NAME, g.nome),\n"
					+ "                    xmlelement(NAME \"description\", 'Parcela para Financiamento'),\n"
					+ "                    public.ST_AsKML(public.ST_Transform(public.ST_Force3D(geometry) , 4326)) \\:\\: XML\n"
					+ "                )\n" + "            )\n" + "        )\n" + "    )\n" + "    FROM " + tenant
					+ ".app_polygon p join \n" + tenant + ".app_geometry g on (p.id = g.id) \n" + " where g.id = :id";

			Query query = entityManager.createNativeQuery(consultax, Object.class);
			query.setParameter("id", parcelId);
			Object result = query.getSingleResult();

			if (result == null) {
				String message = "Parcela não encontrada!";
				return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(message).build();
			}

			String title = (String) result;

			String prettyXml = indentXmlString(title);

			return Response.ok(prettyXml, MediaType.APPLICATION_JSON).build();
		}

		String message = "Acesso negado!";
		return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN) // or MediaType.APPLICATION_JSON,
																						// etc.
				.entity(message).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/parcelas/{layer}")
	public Response listParcels(@Context HttpServletRequest request, @PathParam("layer") Long layerId) {

		String authHeader = request.getHeader("Authorization");
		String secretToken = request.getHeader("X-Secure-Token");

		Status status = Status.INVALID;

		if (authHeader != null && authHeader.startsWith("Basic")) {
			// Decoding Base64 (requires java.util.Base64)
			String base64Credentials = authHeader.substring("Basic ".length()).trim();
			String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
			// Credentials format is "user:password"
			final String[] values = credentials.split(":", 2);
			String username = values[0];
			String password = values[1];

			UsernamePasswordCredential credential = new UsernamePasswordCredential(username, password);
			CredentialValidationResult crVR = validate(credential);
			status = crVR.getStatus();

		}

		TypedQuery<Layer> layer = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
		layer.setParameter("id", layerId);
		Layer layerFind = layer.getResultList().stream().findFirst().orElse(null);

		if (layerFind == null) {
			String message = "Camada não encontrada!";
			return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(message).build();
		}

		if (status == VALID && layerFind != null) {

			String servername = request.getServerName().replace("-", "_").replace(".", "_");
			String tenant = "geoazul_" + servername;

			String consultax = "SELECT '<?xml version=\"1.0\" encoding=\"UTF-8\"?>' ||\n" + "xmlelement(\n"
					+ "    NAME kml,\n" + "    XMLATTRIBUTES ('http://www.opengis.net/kml/2.2' AS xmlns),\n"
					+ "    xmlelement(\n" + "        NAME \"Document\",\n"
					+ "        xmlelement(NAME NAME, 'Área(s) para Financiamento Agrícola SICOR'),\n"
					+ "            xmlagg(\n" + "                xmlelement(\n"
					+ "                    NAME \"Placemark\",\n"
					+ "                    xmlelement(NAME NAME, g.nome),\n"
					+ "                    xmlelement(NAME \"description\", 'Parcela para Financiamento'),\n"
					+ "                    public.ST_AsKML(public.ST_Transform(public.ST_Force3D(geometry) , 4326)) \\:\\: XML\n"
					+ "                )\n" + "            )\n" + "        )\n" + "    )\n" + "    FROM " + tenant
					+ ".app_polygon p join \n" + tenant + ".app_geometry g on (p.id = g.id) \n"
					+ " where g.layer_id = :id";

			Query query = entityManager.createNativeQuery(consultax, Object.class);
			query.setParameter("id", layerId);
			Object result = query.getSingleResult();

			String title = (String) result;

			String prettyXml = indentXmlString(title);

			return Response.ok(prettyXml, MediaType.APPLICATION_JSON).build();
		}

		String message = "Acesso negado!";
		return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(message).build();

	}

}
