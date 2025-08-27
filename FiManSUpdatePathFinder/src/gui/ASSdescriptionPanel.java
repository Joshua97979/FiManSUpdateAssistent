package gui;
//By Joshua Froitzheim 2023
import java.awt.Color;
import java.awt.Font;

import src.Config;
import src.Update;

@SuppressWarnings("serial")
public class ASSdescriptionPanel extends FimansDescriptionPanel{
	
    public ASSdescriptionPanel(Update update, Font font, Config configFile) {
    	super(update, font, configFile);
    	
    	this.addSpace(30);
    	
    	this.addTitel(update.toString());
    	
    	String text = "";
    	if (update.getLink() != null) {
    		this.addLink("Download", update.getLink());
    		text += "Oder: ";
		}
    	
    	String date = update.getReleasedate();
    	String newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
    	String path = configFile.pathToAssFiles + "\\AJI"+ update.getVersion() +"_" + newDate;
    	this.addOpenFolderButtonAndCopyableText(text + path, path);
    	
    	this.addSpace(50);
    	
    	if (update.getVersion() == 5720) {
    		this.addLabel(new Object[] {"Dieses Update muss von außerhalb der Anwendung installiert werden!"}, font, Color.red);
    		this.addSpace(30);
    	};
    	
    	this.addTitel(getNbr() + ". Vorbereitung");
    	this.addSpace(10);
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Bibliotheken:");
    	
    	this.addLabel("Bibliothek mit den Programmen zu VEDA SE Basis:");
    	assPgmBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/X1150R) OBJTYPE(*PGM)");
    	
    	this.addLabel("Bibliothek mit den Daten zu VEDA SE Basis:");
    	assDtaBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/XPDIVPU) OBJTYPE(*FILE)");
    	
    	this.addLabel("SE-Tools (AES) obj. Dateien-Bibliothek (DTA):");
    	aesDtaBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/V§DDCPU) OBJTYPE(*FILE)");
    	
    	this.addLabel("SE-Tools (AES) obj. Programm-Bibliothek (LIB):");
    	aesPgmBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/CRTDIAPGM) OBJTYPE(*CMD)");
    	
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	addSection_FibInsDirPath();
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Installierte Versionen überprüfen");
    	this.addSpace(10);
    	
    	add1AtNbr(2);
    	this.addBoldLabel(getNbr() + " Classic:");
    	this.addButtonAndCopyableText("DSPDTAARA DTAARA(ASSLIB)");
    	this.addSpace(10);
    	add1AtNbr(2);
    	addSubSection_LookUpGraphicVersion();
    	this.addLabel("seBasis = ASS");
    	
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
    	
    	if (update.getVersion() == 5720) {
    		this.addLabel(new Object[] {"Dieses Update muss von außerhalb der Anwendung installiert werden!"}, font, Color.red);
    		this.addLabel("21.  VEDA SE Basis -> 83.  Installationen / Updates -> 2.  Update installieren");
    		this.addButtonAndLabel("Vorher prüfen, ob es auch wirklich keine Sperre auf die XFIRMP gibt", new Object[] {"WRKOBJLCK OBJ(", assDtaBibTextField, "/XFIRMP) OBJTYPE(*FILE)"});
    		this.addSpace(30);
    	};
    	
    	
    	date = update.getReleasedate();
    	newDate = date.substring(6, 8);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
    	
    	String savf = "ASS$" + newDate + ".SAVF";
    	
    	date = update.getReleasedate();
    	newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
    	String vins = "ASS" + update.getVersion() + newDate + ".VINS";
    	
    	date = update.getReleasedate();
    	newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
    	path = configFile.pathToAssFiles + "\\AJI"+ update.getVersion() +"_" + newDate;
    	this.addOpenFolderButtonAndCopyableText(new Object[] {savf + " und " + vins + " kopieren."}, new Object[] {path});
    	this.addButtonAndCopyableText(new Object[] {"nach: ", fibInsDirPathTextField}, new Object[] {fibInsDirPathTextField}, true, false, true);
    	
    	this.addLabel("Menü-Weg: 40.  VEDA SE Basis -> 83.  Installationen / Updates -> 2.  Update installieren");
    	String copyText = "ASS\n" + update.getVersion() + "\n" + update.getReleasedate();
    	String displayedText = "Produkt: ASS  Release: " + update.getVersion() + "  Datum: " + update.getReleasedate();
    	this.addButtonAndCopyableText(displayedText, copyText);
    	
    	this.addSpace(10);
    	
    	this.addCopyableText(new Object[] {assPgmBibTextField, " <- Bibliothek mit den Programmen zu VEDA SE Basis"}, true);
    	this.addCopyableText(new Object[] {assDtaBibTextField, " <- Bibliothek mit den Daten zu VEDA SE Basis"}, true);
    	this.addCopyableText(new Object[] {aesDtaBibTextField, " <- SE-Tools (AES) obj. Dateien-Bibliothek"}, true);
    	this.addCopyableText(new Object[] {aesPgmBibTextField, " <- SE-Tools (AES) obj. Programm-Bibliothek"}, true);
    	
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
    	this.addBoldLabel(getNbr() + " Setup.JAR Ausf\u00FChren");
    	
    	path = "/pictures/ASS_1_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	this.addLabel("iJUMP2 Ordner angeben (bzw. Ordner mit .jnlp-Datei):");
    	this.addButtonAndCopyableText(new Object[] {"Pfad: ",  fibInsDirPathTextField}, new Object[] {fibInsDirPathTextField});
    	
    	path = "/pictures/ASS_2_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/ASS_3_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/ASS_4_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/ASS_5_s.png";
    	this.addImage(path);
    	
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	addSection_CheckForSuccessfulInstallation();
		
    	this.addSpace(50);
    	
    	add1AtNbr(0);
    	this.addTitel(getNbr() + ". Nacharbeiten");
    	this.addSpace(10);
    	
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