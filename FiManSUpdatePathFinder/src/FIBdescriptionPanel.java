import java.awt.Font;
@SuppressWarnings("serial")
public class FIBdescriptionPanel extends FimansDescriptionPanel {
	
	public FIBdescriptionPanel(Update update, Font font, Config configFile) {
		super(update, font, configFile);
		
    	this.addSpace(30);
    	
    	this.addTitel(update.toString());
    	
    	String text = "";
    	if (update.getLink() != null) {
    		this.addLink("Download", update.getLink());
    		text += "Oder: ";
		}
    	
    	String path = configFile.pathToFibUpdateFiles + "\\FIB" + update.getVersion();
    	this.addOpenFolderButtonAndCopyableText(new Object[] {text + path}, new Object[] {path});
    	this.addSpace(50);
    	
    	this.addTitel(getNbr() + ". Vorbereitung");
    	this.addSpace(10);
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Bibliotheken:");
    	
    	this.addLabel("FA Finanzen-Programmbibliothek:");
    	fibPgmBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/F1000X) OBJTYPE(*PGM)");
    	
    	this.addLabel("FA Finanzen-Datenbibliothek:");
    	fibDtaBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/FBUCHPU) OBJTYPE(*FILE)");
    	
    	this.addLabel("FA Finanzen-Quellenbibliothek:");
    	fibSrcBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/QRPGSRC) OBJTYPE(*FILE)");
    	
    	this.addLabel("Bibliothek mit den Daten zu VEDA SE Basis:");
    	assDtaBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/XPDIVPU) OBJTYPE(*FILE)");
    	
    	this.addLabel("Bibliothek mit den Programmen zu VEDA SE Basis:");
    	assPgmBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/X1150R) OBJTYPE(*PGM)");
    	
    	this.addSpace(50);
    	
    	this.addLabel("FA Finanzen-Modifikationsbibliothek:");
    	fibModBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/QRPGSRC) OBJTYPE(*FILE)");
    	
    	this.addSpace(50);
    	
    	
    	add1AtNbr(1);
    	addSection_FibInsDirPath();
    	
    	this.addSpace(50);
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Installierte Versionen überprüfen:");
    	
    	this.addSpace(10);
    	add1AtNbr(2);
    	this.addBoldLabel(getNbr() + " Classic:");
    	this.addButtonAndCopyableText("DSPDTAARA DTAARA(FIBLIB)");
    	this.addSpace(10);
    	add1AtNbr(2);
    	addSubSection_LookUpGraphicVersion();
    	this.addLabel("faFinanzen = FIB");
    	
    	this.addSpace(50);
    	add1AtNbr(2);
    	addSection_JdkVersion();
    	
    	this.addSpace(50);
    	add1AtNbr(2);
    	addSection_osVersion();
    	
    	this.addSpace(50);
    	
    	add1AtNbr(2);
    	addSection_tomCatJobName();
    	
    	this.addSpace(50);
    	
    	add1AtNbr(2);
    	addSection_tomCatVersion();
    	
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	addSection_alternativeTomCatCommands();
        
        this.addSpace(50);
        
        add1AtNbr(0);
        this.addTitel(getNbr() + ". Installation Classic Teil");
        this.addSpace(10);

    	String date = update.getReleasedate();
    	String newDate = date.substring(6, 8);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
    	
    	String savf = "FIB$" + newDate + ".SAVF";
    	
    	date = update.getReleasedate();
    	newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
    	String vins = "FIB" + update.getVersion() + newDate + ".VINS";
    	
    	this.addLabel(savf + " und " + vins + " kopieren nach vswappl/ptf.");
    	
    	this.addLabel("Menü-Weg: 40.  VEDA SE Basis -> 83.  Installationen / Updates -> 2.  Update installieren");
    	String copyText = "FIB\n" + update.getVersion() + "\n" + update.getReleasedate();
    	String displayedText = "Produkt: FIB  Release: " + update.getVersion() + "  Datum: " + update.getReleasedate();
    	this.addButtonAndCopyableText(displayedText, copyText);
    	
    	this.addSpace(10);
    	
    	//The order is different than for ASS updates
    	this.addCopyableText(new Object[] {fibPgmBibTextField, " <- FA Finanzen-Programmbibliothek"}, true);
    	this.addCopyableText(new Object[] {fibDtaBibTextField, " <- FA Finanzen-Datenbibliothek"}, true);
    	this.addCopyableText(new Object[] {fibSrcBibTextField, " <- FA Finanzen-Quellenbibliothek"}, true);
    	this.addCopyableText(new Object[] {assDtaBibTextField, " <- Bibliothek mit den Daten zu VEDA SE Basis"}, true);
    	this.addCopyableText(new Object[] {assPgmBibTextField, " <- Bibliothek mit den Programmen zu VEDA SE Basis"}, true);
    	
    	this.addSpace(50);
    	
    	add1AtNbr(0);
    	this.addTitel(getNbr() + ". Installation Grafik Teil");
    	this.addSpace(10);
    	
		add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Benutzer abmelden:");
    	this.addButtonAndLabel("Angemeldete Benutzer nachschauen", new Object[] {"WRKOBJLCK OBJ(", assDtaBibTextField,"/XPDIVPU) OBJTYPE(*FILE)\r\n"});
    	this.addLabel("Dann F6");
    	
    	this.addSpace(10);
    	this.addLabel("QQF-Benutzer mit Auswahl 4 zwangsabmelden:");
    	this.addLabel("(Restliche Benutzer können bei Updates angemeldet bleiben)");

        this.addSpace(50);
        
        add1AtNbr(1);
        this.addBoldLabel(getNbr() + " TomCat herunterfahren");
        addSubSection_tomCatDown();
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " SETUP-Programm:");
    	
    	date = update.getReleasedate();
    	newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
    	
    	path = configFile.pathToFibUpdateFiles + "\\FIB" + update.getVersion();
		text = "FJI_" + update.getVersion() + "_" + newDate + "_AU.jar entzippen unter: ";
    	this.addLabel(text);
    	this.addOpenFolderButtonAndCopyableText(path, path);
    	
    	this.addSpace(50);
    	this.addLabel("In diesem \"Setup.Jar\" Ausf\u00FChren");
    	
    	path = "/pictures/FIB_1_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	this.addLabel(new Object[] {"iJUMP2 Ordner angeben (bzw. Ordner mit .jnlp-Datei):"});
    	this.addButtonAndCopyableText(new Object[] {"Pfad: ",  fibInsDirPathTextField}, new Object[] {fibInsDirPathTextField}, false, true);
    	
    	path = "/pictures/FIB_2_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/FIB_3_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/FIB_4_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/FIB_5_s.png";
    	this.addImage(path);
    	
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	addSection_CheckForSuccessfulInstallation();
		
		this.addSpace(50);
		add1AtNbr(0);
    	this.addTitel(getNbr() + ". Nacharbeiten");
    	this.addSpace(10);
    	
    	add1AtNbr(1);
    	
    	path += update.getVersion();
    	addSection_ModificationsUpdate(configFile.pathToFibUpdateFiles + "\\FIB" + update.getVersion());
    	
    	this.addSpace(50);
    	add1AtNbr(1);
    	addSection_CompanySpecificRecordTypes();
    	
    	this.addSpace(50);
    	add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Systeme starten:");
		this.addLabel("TomCat hochfahren:");
		addSubSection_tomCatUp();
    	
    	this.addSpace(50);
    }
	
	@Override
	public String toString() {
		return this.toString();
	}
}
